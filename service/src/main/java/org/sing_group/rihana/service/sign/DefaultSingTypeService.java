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
package org.sing_group.rihana.service.sign;

import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJBAccessException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.rihana.domain.dao.spi.sign.SignTypeDAO;
import org.sing_group.rihana.domain.entities.sign.SignType;
import org.sing_group.rihana.service.spi.acl.permission.PermissionService;
import org.sing_group.rihana.service.spi.sign.SignTypeService;

@Stateless
@PermitAll
public class DefaultSingTypeService implements SignTypeService {

	@Inject
	private SignTypeDAO signTypeDAO;

	@Inject
	private PermissionService permissionService;

	@Resource
	private SessionContext context;

	@Override
	public SignType get(String code) {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"TYPE_SIGN_MANAGEMENT",
				"SHOW_CURRENT") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		return signTypeDAO.get(code);
	}

	@Override
	public SignType create(SignType signType) {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"TYPE_SIGN_MANAGEMENT",
				"ADD") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		return signTypeDAO.create(signType);
	}

	@Override
	public SignType edit(SignType signType) {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"TYPE_SIGN_MANAGEMENT",
				"EDIT") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		return signTypeDAO.edit(signType);
	}

	@Override
	public void delete(SignType signType) {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"TYPE_SIGN_MANAGEMENT",
				"DELETE") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		signTypeDAO.delete(signType);
	}

	@Override
	public Stream<SignType> listSignTypes() {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"TYPE_SIGN_MANAGEMENT",
				"SHOW_ALL") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		return signTypeDAO.listSignTypes();
	}
}
