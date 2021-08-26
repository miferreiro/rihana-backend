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

import org.sing_group.rihana.domain.entities.report.PerformedExploration;
import org.sing_group.rihana.domain.entities.report.Report;
import org.sing_group.rihana.domain.entities.report.RequestedExploration;
import org.sing_group.rihana.rest.entity.mapper.spi.PerformedExplorationMapper;
import org.sing_group.rihana.rest.entity.mapper.spi.ReportMapper;
import org.sing_group.rihana.rest.entity.mapper.spi.RequestedExplorationMapper;
import org.sing_group.rihana.rest.entity.report.PerformedExplorationData;
import org.sing_group.rihana.rest.entity.report.ReportData;
import org.sing_group.rihana.rest.entity.report.RequestedExplorationData;
import org.sing_group.rihana.rest.resource.exploration.DefaultExplorationResource;

@Default
public class DefaultReportMapper implements ReportMapper {

	private UriInfo requestURI;

	@Inject
	private RequestedExplorationMapper requestedExplorationMapper;

	@Inject
	private PerformedExplorationMapper performedExplorationMapper;

	@Override
	public void setRequestURI(UriInfo requestURI) {
		this.requestURI = requestURI;
	}

	@Override
	public ReportData toReportData(Report report) {

		return new ReportData(
			report.getId(), report.getReportN(), report.getCompletionDate(), report.getApplicant(),
			report.getPriority(), report.getStatus(), report.getBed(), report.getClinicalData(), report.getFindings(),
			report.getConclusions(),
			fromEntity(requestURI, report.getExploration(), DefaultExplorationResource.class),
			toListRequestedExplorationData(report.getRequestedExplorations()),
			toListPerformedExplorationData(report.getPerformedExplorations())
		);
	}

	private List<RequestedExplorationData> toListRequestedExplorationData(List<RequestedExploration> requestedExplorations) {
		List<RequestedExplorationData> requestedExplorationDataList = new ArrayList<>();

		for (RequestedExploration requestedExploration : requestedExplorations) {
			requestedExplorationDataList.add(requestedExplorationMapper.toRequestedExplorationData((requestedExploration)));
		}

		return requestedExplorationDataList;
	}

	private List<PerformedExplorationData> toListPerformedExplorationData(List<PerformedExploration> performedExplorations) {
		List<PerformedExplorationData> performedExplorationDataList = new ArrayList<>();

		for (PerformedExploration performedExploration : performedExplorations) {
			performedExplorationDataList.add(performedExplorationMapper.toPerformedExplorationData((performedExploration)));
		}

		return performedExplorationDataList;
	}
}
