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
package org.sing_group.rihana.rest.resource.patient;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
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
import org.sing_group.rihana.domain.entities.patient.Patient;
import org.sing_group.rihana.rest.entity.mapper.spi.PatientMapper;
import org.sing_group.rihana.rest.entity.patient.PatientData;
import org.sing_group.rihana.rest.entity.patient.PatientEditionData;
import org.sing_group.rihana.rest.filter.CrossDomain;
import org.sing_group.rihana.rest.resource.spi.patient.PatientResource;
import org.sing_group.rihana.service.spi.patient.PatientService;

@RolesAllowed({
	"ADMIN", "USER", "RADIOLOGIST"
})
@Path("patient")
@Produces({
	APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
	APPLICATION_JSON, APPLICATION_XML
})
@Api(value = "patient")
@Stateless
@Default
@CrossDomain
public class DefaultPatientResource implements PatientResource {

	@Inject
	private PatientService service;

	@Inject
	private PatientMapper patientMapper;

	@Context
	private UriInfo uriInfo;

	public DefaultPatientResource() {}

	@PostConstruct
	public void init() {
		this.patientMapper.setRequestURI(this.uriInfo);
	}

	@POST
	@ApiOperation(
		value = "Creates a new patient.", response = PatientData.class, code = 201
	)
	@Override
	public Response create(PatientEditionData patientEditionData) {
		Patient patient =
			this.service.create(
				new Patient(
					patientEditionData.getPatientID(), patientEditionData.getSex(), patientEditionData.getBirthdate()
				)
			);
		return Response.created(UriBuilder.fromResource(DefaultPatientResource.class).path(patient.getId()).build())
			.entity(patientMapper.toPatientData(patient)).build();
	}

	@GET
	@Path("{id}")
	@ApiOperation(
		value = "Return the data of a patient.", response = PatientData.class, code = 200
	)
	@Override
	public Response getPatient(@PathParam("id") String id) {
		return Response.ok(this.patientMapper.toPatientData(this.service.get(id))).build();
	}

	@GET
	@ApiOperation(
		value = "Return the data of all patients.", response = PatientData.class, responseContainer = "List", code = 200
	)
	@Override
	public Response listPatients() {
		return Response.ok(this.service.getPatients().map(this.patientMapper::toPatientData).toArray(PatientData[]::new))
			.build();
	}

	@GET
	@Path("patientID/{patientID}")
	@ApiOperation(
		value = "Return the data of a patient with the identifier of patient.", response = PatientData.class, code = 200
	)
	@Override
	public Response getPatientBy(@PathParam("patientID") String patientID) {
		return Response.ok(this.patientMapper.toPatientData(this.service.getPatientBy(patientID))).build();
	}

	@Path("{id}")
	@PUT
	@ApiOperation(
		value = "Modifies an existing patient", response = PatientData.class, code = 200
	)
	@Override
	public Response edit(@PathParam("id") String id, PatientEditionData patientEditionData) {
		Patient patient = this.service.get(id);
		this.patientMapper.assignPatientEditionData(patient, patientEditionData);
		return Response.ok(this.patientMapper.toPatientData(this.service.edit(patient))).build();
	}

	@DELETE
	@Path("{id}")
	@ApiOperation(
		value = "Deletes an existing patient.", code = 200
	)
	@ApiResponses(
		@ApiResponse(code = 400, message = "Unknown patient: {id}")
	)
	@Override
	public Response delete(@PathParam("id") String id) {
		Patient patient = this.service.get(id);
		this.service.delete(patient);
		return Response.ok().build();
	}
}
