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
package org.sing_group.rihana.rest.resource.user;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.sing_group.rihana.domain.entities.user.Role;
import org.sing_group.rihana.domain.entities.user.User;
import org.sing_group.rihana.rest.entity.mapper.spi.UserMapper;
import org.sing_group.rihana.rest.entity.user.UserData;
import org.sing_group.rihana.rest.entity.user.UserEditionData;
import org.sing_group.rihana.rest.filter.CrossDomain;
import org.sing_group.rihana.rest.mapper.SecurityExceptionMapper;
import org.sing_group.rihana.rest.resource.spi.user.UserResource;
import org.sing_group.rihana.service.spi.user.UserService;

@RolesAllowed({
	"ADMIN", "USER"
})
@Path("user")
@Produces({
	APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
	APPLICATION_JSON, APPLICATION_XML
})
@Api("user")
@Stateless
@Default
@CrossDomain
public class DefaultUserResource implements UserResource {

	@Inject
	private UserService userService;

	@Inject
	private UserMapper userMapper;

	@GET
	@Path("{login}/role")
	@ApiOperation(
		value = "Checks the provided credentials", response = Role.class, code = 200
	)
	@ApiResponses({
		@ApiResponse(code = 200, message = "successful operation"),
		@ApiResponse(code = 401, message = SecurityExceptionMapper.UNAUTHORIZED_MESSAGE)
	})
	@Override
	public Response getRole(@PathParam("login") String login) {
		User currentUser = this.userService.getCurrentUser();
		if (!login.equals(currentUser.getLogin())) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		return Response.ok(
			currentUser.getRole()
		).build();
	}

	@POST
	@RolesAllowed("ADMIN")
	@ApiOperation(
		value = "Creates a new user.", response = UserData.class, code = 201
	)
	@ApiResponses(
		@ApiResponse(code = 400, message = "Entity already exists")
	)
	@Override
	public Response create(UserEditionData userEditionData) {
		User user = new User(userEditionData.getLogin(), userEditionData.getPassword(), userEditionData.getRole());
		user = this.userService.create(user);
		return Response.created(UriBuilder.fromResource(DefaultUserResource.class).path(user.getLogin()).build())
			.entity(userMapper.toUserData(user)).build();
	}

	@GET
	@RolesAllowed("ADMIN")
	@ApiOperation(
		value = "Return the data of all users.", response = UserData.class, responseContainer = "List", code = 200
	)
	@Override
	public Response getUsers() {
		return Response.ok(
			this.userService.getUsers().map(this.userMapper::toUserData).toArray(UserData[]::new)
		).build();
	}
}
