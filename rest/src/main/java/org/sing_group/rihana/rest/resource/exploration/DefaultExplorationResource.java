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
package org.sing_group.rihana.rest.resource.exploration;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.sing_group.rihana.domain.entities.user.User;
import org.sing_group.rihana.rest.entity.exploration.ExplorationData;
import org.sing_group.rihana.rest.entity.mapper.spi.ExplorationMapper;
import org.sing_group.rihana.rest.filter.CrossDomain;
import org.sing_group.rihana.rest.resource.spi.exploration.ExplorationResource;
import org.sing_group.rihana.service.spi.exploration.ExplorationService;
import org.sing_group.rihana.service.spi.user.UserService;


@RolesAllowed({
	"ADMIN", "USER", "RADIOLOGIST"
})
@Path("exploration")
@Produces({
	APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
	APPLICATION_JSON, APPLICATION_XML
})
@Api(value = "exploration")
@Stateless
@Default
@CrossDomain(allowedHeaders = "X-Pagination-Total-Items")
public class DefaultExplorationResource implements ExplorationResource {

	@Inject
	private ExplorationService service;

	@Inject
	private UserService userService;

	@Inject
	private ExplorationMapper explorationMapper;

	@Context
	private UriInfo uriInfo;

	@PostConstruct
	public void init() {
		this.explorationMapper.setRequestURI(this.uriInfo);
	}

	@GET
	@ApiOperation(
		value = "Return the data of all explorations or explorations of a specified user.",
		response = ExplorationData.class, responseContainer = "List", code = 200
	)
	@ApiResponses(
		@ApiResponse(code = 400, message = "Invalid page or pageSize. They must be an integer.")
	)
	@Override
	public Response listExplorationsByUser(
		@QueryParam("user") String userId, @QueryParam("page") int page, @QueryParam("pageSize") int pageSize
	) {
		User user = null;
		int countExplorations;
		if (userId == null || userId.equals("")) {
			countExplorations = this.service.countExplorations();
		} else {
			user = this.userService.get(userId);
			countExplorations = this.service.countExplorationsByUser(user);
		}

		return Response.ok(
			this.service.listExplorationsByUser(page, pageSize, user)
				.map(this.explorationMapper::toExplorationData).toArray(ExplorationData[]::new)
		).header("X-Pagination-Total-Items", countExplorations).build();
	}
}
