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
import org.sing_group.rihana.domain.entities.acl.permission.FunctionalityActionKey;
import org.sing_group.rihana.domain.entities.acl.permission.Permission;
import org.sing_group.rihana.domain.entities.acl.permission.PermissionKey;
import org.sing_group.rihana.rest.entity.acl.functionality.FunctionalityData;
import org.sing_group.rihana.rest.entity.acl.permission.FunctionalityActionData;
import org.sing_group.rihana.rest.entity.acl.permission.PermissionData;
import org.sing_group.rihana.rest.entity.mapper.spi.PermissionMapper;
import org.sing_group.rihana.rest.filter.CrossDomain;
import org.sing_group.rihana.rest.resource.acl.action.DefaultActionResource;
import org.sing_group.rihana.rest.resource.spi.acl.permission.PermissionResource;
import org.sing_group.rihana.service.spi.acl.permission.PermissionService;
import org.sing_group.rihana.service.spi.user.UserService;

@Path("permission")
@Produces({
	APPLICATION_JSON + ";charset=utf-8", APPLICATION_XML + ";charset=utf-8"
})
@Consumes({
	APPLICATION_JSON + ";charset=utf-8", APPLICATION_XML + ";charset=utf-8"
})
@Api(value = "permission")
@Stateless
@Default
@CrossDomain(allowedHeaders = "charset")
public class DefaultPermissionResource implements PermissionResource {

	@Inject
	private PermissionService service;

	@Inject
	private PermissionMapper permissionMapper;

	@Inject
	private UserService userService;

	@Context
	private UriInfo uriInfo;

	public DefaultPermissionResource() {}

	@PostConstruct
	public void init() {
		this.permissionMapper.setRequestURI(this.uriInfo);
	}

	@POST
	@ApiOperation(
		value = "Creates a new permission.", response = PermissionData.class, code = 201
	)
	@Override
	public Response create(PermissionData permissionData) {
		Permission permission =
			this.service.create(
				new Permission(
					new PermissionKey(
						permissionData.getRoleId(),
						new FunctionalityActionKey(
							permissionData.getFunctionalityId(),
							permissionData.getActionId()
						)
					)
				)
			);
		return Response.created(
			UriBuilder.fromResource(DefaultActionResource.class)
				.path(permission.getPermissionKey().getRoleId() +
					"/" +
					permission.getPermissionKey().getFunctionalityActionId().getFunctionalityId() +
					"/" +
					permission.getPermissionKey().getFunctionalityActionId().getActionId()).build())
			.entity(this.permissionMapper.toPermissionData(permission))
			.build();
	}

	@GET
	@Path("{roleId}/{functionalityId}/{actionId}")
	@ApiOperation(
		value = "Return the data of a permission.", response = PermissionData.class, code = 200
	)
	@Override
	public Response getPermission(@PathParam("roleId") int roleId,
								  @PathParam("functionalityId") int functionalityId,
								  @PathParam("actionId") int actionId) {
		return Response.ok(this.permissionMapper.toPermissionData(this.service.get(
			new PermissionKey(
				roleId,
				new FunctionalityActionKey(
					functionalityId,
					actionId
				)
			)
		))).build();
	}

	@GET
	@Path("{login}")
	@ApiOperation(
		value = "Return the permissions of a user.", response = FunctionalityData.class, code = 200
	)
	@Override
	public Response getUserPermissions(@PathParam("login") String login) {
		return Response.ok(this.userService.get(login).getRole().getPermissions()
			.stream().map(this.permissionMapper::toFunctionalityActionData)
			.toArray(FunctionalityActionData[]::new))
			.build();
	}

	@GET
	@ApiOperation(
		value = "Return the data of all permissions.", response = PermissionData.class,
		responseContainer = "List", code = 200
	)
	@Override
	public Response listPermissions() {
		return Response.ok(this.service.getPermissions()
			.map(this.permissionMapper::toPermissionData)
			.toArray(PermissionData[]::new))
			.build();
	}

	@DELETE
	@Path("{roleId}/{functionalityId}/{actionId}")
	@ApiOperation(
		value = "Deletes an existing permission.", code = 200
	)
	@ApiResponses(
		@ApiResponse(code = 400, message = "Unknown functionality action: {id}")
	)
	@Override
	public Response delete(@PathParam("roleId") int roleId,
						   @PathParam("functionalityId") int functionalityId,
						   @PathParam("actionId") int actionId) {
		Permission permission = this.service.get(
			new PermissionKey(
				roleId,
				new FunctionalityActionKey(
					functionalityId,
					actionId
				)
			)
		);
		this.service.delete(permission);
		return Response.ok().build();
	}
}
