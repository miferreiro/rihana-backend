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
package org.sing_group.rihana.domain.dao.acl.functionality;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.sing_group.rihana.domain.dao.DAOHelper;
import org.sing_group.rihana.domain.dao.spi.acl.functionality.FunctionalityDAO;
import org.sing_group.rihana.domain.entities.acl.functionality.Functionality;

@Default
@Transactional(value = Transactional.TxType.MANDATORY)
public class DefaultFunctionalityDAO implements FunctionalityDAO {

	@PersistenceContext
	protected EntityManager em;

	protected DAOHelper<Integer, Functionality> dh;

	public DefaultFunctionalityDAO() {
		super();
	}

	public DefaultFunctionalityDAO(EntityManager em) {
		this.em = em;
		createDAOHelper();
	}

	@PostConstruct
	protected void createDAOHelper() {
		this.dh = DAOHelper.of(Integer.class, Functionality.class, this.em);
	}

	@Override
	public Functionality create(Functionality functionality) {
		return this.dh.persist(functionality);
	}

	@Override
	public Stream<Functionality> getFunctionalities() {
		return this.dh.listBy("deleted", 0).stream();
	}

	@Override
	public Functionality get(int id) {
		return this.dh.get(id).filter(e -> !e.isDeleted())
			.orElseThrow(() -> new IllegalArgumentException("Unknown functionality: " + id));
	}

	@Override
	public Functionality getByName(String name) {
		return this.em
			.createQuery("SELECT f FROM Functionality f WHERE f.name=:name AND f.deleted=0", Functionality.class)
			.setParameter("name", name).getSingleResult();
	}

	@Override
	public Functionality edit(Functionality functionality) {
		return this.dh.update(functionality);
	}

	@Override
	public void delete(Functionality functionality) {
		functionality.setDeleted(true);
		this.dh.update(functionality);
	}
}
