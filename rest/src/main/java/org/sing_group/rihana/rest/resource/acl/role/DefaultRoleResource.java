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

package org.sing_group.rihana.rest.resource.acl.role;

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
import org.sing_group.rihana.domain.entities.acl.role.Role;
import org.sing_group.rihana.rest.entity.acl.role.RoleData;
import org.sing_group.rihana.rest.entity.acl.role.RoleEditionData;
import org.sing_group.rihana.rest.entity.mapper.spi.RoleMapper;
import org.sing_group.rihana.rest.filter.CrossDomain;
import org.sing_group.rihana.rest.resource.spi.acl.role.RoleResource;
import org.sing_group.rihana.service.spi.acl.role.RoleService;

@Path("role")
@Produces({
	APPLICATION_JSON + ";charset=utf-8", APPLICATION_XML + ";charset=utf-8"
})
@Consumes({
	APPLICATION_JSON + ";charset=utf-8", APPLICATION_XML + ";charset=utf-8"
})
@Api(value = "role")
@Stateless
@Default
@CrossDomain(allowedHeaders = "charset")
public class DefaultRoleResource implements RoleResource {

	@Inject
	private RoleService service;

	@Inject
	private RoleMapper roleMapper;

	@Context
	private UriInfo uriInfo;

	public DefaultRoleResource() {}

	@PostConstruct
	public void init() {
		this.roleMapper.setRequestURI(this.uriInfo);
	}

	@POST
	@ApiOperation(
		value = "Creates a new role.", response = RoleData.class, code = 201
	)
	@Override
	public Response create(RoleEditionData roleEditionData) {
		Role role =
			this.service.create(
				new Role(roleEditionData.getName(), roleEditionData.getDescription())
			);
		return Response.created(UriBuilder.fromResource(DefaultRoleResource.class).path(Integer.toString(role.getId())).build())
			.entity(this.roleMapper.toRoleData(role)).build();
	}

	@GET
	@Path("{id}")
	@ApiOperation(
		value = "Return the data of a role.", response = RoleData.class, code = 200
	)
	@Override
	public Response getRole(@PathParam("id") int id) {
		return Response.ok(this.roleMapper.toRoleData(this.service.get(id))).build();
	}

	@GET
	@ApiOperation(
		value = "Return the data of all roles.", response = RoleData.class, responseContainer = "List", code = 200
	)
	@Override
	public Response listRoles() {
		return Response.ok(this.service.getRoles().map(this.roleMapper::toRoleData).toArray(RoleData[]::new))
			.build();
	}

	@PUT
	@Path("{id}")
	@ApiOperation(
		value = "Modifies an existing role.", response = RoleData.class, code = 200
	)
	@Override
	public Response edit(@PathParam("id") int id, RoleEditionData roleEditionData) {
		Role role = this.service.get(id);
		this.roleMapper.assignRoleEditionData(role, roleEditionData);
		return Response.ok(this.roleMapper.toRoleData(this.service.edit(role))).build();
	}

	@DELETE
	@Path("{id}")
	@ApiOperation(
		value = "Deletes an existing role.", code = 200
	)
	@ApiResponses(
		@ApiResponse(code = 400, message = "Unknown role: {id}")
	)
	@Override
	public Response delete(@PathParam("id") int id) {
		Role role = this.service.get(id);
		this.service.delete(role);
		return Response.ok().build();
	}
}
