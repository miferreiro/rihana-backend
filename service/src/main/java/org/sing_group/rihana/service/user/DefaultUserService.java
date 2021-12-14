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
package org.sing_group.rihana.service.user;

import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJBAccessException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.rihana.domain.dao.spi.user.UserDAO;
import org.sing_group.rihana.domain.entities.user.User;
import org.sing_group.rihana.service.spi.acl.permission.PermissionService;
import org.sing_group.rihana.service.spi.user.UserService;

@Stateless
@PermitAll
public class DefaultUserService implements UserService {

	@Inject
	private UserDAO userDAO;

	@Inject
	private PermissionService permissionService;

	@Resource
	private SessionContext context;

	@Override
	public User getCurrentUser() {
		return userDAO.get(this.context.getCallerPrincipal().getName());
	}

	@Override
	public User get(String login) {
		String loginLogged = context.getCallerPrincipal().getName();

		if (!(this.permissionService.hasPermission(
				loginLogged,
				"USER_MANAGEMENT",
				"SHOW_CURRENT") &&
			loginLogged.equals(login)) &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		return userDAO.get(login);
	}

	@Override
	public User create(User user) {
		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"USER_MANAGEMENT",
				"ADD") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		return userDAO.create(user);
	}

	@Override
	public User edit(User user) {
		String loginLogged = context.getCallerPrincipal().getName();
		if (!(this.permissionService.hasPermission(
				loginLogged,
				"USER_MANAGEMENT",
				"EDIT") &&
			loginLogged.equals(user.getLogin())) &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		return userDAO.edit(user);
	}

	@Override
	public void delete(User user) {
		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"USER_MANAGEMENT",
				"DELETE") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		userDAO.delete(user);
	}

	@Override
	public Stream<User> getUsers() {
		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"USER_MANAGEMENT",
				"SHOW_ALL") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		return userDAO.getUsers();
	}
}
