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
package org.sing_group.rihana.domain.dao.acl.permission;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.sing_group.rihana.domain.dao.DAOHelper;
import org.sing_group.rihana.domain.dao.spi.acl.permission.PermissionDAO;
import org.sing_group.rihana.domain.entities.acl.permission.Permission;
import org.sing_group.rihana.domain.entities.acl.permission.PermissionKey;
import org.sing_group.rihana.domain.entities.user.User;

@Default
@Transactional(value = Transactional.TxType.MANDATORY)
public class DefaultPermissionDAO implements PermissionDAO {

	@PersistenceContext
	protected EntityManager em;

	protected DAOHelper<PermissionKey, Permission> dh;

	public DefaultPermissionDAO() {
		super();
	}

	public DefaultPermissionDAO(EntityManager em) {
		this.em = em;
		createDAOHelper();
	}

	@PostConstruct
	protected void createDAOHelper() {
		this.dh = DAOHelper.of(PermissionKey.class, Permission.class, this.em);
	}

	@Override
	public Permission create(Permission permission) {
		return this.dh.persist(permission);
	}

	@Override
	public Stream<Permission> getPermissions() {
		return this.dh.listBy("deleted", 0).stream();
	}

	@Override
	public Permission get(PermissionKey id) {
		return this.dh.get(id).filter(e -> !e.isDeleted())
			.orElseThrow(() -> new IllegalArgumentException("Unknown permission: " + id));
	}

	@Override
	public void delete(Permission permission) {
		permission.setDeleted(true);
		this.dh.update(permission);
	}

	@Override
	public boolean hasPermission(PermissionKey permissionKey) {
		String queryPermissions = "SELECT p " +
			"FROM Permission p " +
			"WHERE p.id.roleId=:roleId AND " +
			"p.id.functionalityActionId.functionalityId=:functionalityId AND " +
			"p.id.functionalityActionId.actionId=:actionId";

		Query query = this.em.createQuery(queryPermissions, Permission.class);
		query.setParameter("roleId", permissionKey.getRoleId());
		query.setParameter("functionalityId", permissionKey.getFunctionalityActionId().getFunctionalityId());
		query.setParameter("actionId", permissionKey.getFunctionalityActionId().getActionId());

		return query.getResultList().size() > 0;
	}

	@Override
	public boolean isAdmin(String login) {

		String queryAdmin = "SELECT u " +
			"FROM User u " +
			"WHERE u.role.id = 1 AND " +
			"u.login=:login";

		Query query = this.em.createQuery(queryAdmin, User.class);
		query.setParameter("login", login);

		return query.getResultList().size() > 0;
	}

	@Override
	public boolean isSupervisor(String login) {

		String queryAdmin = "SELECT u " +
			"FROM User u " +
			"WHERE u.role.id = 2 AND " +
			"u.login=:login";

		Query query = this.em.createQuery(queryAdmin, User.class);
		query.setParameter("login", login);

		return query.getResultList().size() > 0;
	}
}
