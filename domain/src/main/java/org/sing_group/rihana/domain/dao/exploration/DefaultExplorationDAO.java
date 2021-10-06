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

import java.util.List;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.sing_group.rihana.domain.dao.DAOHelper;
import org.sing_group.rihana.domain.dao.ListingOptions;
import org.sing_group.rihana.domain.dao.spi.exploration.ExplorationDAO;
import org.sing_group.rihana.domain.entities.exploration.Exploration;
import org.sing_group.rihana.domain.entities.sign.SignType;
import org.sing_group.rihana.domain.entities.user.User;

@Default
@Transactional(value = Transactional.TxType.MANDATORY)
public class DefaultExplorationDAO implements ExplorationDAO {

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
	public Stream<Exploration> listExplorationsByUser(Integer page, Integer pageSize, User user, List<SignType> signTypeList) {
		return this.listExplorations(page, pageSize, user, signTypeList).stream();
	}

	@Override
	public int countExplorations() {
		return dh.listBy("deleted", 0).size();
	}

	@Override
	public int countExplorationsByUser(User user) {
		String queryString = "SELECT e FROM Exploration e LEFT JOIN e.user u WHERE e.deleted=0 AND u.login=:login";
		return em.createQuery(queryString).setParameter("login", user.getLogin()).getResultList().size();
	}

	@Override
	public int countExplorationsByUserAndSignTypes(User user, List<SignType> signTypeList) {
		return this.listExplorations(null,null, user, signTypeList).size();
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

	private List<Exploration> listExplorations(Integer page, Integer pageSize, User user, List<SignType> signTypeList) {

		// Checks if a user is sent
		if (signTypeList.size() > 0) {

			StringBuilder stringBuilder = new StringBuilder("");
			int count = 0;
			String querySignType = "SELECT tt.code from Exploration ee LEFT JOIN ee.radiographs rr LEFT JOIN rr.signs ss LEFT JOIN ss.type tt WHERE ee.id=e.id AND ee.deleted=0";

			if (signTypeList.size() > 0) {
				stringBuilder.append(" AND (");
			}

			for (SignType signType: signTypeList) {
				stringBuilder.append("?").append(count).append(" IN (").append(querySignType).append(")");

				count++;
				if (count < signTypeList.size()) {
					stringBuilder.append(" AND ");
				}
			}

			if (signTypeList.size() > 0) {
				stringBuilder.append(")");
			}

			String queryExplorations;
			Query query;

			if (user != null) {
				queryExplorations = "SELECT e " +
					"FROM Exploration e LEFT JOIN e.radiographs r LEFT JOIN r.signs s LEFT JOIN s.type t " +
					"WHERE e.user.login=:login AND e.deleted=0" +
					stringBuilder +
					" GROUP BY e.id ORDER BY e.creationDate";

				query = this.em.createQuery(queryExplorations, Exploration.class);
				query.setParameter("login", user.getLogin());
			} else {
				queryExplorations = "SELECT e " +
					"FROM Exploration e LEFT JOIN e.radiographs r LEFT JOIN r.signs s LEFT JOIN s.type t " +
					"WHERE e.deleted=0 " +
					stringBuilder +
					" GROUP BY e.id ORDER BY e.creationDate";

				query = this.em.createQuery(queryExplorations, Exploration.class);
			}

			count = 0;
			for (SignType signType: signTypeList) {
				query.setParameter(count, signType.getCode());
				count++;
			}

			if (page != null && pageSize != null) {
				return query.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize).getResultList();
			} else {
				return query.getResultList();
			}
		} else {

			if (user != null) {
				String queryString = "SELECT e FROM Exploration e LEFT JOIN e.user u WHERE e.deleted=0 AND u.login=:login ORDER BY e.creationDate DESC";
				if (page != null && pageSize != null) {
					return em.createQuery(queryString).setParameter("login", user.getLogin()).setFirstResult((page - 1) * pageSize).setMaxResults(pageSize).getResultList();
				} else {
					return em.createQuery(queryString).setParameter("login", user.getLogin()).getResultList();
				}

			} else {
				String queryString = "SELECT e FROM Exploration e WHERE e.deleted=0 ORDER BY e.creationDate DESC";
				if (page != null && pageSize != null) {
					return em.createQuery(queryString).setFirstResult((page - 1) * pageSize).setMaxResults(pageSize).getResultList();
				} else {
					return em.createQuery(queryString).getResultList();
				}
			}
		}
	}
}
