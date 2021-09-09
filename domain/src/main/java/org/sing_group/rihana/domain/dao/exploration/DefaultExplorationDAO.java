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
		return this.dh.get(id)
			.orElseThrow(() -> new IllegalArgumentException("Unknown exploration: " + id));
	}

	@Override
	public Stream<Exploration> listExplorationsByUser(Integer page, Integer pageSize, User user, List<SignType> signTypeList) {
		return this.listExplorations(page, pageSize, user, signTypeList).stream();
	}

	@Override
	public int countExplorations() {
		return dh.list().size();
	}

	@Override
	public int countExplorationsByUser(User user) {
		return dh.listBy("user", user).size();
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
		this.dh.remove(exploration);
	}

	private List<Exploration> listExplorations(Integer page, Integer pageSize, User user, List<SignType> signTypeList) {

		// Checks if a user is sent
		if (signTypeList.size() > 0) {

			StringBuilder stringBuilder = new StringBuilder("");
			int count = 0;
			String querySignType = "select tt.code from Exploration ee LEFT JOIN ee.radiographs rr LEFT JOIN rr.signs ss LEFT JOIN ss.type tt WHERE ee.id=e.id";

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
					"WHERE e.user.login=:login" +
					stringBuilder +
					" GROUP BY e.id ORDER BY e.creationDate";

				query = this.em.createQuery(queryExplorations, Exploration.class);
				query.setParameter("login", user.getLogin());
			} else {
				queryExplorations = "SELECT e " +
					"FROM Exploration e LEFT JOIN e.radiographs r LEFT JOIN r.signs s LEFT JOIN s.type t " +
					"WHERE " +
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
				if (page != null && pageSize != null) {
					final ListingOptions listingOptions = ListingOptions.forPage(page, pageSize)
						.sortedBy(ListingOptions.SortField.descending("creationDate"));
					return dh.listBy("user", user, listingOptions);
				} else {
					return dh.listBy("user", user);
				}

			} else {
				if (page != null && pageSize != null) {
					final ListingOptions listingOptions = ListingOptions.forPage(page, pageSize)
						.sortedBy(ListingOptions.SortField.descending("creationDate"));
					return dh.list(listingOptions);
				} else {
					return dh.list();
				}
			}
		}
	}
}
