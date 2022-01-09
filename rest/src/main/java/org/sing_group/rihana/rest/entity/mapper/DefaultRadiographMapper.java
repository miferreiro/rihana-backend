/*-
 * #%L
 * REST
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
package org.sing_group.rihana.rest.entity.mapper;

import static org.sing_group.rihana.rest.entity.UuidAndUri.fromEntity;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import org.sing_group.rihana.domain.entities.radiograph.Radiograph;
import org.sing_group.rihana.domain.entities.sign.Sign;
import org.sing_group.rihana.rest.entity.mapper.spi.RadiographMapper;
import org.sing_group.rihana.rest.entity.mapper.spi.SignLocationMapper;
import org.sing_group.rihana.rest.entity.mapper.spi.SignTypeMapper;
import org.sing_group.rihana.rest.entity.radiograph.RadiographData;
import org.sing_group.rihana.rest.entity.sign.SignData;
import org.sing_group.rihana.rest.resource.exploration.DefaultExplorationResource;

@Default
public class DefaultRadiographMapper implements RadiographMapper {

	private UriInfo requestURI;

	@Inject
	private SignTypeMapper signTypeMapper;

	@Inject
	private SignLocationMapper signLocationMapper;

	@Override
	public void setRequestURI(UriInfo requestURI) {
		this.requestURI = requestURI;
	}

	@Override
	public RadiographData toRadiographData(Radiograph radiograph) {
		return new RadiographData(
			radiograph.getId(), radiograph.getSource(), radiograph.getType(), radiograph.getObservations(),
			fromEntity(requestURI, radiograph.getExploration(), DefaultExplorationResource.class),
			toSetSignData(radiograph.getSigns())
		);
	}

	private Set<SignData> toSetSignData(Set<Sign> signs) {
		Set<SignData> signDataSet = new HashSet<>();

		for (Sign sign : signs) {
			signDataSet.add(new SignData(signTypeMapper.toSignTypeData(sign.getType()),
				signLocationMapper.toSignLocationData(sign.getSignLocation()),
				sign.getBrightness(),
				sign.getContrast())
			);
		}

		return signDataSet;
	}
}
