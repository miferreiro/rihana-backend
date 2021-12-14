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
package org.sing_group.rihana.domain.dao.acl.role;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.sing_group.rihana.domain.dao.DAOHelper;
import org.sing_group.rihana.domain.dao.spi.acl.role.RoleDAO;
import org.sing_group.rihana.domain.entities.acl.role.Role;

@Default
@Transactional(value = Transactional.TxType.MANDATORY)
public class DefaultRoleDAO implements RoleDAO {

	@PersistenceContext
	protected EntityManager em;

	protected DAOHelper<Integer, Role> dh;

	public DefaultRoleDAO() {
		super();
	}

	public DefaultRoleDAO(EntityManager em) {
		this.em = em;
		createDAOHelper();
	}

	@PostConstruct
	protected void createDAOHelper() {
		this.dh = DAOHelper.of(Integer.class, Role.class, this.em);
	}

	@Override
	public Role create(Role role) {
		return this.dh.persist(role);
	}

	@Override
	public Stream<Role> getRoles() {
		return this.dh.listBy("deleted", 0).stream();
	}

	@Override
	public Role get(int id) {
		return this.dh.get(id).filter(e -> !e.isDeleted())
			.orElseThrow(() -> new IllegalArgumentException("Unknown role: " + id));
	}

	@Override
	public Role getByName(String name) {
		return this.em
			.createQuery("SELECT r FROM Role r WHERE r.name=:name AND r.deleted=0", Role.class)
			.setParameter("name", name).getSingleResult();
	}

	@Override
	public Role edit(Role role) {
		return this.dh.update(role);
	}

	@Override
	public void delete(Role role) {
		role.setDeleted(true);
		this.dh.update(role);
	}
}
