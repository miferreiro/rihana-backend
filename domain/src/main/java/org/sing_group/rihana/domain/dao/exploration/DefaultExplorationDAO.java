/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2021 David A. Ruano Ordás, José Ramón Méndez Reboredo,
 * 		Miguel Ferreiro Díaz
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.rihana.domain.dao.exploration;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.sing_group.rihana.domain.dao.DAOHelper;
import org.sing_group.rihana.domain.dao.spi.acl.permission.PermissionDAO;
import org.sing_group.rihana.domain.dao.spi.exploration.ExplorationDAO;
import org.sing_group.rihana.domain.dao.spi.user.UserDAO;
import org.sing_group.rihana.domain.entities.exploration.Exploration;
import org.sing_group.rihana.domain.entities.sign.SignType;
import org.sing_group.rihana.domain.entities.user.User;

@Default
@Transactional(value = Transactional.TxType.MANDATORY)
public class DefaultExplorationDAO implements ExplorationDAO {

	@Resource
	private SessionContext context;

	@Inject
	private UserDAO userDao;

	@Inject
	private PermissionDAO permissionDao;

	@PersistenceContext
	protected EntityManager em;

	protected DAOHelper<String, Exploration> dh;

	public DefaultExplorationDAO() {
		super();
	}

	public DefaultExplorationDAO(EntityManager em) {
		this.em = em;
		createDAOHelper();
	}

	@PostConstruct
	protected void createDAOHelper() {
		this.dh = DAOHelper.of(String.class, Exploration.class, this.em);
	}

	@Override
	public Exploration getExploration(String id) {
		return this.dh.get(id).filter(e -> !e.isDeleted())
			.orElseThrow(() -> new IllegalArgumentException("Unknown exploration: " + id));
	}

	@Override
	public Exploration getExplorationDeleted(String id) {
		return this.dh.get(id).filter(Exploration::isDeleted)
			.orElseThrow(() -> new IllegalArgumentException("Unknown deleted exploration: " + id));
	}

	@Override
	public Stream<Exploration> listExplorationsByUserInDateRange(Integer page, Integer pageSize, User user,
																 Date initialDate, Date finalDate,
																 Set<SignType> signTypes, String operator) {
		return this.listExplorations(page, pageSize, user, initialDate, finalDate, signTypes, operator).stream();
	}

	@Override
	public int countAllExplorations() {
		return dh.list().size();
	}

	@Override
	public int countExplorationsByUserAndSignTypesInDateRange(User user, Date initialDate, Date finalDate,
															  Set<SignType> signTypes, String operator) {
		return this.listExplorations(null,null, user, initialDate, finalDate, signTypes, operator).size();
	}

	@Override
	public Exploration create(Exploration exploration) {
		return this.dh.persist(exploration);
	}

	@Override
	public Exploration edit(Exploration exploration) {
		return this.dh.update(exploration);
	}

	@Override
	public void delete(Exploration exploration) {
		exploration.setDeleted(true);
		this.dh.update(exploration);
	}

	@Override
	public void recover(Exploration exploration) {
		exploration.setDeleted(false);
		this.dh.update(exploration);
	}

	private List listExplorations(Integer page, Integer pageSize, User user, Date initialDate, Date finalDate,
								  Set<SignType> signTypes, String operator) {

		String roleLogged = userDao.get(context.getCallerPrincipal().getName()).getRole().getName();

		String queryInDateRange = "";
		if (initialDate != null && finalDate != null) {
			queryInDateRange = "e.date >= :initialDate AND e.date <= :finalDate ";
		} else {
			if (initialDate != null) {
				  queryInDateRange = "e.date >= :initialDate ";
			} else {
				if (finalDate != null) {
					queryInDateRange = "e.date >= :finalDate ";
				}
			}
		}

		Query query;
		String queryExplorations, conditionDeleted;

		if (signTypes.size() > 0) {
			StringBuilder stringBuilder = new StringBuilder("");
		 	String querySignType = "SELECT tt.code " +
				"FROM Exploration ee LEFT JOIN ee.radiographs rr LEFT JOIN rr.signs ss LEFT JOIN ss.type tt " +
				"WHERE ee.id=e.id";

		 	if (user == null && (roleLogged != "ADMIN" || roleLogged != "SUPERVISOR")) {
		 		querySignType += " AND ee.deleted=0";
			}

			stringBuilder.append("(");
			for (int count = 0; count < signTypes.size(); count++) {
				stringBuilder.append("?")
					.append(count)
					.append(" IN (")
					.append(querySignType)
					.append(")");
				if (count + 1 < signTypes.size()){
					stringBuilder.append(" " + operator + " ");
				}
		  	}
			stringBuilder.append(") ");

			if (user != null && (roleLogged != "ADMIN" || roleLogged != "SUPERVISOR")) {
				if (queryInDateRange != "") {
					queryInDateRange += "AND ";
				}

				queryExplorations = "SELECT e " +
					"FROM Exploration e LEFT JOIN e.radiographs r LEFT JOIN r.signs s LEFT JOIN s.type t " +
					"WHERE e.user.login=:login AND e.deleted=0 AND " + queryInDateRange + stringBuilder +
					"GROUP BY e.id ORDER BY e.date DESC";

				query = this.em.createQuery(queryExplorations, Exploration.class);
				query.setParameter("login", user.getLogin());
			} else {
				if (user != null && (roleLogged != "ADMIN" || roleLogged != "SUPERVISOR")) {
					conditionDeleted = "";
				} else {
					conditionDeleted = "AND e.deleted=0 ";
				}
				if (queryInDateRange != "") {
					stringBuilder.insert(0, "AND ");
				}

				queryExplorations = "SELECT e " +
					"FROM Exploration e LEFT JOIN e.radiographs r LEFT JOIN r.signs s LEFT JOIN s.type t " +
					"WHERE " + queryInDateRange  + stringBuilder + conditionDeleted +
					"GROUP BY e.id ORDER BY e.date DESC";

				query = this.em.createQuery(queryExplorations, Exploration.class);
			}

			int count = 0;
			for (SignType signType : signTypes) {
				query.setParameter(count, signType.getCode());
				count++;
			}

			if (page != null && pageSize != null) {
				query = query.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize);
			}
		} else if (user != null) {
			if (queryInDateRange != "") {
				queryInDateRange= "AND " + queryInDateRange;
			}

			if (!roleLogged.equals("ADMIN") && !roleLogged.equals("SUPERVISOR")) {
				conditionDeleted = " AND e.deleted=0 ";
			} else {
				conditionDeleted = "";
			}

			queryExplorations = "SELECT e " +
				"FROM Exploration e " +
				"WHERE e.user.login=:login " + queryInDateRange + conditionDeleted +
				"ORDER BY e.date DESC";

		 	query = this.em.createQuery(queryExplorations, Exploration.class);
		 	query.setParameter("login", user.getLogin());

			if (initialDate != null){
				query.setParameter("initialDate", initialDate);
			}
			if (finalDate != null){
				query.setParameter("finalDate", finalDate);
			}

			if (page != null && pageSize != null) {
				query = query.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize);
			}
		} else {
		 	if (queryInDateRange != "") {
				if (!roleLogged.equals("ADMIN") && !roleLogged.equals("SUPERVISOR")) {
					conditionDeleted = " AND e.deleted=0 ";
				} else {
					conditionDeleted = "";
				}
				queryExplorations = "SELECT e " +
					"FROM Exploration e " +
					"WHERE " + queryInDateRange + conditionDeleted +
					"ORDER BY e.date DESC";
			} else {
		 		if(!roleLogged.equals("ADMIN") && !roleLogged.equals("SUPERVISOR")) {
					queryExplorations = "SELECT e " +
						"FROM Exploration e " +
						"WHERE e.deleted=0 " +
						"ORDER BY e.date DESC";
				} else {
					queryExplorations = "SELECT e " +
						"FROM Exploration e " +
						"ORDER BY e.date DESC";
				}
			}

		 	query = this.em.createQuery(queryExplorations, Exploration.class);

			if (page != null && pageSize != null) {
				query = query.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize);
			}
		}

		if (initialDate != null){
			query.setParameter("initialDate", initialDate);
		}
		if (finalDate != null){
			query.setParameter("finalDate", finalDate);
		}

		return query.getResultList();
	}
}
