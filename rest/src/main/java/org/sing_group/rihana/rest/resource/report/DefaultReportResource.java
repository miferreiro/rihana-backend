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
package org.sing_group.rihana.rest.resource.report;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.sing_group.rihana.rest.entity.mapper.spi.ReportMapper;
import org.sing_group.rihana.rest.entity.report.ReportData;
import org.sing_group.rihana.rest.filter.CrossDomain;
import org.sing_group.rihana.rest.resource.spi.report.ReportResource;
import org.sing_group.rihana.service.spi.report.ReportService;

@Path("report")
@Produces({
	APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
	APPLICATION_JSON, APPLICATION_XML
})
@Api(value = "report")
@Stateless
@Default
@CrossDomain(allowedHeaders = "X-Pagination-Total-Items")
public class DefaultReportResource implements ReportResource {

	@Inject
	private ReportService service;

	@Inject
	private ReportMapper reportMapper;

	@Context
	private UriInfo uriInfo;

	@PostConstruct
	public void init() {
		this.reportMapper.setRequestURI(this.uriInfo);
	}

	@GET
	@Path("{id}")
	@ApiOperation(
		value = "Return the data of a report.", response = ReportData.class, code = 200
	)
	@ApiResponses(
		@ApiResponse(code = 400, message = "Unknown report: {id}")
	)
	@Override
	public Response getReport(
		@PathParam("id") String id
	) {
		return Response
			.ok(this.reportMapper.toReportData(this.service.getReport(id)))
			.build();
	}
}
