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
package org.sing_group.rihana.service.radiograph;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJBAccessException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.sing_group.rihana.domain.dao.spi.radiograph.RadiographDAO;
import org.sing_group.rihana.domain.entities.radiograph.Radiograph;
import org.sing_group.rihana.service.spi.acl.permission.PermissionService;
import org.sing_group.rihana.service.spi.exploration.ExplorationStorage;
import org.sing_group.rihana.service.spi.radiograph.RadiographService;

@Stateless
@PermitAll
public class DefaultRadiographService implements RadiographService {

	@Inject
	private RadiographDAO radiographDAO;

	@Inject
	private ExplorationStorage explorationStorage;

	@Inject
	private PermissionService permissionService;

	@Resource
	private SessionContext context;

	@Override
	public Radiograph getRadiograph(String id) {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"RADIOGRAPH_MANAGEMENT",
				"SHOW_CURRENT") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		return radiographDAO.get(id);
	}

	@Override
	public Radiograph create(Radiograph radiograph, String source) {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"RADIOGRAPH_MANAGEMENT",
				"ADD") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		InputStream data = sourceToInputStream(radiograph, source);

		String filePath = explorationStorage.storeRadiograph(radiograph, data);
		radiograph.setSource(filePath);

		return radiographDAO.create(radiograph);
	}

	@Override
	public void delete(Radiograph radiograph) {

		String loginLogged = context.getCallerPrincipal().getName();
		if (!this.permissionService.hasPermission(
				loginLogged,
				"RADIOGRAPH_MANAGEMENT",
				"DELETE") &&
			!this.permissionService.isAdmin(loginLogged)
		) {
			throw new EJBAccessException("Insufficient privileges");
		}

		radiographDAO.delete(radiograph);
	}

	private InputStream sourceToInputStream(Radiograph radiograph, String source) {

		String b64Data = source.split(",")[1];

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
