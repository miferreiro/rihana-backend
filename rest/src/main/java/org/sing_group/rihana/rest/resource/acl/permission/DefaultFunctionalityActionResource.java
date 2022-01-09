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
package org.sing_group.rihana.rest.resource.acl.permission;

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
import org.sing_group.rihana.domain.entities.acl.permission.FunctionalityAction;
import org.sing_group.rihana.domain.entities.acl.permission.FunctionalityActionKey;
import org.sing_group.rihana.rest.entity.acl.permission.FunctionalityActionData;
import org.sing_group.rihana.rest.entity.mapper.spi.FunctionalityActionMapper;
import org.sing_group.rihana.rest.filter.CrossDomain;
import org.sing_group.rihana.rest.resource.spi.acl.permission.FunctionalityActionResource;
import org.sing_group.rihana.service.spi.acl.permission.FunctionalityActionService;

@Path("functionalityaction")
@Produces({
	APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
	APPLICATION_JSON, APPLICATION_XML
})
@Api(value = "functionalityaction")
@Stateless
@Default
@CrossDomain
public class DefaultFunctionalityActionResource implements FunctionalityActionResource {

	@Inject
	private FunctionalityActionService service;

	@Inject
	private FunctionalityActionMapper functionalityActionMapper;

	@Context
	private UriInfo uriInfo;

	public DefaultFunctionalityActionResource() {}

	@PostConstruct
	public void init() {
		this.functionalityActionMapper.setRequestURI(this.uriInfo);
	}

	@POST
	@ApiOperation(
		value = "Creates a new functionality action.", response = FunctionalityActionData.class, code = 201
	)
	@Override
	public Response create(FunctionalityActionData functionalityActionData) {
		FunctionalityAction functionalityAction =
			this.service.create(
				new FunctionalityAction(
					new FunctionalityActionKey(
						functionalityActionData.getFunctionalityId(),
						functionalityActionData.getActionId()
					)
				)
			);
		return Response.created(
			UriBuilder.fromResource(DefaultFunctionalityActionResource.class)
				.path(functionalityAction.getFunctionalityActionKey().getFunctionalityId() +
					"/" +
					functionalityAction.getFunctionalityActionKey().getActionId()).build())
			.entity(this.functionalityActionMapper.toFunctionalityActionData(functionalityAction))
			.build();
	}

	@GET
	@Path("{functionalityId}/{actionId}")
	@ApiOperation(
		value = "Return the data of a functionality action.", response = FunctionalityActionData.class, code = 200
	)
	@Override
	public Response getFunctionalityAction(@PathParam("functionalityId") int functionalityId,
										   @PathParam("actionId") int actionId) {
		return Response.ok(this.functionalityActionMapper.toFunctionalityActionData(this.service.get(
			new FunctionalityActionKey(functionalityId, actionId)
		))).build();
	}

	@GET
	@ApiOperation(
		value = "Return the data of all functionality actions.", response = FunctionalityActionData.class,
		responseContainer = "List", code = 200
	)
	@Override
	public Response listFunctionalityActions() {
		return Response.ok(this.service.getFunctionalityActions()
			.map(this.functionalityActionMapper::toFunctionalityActionData)
			.toArray(FunctionalityActionData[]::new))
			.build();
	}

	@DELETE
	@Path("{functionalityId}/{actionId}")
	@ApiOperation(
		value = "Deletes an existing functionality action.", code = 200
	)
	@ApiResponses(
		@ApiResponse(code = 400, message = "Unknown functionality action: {id}")
	)
	@Override
	public Response delete(@PathParam("functionalityId") int functionalityId,
						   @PathParam("actionId") int actionId) {
		FunctionalityAction functionalityAction = this.service.get(
			new FunctionalityActionKey(functionalityId, actionId)
		);
		this.service.delete(functionalityAction);
		return Response.ok().build();
	}
}
