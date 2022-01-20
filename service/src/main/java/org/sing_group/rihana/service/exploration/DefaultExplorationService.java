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
package org.sing_group.rihana.service.exploration;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJBAccessException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.sing_group.rihana.domain.dao.spi.exploration.ExplorationDAO;
import org.sing_group.rihana.domain.entities.exploration.Exploration;
import org.sing_group.rihana.domain.entities.radiograph.Radiograph;
import org.sing_group.rihana.domain.entities.sign.SignType;
import org.sing_group.rihana.domain.entities.user.User;
import org.sing_group.rihana.service.spi.acl.permission.PermissionService;
import org.sing_group.rihana.service.spi.exploration.ExplorationService;
import org.sing_group.rihana.service.spi.exploration.ExplorationStorage;

@Stateless
@PermitAll
public class DefaultExplorationService implements ExplorationService {

	@Inject
	private ExplorationDAO explorationDao;

	@Inject
	private ExplorationStorage explorationStorage;

	@Inject
	private PermissionService permissionService;

	@Resource
	private SessionContext context;

	@Override
	public Exploration getExploration(String id) {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"EXPLORATION_MANAGEMENT",
				"SHOW_CURRENT") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		return explorationDao.getExploration(id);
	}

	@Override
	public Exploration getExplorationDeleted(String id) {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"EXPLORATION_MANAGEMENT",
				"RETRIEVE") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		return explorationDao.getExplorationDeleted(id);
	}

	@Override
	public Stream<Exploration> listExplorationsByUserInDateRange(int page, int pageSize, User user, Date initialDate,
																 Date finalDate, Set<SignType> signTypes, String operator) {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"EXPLORATION_MANAGEMENT",
				"SHOW_ALL") &&
			!this.permissionService.hasPermission(
				loginLogged,
				"EXPLORATION_MANAGEMENT",
				"SHOW_OWN") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		return explorationDao.listExplorationsByUserInDateRange(page, pageSize, user, initialDate, finalDate, signTypes,
			operator);
	}

	@Override
	public int countAllExplorations() {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"EXPLORATION_MANAGEMENT",
				"SHOW_ALL") &&
			!this.permissionService.hasPermission(
				loginLogged,
				"EXPLORATION_MANAGEMENT",
				"ADD") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		return explorationDao.countAllExplorations();
	}

	@Override
	public int countExplorationsByUserAndSignTypesInDateRange(User user, Date initialDate, Date finalDate,
															  Set<SignType> signTypes, String operator) {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"EXPLORATION_MANAGEMENT",
				"SHOW_ALL") &&
			!this.permissionService.hasPermission(
				loginLogged,
				"EXPLORATION_MANAGEMENT",
				"SHOW_OWN") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		return explorationDao.countExplorationsByUserAndSignTypesInDateRange(user, initialDate, finalDate, signTypes,
			operator);
	}

	@Override
	public Exploration create(Exploration exploration) {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"EXPLORATION_MANAGEMENT",
				"ADD") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		for (Radiograph radiograph: exploration.getCurrentRadiographs()) {
			InputStream data = sourceToInputStream(radiograph);

			String filePath = explorationStorage.storeRadiograph(radiograph, data);
			radiograph.setSource(filePath);
		}

		return explorationDao.create(exploration);
	}

	@Override
	public Exploration edit(Exploration exploration) {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"EXPLORATION_MANAGEMENT",
				"EDIT") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		return explorationDao.edit(exploration);
	}

	@Override
	public void delete(Exploration exploration) {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"EXPLORATION_MANAGEMENT",
				"DELETE") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		explorationDao.delete(exploration);
	}

	@Override
	public void recover(Exploration exploration) {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"EXPLORATION_MANAGEMENT",
				"RETRIEVE") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		explorationDao.recover(exploration);
	}

	private InputStream sourceToInputStream(Radiograph radiograph) {

		String b64Data = radiograph.getSource().split(",")[1];

		byte[] decodedString = new byte[0];
		try {
			decodedString = Base64.getDecoder().decode(b64Data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		FileInputStream fileInputStream = null;
		try {
			BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(decodedString));
			File imgOutFile = new File(radiograph.getType().name() + ".png");
			ImageIO.write(bufImg, "png", imgOutFile);
			fileInputStream = new FileInputStream(imgOutFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileInputStream;
	}
}
