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

import static org.sing_group.rihana.rest.entity.UuidAndUri.fromEntities;
import static org.sing_group.rihana.rest.entity.UuidAndUri.fromEntity;

import java.util.ArrayList;

import javax.enterprise.inject.Default;
import javax.ws.rs.core.UriInfo;

import org.sing_group.rihana.domain.entities.exploration.Exploration;
import org.sing_group.rihana.rest.entity.UuidAndUri;
import org.sing_group.rihana.rest.entity.exploration.ExplorationAdminData;
import org.sing_group.rihana.rest.entity.exploration.ExplorationData;
import org.sing_group.rihana.rest.entity.mapper.spi.ExplorationMapper;
import org.sing_group.rihana.rest.resource.patient.DefaultPatientResource;
import org.sing_group.rihana.rest.resource.radiograph.DefaultRadiographResource;
import org.sing_group.rihana.rest.resource.report.DefaultReportResource;

@Default
public class DefaultExplorationMapper implements ExplorationMapper {

	private UriInfo requestURI;

	@Override
	public void setRequestURI(UriInfo requestURI) {
		this.requestURI = requestURI;
	}

	@Override
	public ExplorationData toExplorationData(Exploration exploration) {

		UuidAndUri report, patient;
		if (exploration.getCurrentReport() == null) {
			report = null;
		} else {
			report =fromEntity(requestURI, exploration.getCurrentReport(), DefaultReportResource.class);
		}

		if (exploration.getPatient() == null) {
			patient = null;
		} else {
			patient =fromEntity(requestURI, exploration.getPatient(), DefaultPatientResource.class);
		}

		return new ExplorationData(
			exploration.getId(), exploration.getTitle(), exploration.getDate(), exploration.getSource(), exploration.getUser(),
			patient, report,
			fromEntities(requestURI, new ArrayList<>(exploration.getCurrentRadiographs()), DefaultRadiographResource.class)
		);
	}
	@Override
	public ExplorationAdminData toExplorationAdminData(Exploration exploration) {

		UuidAndUri report, patient;
		if (exploration.getCurrentReport() == null) {
			report = null;
		} else {
			report =fromEntity(requestURI, exploration.getCurrentReport(), DefaultReportResource.class);
		}

		if (exploration.getPatient() == null) {
			patient = null;
		} else {
			patient =fromEntity(requestURI, exploration.getPatient(), DefaultPatientResource.class);
		}

		return new ExplorationAdminData(
			exploration.getId(), exploration.getTitle(), exploration.getDate(), exploration.getSource(), exploration.getUser(),
			patient, report,
			fromEntities(requestURI, new ArrayList<>(exploration.getCurrentRadiographs()), DefaultRadiographResource.class),
			exploration.isDeleted()
		);
	}
}
