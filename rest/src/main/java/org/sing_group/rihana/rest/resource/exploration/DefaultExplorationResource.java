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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.sing_group.rihana.domain.entities.exploration.Exploration;
import org.sing_group.rihana.domain.entities.patient.Patient;
import org.sing_group.rihana.domain.entities.radiograph.Radiograph;
import org.sing_group.rihana.domain.entities.report.ExplorationCode;
import org.sing_group.rihana.domain.entities.report.PerformedExploration;
import org.sing_group.rihana.domain.entities.report.Report;
import org.sing_group.rihana.domain.entities.report.RequestedExploration;
import org.sing_group.rihana.domain.entities.sign.Sign;
import org.sing_group.rihana.domain.entities.sign.SignLocation;
import org.sing_group.rihana.domain.entities.sign.SignType;
import org.sing_group.rihana.domain.entities.user.Role;
import org.sing_group.rihana.domain.entities.user.User;
import org.sing_group.rihana.rest.entity.exploration.ExplorationAdminData;
import org.sing_group.rihana.rest.entity.exploration.ExplorationData;
import org.sing_group.rihana.rest.entity.exploration.ExplorationEditionData;
import org.sing_group.rihana.rest.entity.mapper.spi.ExplorationMapper;
import org.sing_group.rihana.rest.entity.patient.PatientEditionData;
import org.sing_group.rihana.rest.entity.radiograph.RadiographEditionData;
import org.sing_group.rihana.rest.entity.report.PerformedExplorationEditionData;
import org.sing_group.rihana.rest.entity.report.ReportEditionData;
import org.sing_group.rihana.rest.entity.report.RequestedExplorationEditionData;
import org.sing_group.rihana.rest.entity.sign.SignData;
import org.sing_group.rihana.rest.filter.CrossDomain;
import org.sing_group.rihana.rest.mapper.SecurityExceptionMapper;
import org.sing_group.rihana.rest.resource.spi.exploration.ExplorationResource;
import org.sing_group.rihana.service.spi.exploration.ExplorationService;
import org.sing_group.rihana.service.spi.patient.PatientService;
import org.sing_group.rihana.service.spi.radiograph.RadiographService;
import org.sing_group.rihana.service.spi.report.ExplorationCodeService;
import org.sing_group.rihana.service.spi.report.ReportService;
import org.sing_group.rihana.service.spi.sign.SignService;
import org.sing_group.rihana.service.spi.sign.SignTypeService;
import org.sing_group.rihana.service.spi.user.UserService;


@RolesAllowed({
	"ADMIN", "USER", "RADIOLOGIST", "SUPERVISOR"
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
	private SignTypeService signTypeService;

	@Inject
	private PatientService patientService;

	@Inject
	private ReportService reportService;

	@Inject
	private ExplorationCodeService explorationCodeService;

	@Inject
	private RadiographService radiographService;

	@Inject
	private SignService signService;

	@Inject
	private ExplorationMapper explorationMapper;

	@Context
	private UriInfo uriInfo;

	public DefaultExplorationResource() {}

	@PostConstruct
	public void init() {
		this.explorationMapper.setRequestURI(this.uriInfo);
	}

	@GET
	@ApiOperation(
		value = "Return the data of all explorations or explorations of a specified user. In addition, can be filtered by the sign types detected in the explorations",
		response = ExplorationData.class, responseContainer = "List", code = 200
	)
	@ApiResponses(
		@ApiResponse(code = 400, message = "Invalid page or pageSize. They must be an integer.")
	)
	@Override
	public Response listExplorations(
		@QueryParam("user") String userId, @QueryParam("page") int page, @QueryParam("pageSize") int pageSize, @QueryParam("signType") List<String> signTypes
	) {
		User user = null;
		int countExplorations;
		List<SignType> signTypeList;

		if (signTypes == null || signTypes.size() == 0) {
			signTypeList = new ArrayList<>();
		} else {
			signTypeList = Arrays.asList(signTypes.stream().map(signType -> this.signTypeService.get(signType)).toArray(SignType[]::new));
		}

		if (userId == null || userId.equals("")) {
			countExplorations = this.service.countExplorations();
		} else {
			user = this.userService.get(userId);
			if (user.getRole() == Role.ADMIN) {
				countExplorations = this.service.countExplorations();
			} else {
				countExplorations = this.service.countExplorationsByUserAndSignTypes(user, signTypeList);
			}
		}

		if (userId != null && !userId.equals("") && user.getRole() == Role.ADMIN) {
			return Response.ok(
				this.service.listExplorationsByUser(page, pageSize, user, signTypeList)
					.map(this.explorationMapper::toExplorationAdminData).toArray(ExplorationAdminData[]::new)
			).header("X-Pagination-Total-Items", countExplorations).build();
		} else {
			return Response.ok(
				this.service.listExplorationsByUser(page, pageSize, user, signTypeList)
					.map(this.explorationMapper::toExplorationData).toArray(ExplorationData[]::new)
			).header("X-Pagination-Total-Items", countExplorations).build();
		}
	}

	@POST
	@ApiOperation(
		value = "Creates a new exploration.", response = ExplorationData.class, code = 201
	)
	@ApiResponses(
		@ApiResponse(code = 400, message = "Unknown exploration: {id}")
	)
	@Override
	public Response create(ExplorationEditionData explorationEditionData) {

		User user = this.userService.get(explorationEditionData.getUser());

		Patient patient;

		if  (explorationEditionData.getPatient() != null) {
			PatientEditionData patientEditionData = explorationEditionData.getPatient();

			if (!this.patientService.existsPatientBy(patientEditionData.getPatientID())) {
				patient = this.patientService.create(
					new Patient(patientEditionData.getPatientID(),
						patientEditionData.getSex(),
						patientEditionData.getBirthdate()));
			} else {
				patient = this.patientService.getPatientBy(patientEditionData.getPatientID());
			}
		} else {
			patient = null;
		}

		String title = "Exploration " + (this.service.countAllExplorations() + 1);
		Exploration exploration = new Exploration(
			title,
			explorationEditionData.getExplorationDate(),
			user,
			patient
		);

		if (explorationEditionData.getReport() != null) {
			ReportEditionData reportEditionData = explorationEditionData.getReport();
			Report report = new Report(reportEditionData.getReportN(),
				reportEditionData.getCompletion_date(),
				reportEditionData.getApplicant(),
				reportEditionData.getPriority(),
				reportEditionData.getStatus(),
				reportEditionData.getBed(),
				reportEditionData.getClinical_data(),
				reportEditionData.getFindings(),
				reportEditionData.getConclusions());

			for (RequestedExplorationEditionData r : reportEditionData.getRequestedExplorations()) {

				ExplorationCode explorationCode;
				if (!this.explorationCodeService.existsExplorationCodeBy(r.getCode())) {
					explorationCode = new ExplorationCode(r.getCode(), r.getDescription());
				} else {
					explorationCode = this.explorationCodeService.getExplorationCode(r.getCode());
				}

				RequestedExploration requestedExploration = new RequestedExploration(r.getDate(), report, explorationCode);
				report.internalAddRequestedExploration(requestedExploration);
			}

			for (PerformedExplorationEditionData p : reportEditionData.getPerformedExplorations()) {

				ExplorationCode explorationCode;
				if (!this.explorationCodeService.existsExplorationCodeBy(p.getCode())) {
					explorationCode = new ExplorationCode(p.getCode(), p.getDescription());
				} else {
					explorationCode = this.explorationCodeService.getExplorationCode(p.getCode());
				}

				PerformedExploration performedExploration = new PerformedExploration(p.getDate(), p.getPortable(), p.getSurgery(), report, explorationCode);
				report.internalAddPerformedExploration(performedExploration);
			}

			report.setExploration(exploration);

			this.reportService.create(report);
		}

		List<RadiographEditionData> radiographEditionDataList = explorationEditionData.getRadiographs();

		for(RadiographEditionData r: radiographEditionDataList) {
			Radiograph radiograph = new Radiograph(r.getSource(), r.getType(), r.getObservations());
			radiograph.setExploration(exploration);

			this.radiographService.create(radiograph);

			for(SignData s: r.getSigns()) {
				SignType signType = this.signTypeService.get(s.getType().getCode());

				Sign sign = new Sign(signType, s.getBrightness(), s.getContrast(), radiograph);
				this.signService.create(sign);
				if (s.getLocation() != null) {
					new SignLocation(s.getLocation().getX(), s.getLocation().getY(), s.getLocation().getWidth(),
						s.getLocation().getHeight(), sign);
				}
			}
		}

		exploration = this.service.create(exploration);

		return Response.created(UriBuilder.fromResource(DefaultExplorationResource.class).path(exploration.getId()).build())
			.entity(explorationMapper.toExplorationData(exploration)).build();
	}

	@DELETE
	@Path("{id}")
	@ApiOperation(
		value = "Deletes an existing exploration.", code = 200
	)
	@ApiResponses({
		@ApiResponse(code = 400, message = "Unknown exploration: {id}"),
		@ApiResponse(code = 430, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
	})
	@Override
	public Response delete(@PathParam("id") String id) {
		Exploration exploration = this.service.getExploration(id);
		this.service.delete(exploration);
		return Response.ok().build();
	}

	@PUT
	@RolesAllowed("ADMIN")
	@Path("recover/{id}")
	@ApiOperation(
		value = "Recovers an existing deleted exploration.", code = 200
	)
	@ApiResponses({
		@ApiResponse(code = 400, message = "Unknown exploration: {id}"),
		@ApiResponse(code = 430, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
	})
	@Override
	public Response recover(@PathParam("id") String id) {
		Exploration exploration = this.service.getExplorationDeleted(id);
		this.service.recover(exploration);
		return Response.ok().build();
	}
}
