/*-
 * #%L
 * Service
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
package org.sing_group.rihana.service.acl.permission;

import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJBAccessException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.rihana.domain.dao.spi.acl.action.ActionDAO;
import org.sing_group.rihana.domain.dao.spi.acl.functionality.FunctionalityDAO;
import org.sing_group.rihana.domain.dao.spi.acl.permission.PermissionDAO;
import org.sing_group.rihana.domain.dao.spi.user.UserDAO;
import org.sing_group.rihana.domain.entities.acl.action.Action;
import org.sing_group.rihana.domain.entities.acl.functionality.Functionality;
import org.sing_group.rihana.domain.entities.acl.permission.FunctionalityActionKey;
import org.sing_group.rihana.domain.entities.acl.permission.Permission;
import org.sing_group.rihana.domain.entities.acl.permission.PermissionKey;
import org.sing_group.rihana.domain.entities.acl.role.Role;
import org.sing_group.rihana.service.spi.acl.permission.PermissionService;

@Stateless
@PermitAll
public class DefaultPermissionService implements PermissionService {

	@Inject
	private PermissionDAO permissionDAO;

	@Inject
	private UserDAO userDAO;

	@Inject
	private FunctionalityDAO functionalityDAO;

	@Inject
	private ActionDAO actionDAO;

	@Resource
	private SessionContext context;

	@Override
	public Permission create(Permission permission) {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.hasPermission(
				loginLogged,
				"PERMISSION_MANAGEMENT",
				"ADD") &&
			!this.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		return permissionDAO.create(permission);
	}

	@Override
	public Stream<Permission> getPermissions() {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.hasPermission(
				loginLogged,
				"PERMISSION_MANAGEMENT",
				"SHOW_ALL") &&
			!this.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		return permissionDAO.getPermissions();
	}

	@Override
	public Permission get(PermissionKey id) {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.hasPermission(
				loginLogged,
				"PERMISSION_MANAGEMENT",
				"SHOW_CURRENT") &&
			!this.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		return permissionDAO.get(id);
	}

	@Override
	public void delete(Permission permission) {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.hasPermission(
				loginLogged,
				"PERMISSION_MANAGEMENT",
				"DELETE") &&
			!this.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		permissionDAO.delete(permission);
	}

	@Override
	public boolean hasPermission(String login, String functionalityName, String actionName) {

		Role role = userDAO.get(login).getRole();

		Functionality functionality = functionalityDAO.getByName(functionalityName);

		Action action = actionDAO.getByName(actionName);

		PermissionKey permissionKey = new PermissionKey(role.getId(),
			new FunctionalityActionKey(functionality.getId(), action.getId()));

		return permissionDAO.hasPermission(permissionKey) ;
	}

	@Override
	public boolean isAdmin(String login) {
		return permissionDAO.isAdmin(login);
	}

	@Override
	public boolean isSupervisor(String login) {
		return permissionDAO.isSupervisor(login);
	}
}
