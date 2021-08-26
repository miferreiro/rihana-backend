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

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.sing_group.rihana.domain.dao.DAOHelper;
import org.sing_group.rihana.domain.dao.ListingOptions;
import org.sing_group.rihana.domain.dao.spi.exploration.ExplorationDAO;
import org.sing_group.rihana.domain.entities.exploration.Exploration;
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
	public Stream<Exploration> listExplorationsByUser(int page, int pageSize, User user) {
		final ListingOptions listingOptions = ListingOptions.forPage(page, pageSize)
			.sortedBy(ListingOptions.SortField.descending("creationDate"));

		// Checks if a patient is sent
		if (user != null) {
			return dh.listBy("user", user, listingOptions).stream();
		} else {
			return dh.list(listingOptions).stream();
		}
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
}
