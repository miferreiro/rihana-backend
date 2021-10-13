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

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.rihana.domain.dao.spi.radiograph.RadiographDAO;
import org.sing_group.rihana.domain.entities.radiograph.Radiograph;
import org.sing_group.rihana.service.spi.radiograph.RadiographService;

@Stateless
@PermitAll
public class DefaultRadiographService implements RadiographService {

	@Inject
	private RadiographDAO radiographDAO;

	@Override
	public Radiograph getRadiograph(String id) {
		return radiographDAO.get(id);
	}

	@Override
	public Radiograph create(Radiograph radiograph) {
		return radiographDAO.create(radiograph);
	}
}
