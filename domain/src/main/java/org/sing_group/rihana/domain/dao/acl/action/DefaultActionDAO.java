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
package org.sing_group.rihana.domain.dao.acl.action;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.sing_group.rihana.domain.dao.DAOHelper;
import org.sing_group.rihana.domain.dao.spi.acl.action.ActionDAO;
import org.sing_group.rihana.domain.entities.acl.action.Action;

@Default
@Transactional(value = Transactional.TxType.MANDATORY)
public class DefaultActionDAO implements ActionDAO {

	@PersistenceContext
	protected EntityManager em;

	protected DAOHelper<Integer, Action> dh;

	public DefaultActionDAO() {
		super();
	}

	public DefaultActionDAO(EntityManager em) {
		this.em = em;
		createDAOHelper();
	}

	@PostConstruct
	protected void createDAOHelper() {
		this.dh = DAOHelper.of(Integer.class, Action.class, this.em);
	}

	@Override
	public Action create(Action action) {
		return this.dh.persist(action);
	}

	@Override
	public Stream<Action> getActions() {
		return this.dh.listBy("deleted", 0).stream();
	}

	@Override
	public Action get(int id) {
		return this.dh.get(id).filter(e -> !e.isDeleted())
			.orElseThrow(() -> new IllegalArgumentException("Unknown action: " + id));
	}

	@Override
	public Action getByName(String name) {
		return this.em
			.createQuery("SELECT a FROM Action a WHERE a.name=:name AND a.deleted=0", Action.class)
			.setParameter("name", name).getSingleResult();
	}

	@Override
	public Action edit(Action action) {
		return this.dh.update(action);
	}

	@Override
	public void delete(Action action) {
		action.setDeleted(true);
		this.dh.update(action);
	}
}
