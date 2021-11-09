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

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.sing_group.rihana.domain.dao.spi.radiograph.RadiographDAO;
import org.sing_group.rihana.domain.entities.radiograph.Radiograph;
import org.sing_group.rihana.service.spi.exploration.ExplorationStorage;
import org.sing_group.rihana.service.spi.radiograph.RadiographService;

@Stateless
@PermitAll
public class DefaultRadiographService implements RadiographService {

	@Inject
	private RadiographDAO radiographDAO;

	@Inject
	private ExplorationStorage explorationStorage;

	@Override
	public Radiograph getRadiograph(String id) {
		return radiographDAO.get(id);
	}

	@Override
	public Radiograph create(Radiograph radiograph) {

		InputStream data = sourceToInputStream(radiograph);

		String filePath = explorationStorage.storeRadiograph(radiograph, data);
		radiograph.setSource(filePath);

		return radiographDAO.create(radiograph);
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
