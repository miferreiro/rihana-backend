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
package org.sing_group.rihana.rest.resource.acl.functionality;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.sing_group.rihana.domain.entities.acl.functionality.Functionality;
import org.sing_group.rihana.rest.entity.acl.functionality.FunctionalityData;
import org.sing_group.rihana.rest.entity.acl.functionality.FunctionalityEditionData;
import org.sing_group.rihana.rest.entity.mapper.spi.FunctionalityMapper;
import org.sing_group.rihana.rest.filter.CrossDomain;
import org.sing_group.rihana.rest.resource.spi.acl.functionality.FunctionalityResource;
import org.sing_group.rihana.service.spi.acl.functionality.FunctionalityService;

@Path("functionality")
@Produces({
	APPLICATION_JSON + ";charset=utf-8", APPLICATION_XML + ";charset=utf-8"
})
@Consumes({
	APPLICATION_JSON + ";charset=utf-8", APPLICATION_XML + ";charset=utf-8"
})
@Api(value = "functionality")
@Stateless
@Default
@CrossDomain(allowedHeaders = "charset")
public class DefaultFunctionalityResource implements FunctionalityResource {

	@Inject
	private FunctionalityService service;

	@Inject
	private FunctionalityMapper functionalityMapper;

	@Context
	private UriInfo uriInfo;

	public DefaultFunctionalityResource() {}

	@PostConstruct
	public void init() {
		this.functionalityMapper.setRequestURI(this.uriInfo);
	}

	@POST
	@ApiOperation(
		value = "Creates a new functionality.", response = FunctionalityData.class, code = 201
	)
	@Override
	public Response create(FunctionalityEditionData functionalityEditionData) {
		Functionality functionality =
			this.service.create(
				new Functionality(
					functionalityEditionData.getName(), functionalityEditionData.getDescription()
				)
			);
		return Response.created(UriBuilder.fromResource(DefaultFunctionalityResource.class).path(Integer.toString(functionality.getId())).build())
			.entity(functionalityMapper.toFunctionalityData(functionality)).build();
	}

	@GET
	@Path("{id}")
	@ApiOperation(
		value = "Return the data of a functionality.", response = FunctionalityData.class, code = 200
	)
	@Override
	public Response getFunctionality(@PathParam("id") int id) {
		return Response.ok(this.functionalityMapper.toFunctionalityData(this.service.get(id))).build();
	}

	@GET
	@ApiOperation(
		value = "Return the data of all functionalities.", response = FunctionalityData.class, responseContainer = "List", code = 200
	)
	@Override
	public Response listFunctionalities() {
		return Response.ok(this.service.getFunctionalities().map(this.functionalityMapper::toFunctionalityData).toArray(FunctionalityData[]::new))
			.build();
	}

	@PUT
	@Path("{id}")
	@ApiOperation(
		value = "Modifies an existing functionality.", response = FunctionalityData.class, code = 200
	)
	@Override
	public Response edit(@PathParam("id") int id, FunctionalityEditionData functionalityEditionData) {
		Functionality functionality = this.service.get(id);
		this.functionalityMapper.assignFunctionalityEditionData(functionality, functionalityEditionData);
		return Response.ok(this.functionalityMapper.toFunctionalityData(this.service.edit(functionality))).build();
	}

	@DELETE
	@Path("{id}")
	@ApiOperation(
		value = "Deletes an existing functionality.", code = 200
	)
	@ApiResponses(
		@ApiResponse(code = 400, message = "Unknown functionality: {id}")
	)
	@Override
	public Response delete(@PathParam("id") int id) {
		Functionality functionality = this.service.get(id);
		this.service.delete(functionality);
		return Response.ok().build();
	}
}
