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

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import org.sing_group.rihana.domain.entities.radiography.Radiography;
import org.sing_group.rihana.domain.entities.sign.Sign;
import org.sing_group.rihana.rest.entity.mapper.spi.RadiographyMapper;
import org.sing_group.rihana.rest.entity.mapper.spi.SignLocationMapper;
import org.sing_group.rihana.rest.entity.mapper.spi.SignTypeMapper;
import org.sing_group.rihana.rest.entity.radiography.RadiographyData;
import org.sing_group.rihana.rest.entity.sign.SignData;
import org.sing_group.rihana.rest.resource.exploration.DefaultExplorationResource;

@Default
public class DefaultRadiographyMapper implements RadiographyMapper {

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
	public RadiographyData toRadiographyData(Radiography radiography) {
		return new RadiographyData(
			radiography.getId(), radiography.getSource(), radiography.getType(),
			fromEntity(requestURI, radiography.getExploration(), DefaultExplorationResource.class),
			toListSignData(radiography.getSigns())
		);
	}

	private List<SignData> toListSignData(List<Sign> signs) {
		List<SignData> signDataList = new ArrayList<>();

		for (Sign sign : signs) {
			signDataList.add(new SignData(sign.getId(),
				signTypeMapper.toSignTypeData(sign.getType()),
				signLocationMapper.toSignLocationData(sign.getSignLocation()))
			);
		}

		return signDataList;
	}
}
