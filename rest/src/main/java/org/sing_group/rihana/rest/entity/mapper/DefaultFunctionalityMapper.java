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

import javax.enterprise.inject.Default;
import javax.ws.rs.core.UriInfo;

import org.sing_group.rihana.domain.entities.acl.functionality.Functionality;
import org.sing_group.rihana.rest.entity.acl.functionality.FunctionalityData;
import org.sing_group.rihana.rest.entity.acl.functionality.FunctionalityEditionData;
import org.sing_group.rihana.rest.entity.mapper.spi.FunctionalityMapper;

@Default
public class DefaultFunctionalityMapper implements FunctionalityMapper {

	private UriInfo requestURI;

	@Override
	public void setRequestURI(UriInfo requestURI) {
		this.requestURI = requestURI;
	}

	@Override
	public FunctionalityData toFunctionalityData(Functionality functionality) {
		return new FunctionalityData(
			functionality.getId(), functionality.getName(), functionality.getDescription()
		);
	}

	@Override
	public void assignFunctionalityEditionData(Functionality functionality, FunctionalityEditionData functionalityEditionData) {
		functionality.setDescription(functionalityEditionData.getDescription());
	}
}
