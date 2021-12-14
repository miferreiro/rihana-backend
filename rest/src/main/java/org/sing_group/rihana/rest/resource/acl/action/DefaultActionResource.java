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
package org.sing_group.rihana.rest.resource.acl.action;

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
import org.sing_group.rihana.domain.entities.acl.action.Action;
import org.sing_group.rihana.rest.entity.acl.action.ActionData;
import org.sing_group.rihana.rest.entity.acl.action.ActionEditionData;
import org.sing_group.rihana.rest.entity.mapper.spi.ActionMapper;
import org.sing_group.rihana.rest.filter.CrossDomain;
import org.sing_group.rihana.rest.resource.spi.acl.action.ActionResource;
import org.sing_group.rihana.service.spi.acl.action.ActionService;

@Path("action")
@Produces({
	APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
	APPLICATION_JSON, APPLICATION_XML
})
@Api(value = "action")
@Stateless
@Default
@CrossDomain
public class DefaultActionResource implements ActionResource {

	@Inject
	private ActionService service;

	@Inject
	private ActionMapper actionMapper;

	@Context
	private UriInfo uriInfo;

	public DefaultActionResource() {}

	@PostConstruct
	public void init() {
		this.actionMapper.setRequestURI(this.uriInfo);
	}

	@POST
	@ApiOperation(
		value = "Creates a new action.", response = ActionData.class, code = 201
	)
	@Override
	public Response create(ActionEditionData actionEditionData) {
		Action action =
			this.service.create(
				new Action(actionEditionData.getName(), actionEditionData.getDescription())
			);
		return Response.created(UriBuilder.fromResource(DefaultActionResource.class).path(Integer.toString(action.getId())).build())
			.entity(this.actionMapper.toActionData(action)).build();
	}

	@GET
	@Path("{id}")
	@ApiOperation(
		value = "Return the data of an action.", response = ActionData.class, code = 200
	)
	@Override
	public Response getAction(@PathParam("id") int id) {
		return Response.ok(this.actionMapper.toActionData(this.service.get(id))).build();
	}

	@GET
	@ApiOperation(
		value = "Return the data of all actions.", response = ActionData.class, responseContainer = "List", code = 200
	)
	@Override
	public Response listActions() {
		return Response.ok(this.service.getActions().map(this.actionMapper::toActionData).toArray(ActionData[]::new))
			.build();
	}

	@PUT
	@Path("{id}")
	@ApiOperation(
		value = "Modifies an existing action.", response = ActionData.class, code = 200
	)
	@Override
	public Response edit(@PathParam("id") int id, ActionEditionData actionEditionData) {
		Action action = this.service.get(id);
		this.actionMapper.assignActionEditionData(action, actionEditionData);
		return Response.ok(this.actionMapper.toActionData(this.service.edit(action))).build();
	}

	@DELETE
	@Path("{id}")
	@ApiOperation(
		value = "Deletes an existing action.", code = 200
	)
	@ApiResponses(
		@ApiResponse(code = 400, message = "Unknown action: {id}")
	)
	@Override
	public Response delete(@PathParam("id") int id) {
		Action action = this.service.get(id);
		this.service.delete(action);
		return Response.ok().build();
	}
}
