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
package org.sing_group.rihana.rest.resource.sign;


import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.util.stream.Stream;

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
import org.sing_group.rihana.domain.entities.sign.Sign;
import org.sing_group.rihana.rest.entity.mapper.spi.SignMapper;
import org.sing_group.rihana.rest.entity.mapper.spi.SignTypeMapper;
import org.sing_group.rihana.rest.entity.sign.SignData;
import org.sing_group.rihana.rest.entity.sign.SignTypeData;
import org.sing_group.rihana.rest.filter.CrossDomain;
import org.sing_group.rihana.rest.resource.spi.sign.SignResource;
import org.sing_group.rihana.service.spi.sign.SignService;
import org.sing_group.rihana.service.spi.sign.SignTypeService;
import org.sing_group.rihana.service.spi.user.UserService;

@RolesAllowed({
	"ADMIN", "USER", "RADIOLOGIST", "SENIORRADIOLOGIST"
})
@Path("sign")
@Produces({
	APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
	APPLICATION_JSON, APPLICATION_XML
})
@Api(value = "sign")
@Stateless
@Default
@CrossDomain
public class DefaultSignResource implements SignResource {

	@Inject
	private SignService service;

	@Inject
	private UserService userService;

	@Inject
	private SignTypeService signTypeService;

	@Inject
	private SignMapper signMapper;

	@Inject
	private SignTypeMapper signTypeMapper;

	@Context
	private UriInfo uriInfo;

	@PostConstruct
	public void init() {
		this.signMapper.setRequestURI(this.uriInfo);
	}

	@GET
	@ApiOperation(
		value = "Return the data of all signs or signs of a specified user.", response = SignData.class, responseContainer = "List", code = 200
	)
	@Override
	public Response listSigns(
		@QueryParam("user") String userId
	) {
		Stream<Sign> signs;
		if (userId == null || userId == "") {
			signs = this.service.listSigns();
		} else {
			signs = this.service.listSignsByUser(this.userService.get(userId));
		}
		return Response.ok(signs.map(this.signMapper::toSignData).toArray(SignData[]::new)).build();
	}

	@GET
	@Path("type")
	@ApiOperation(
		value = "Return the data of all signs of a specified user.", response = SignTypeData.class, responseContainer = "List", code = 200
	)
	@Override
	public Response listSignTypes() {
		return Response.ok(
			this.signTypeService.listSignTypes()
				.map(this.signTypeMapper::toSignTypeData).toArray(SignTypeData[]::new)
		).build();
	}
}
