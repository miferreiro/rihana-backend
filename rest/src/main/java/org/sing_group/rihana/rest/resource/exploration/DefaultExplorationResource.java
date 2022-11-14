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

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
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
import org.sing_group.rihana.service.spi.acl.permission.PermissionService;
import org.sing_group.rihana.service.spi.exploration.ExplorationService;
import org.sing_group.rihana.service.spi.exploration.ExplorationStorage;
import org.sing_group.rihana.service.spi.patient.PatientService;
import org.sing_group.rihana.service.spi.radiograph.RadiographService;
import org.sing_group.rihana.service.spi.report.ExplorationCodeService;
import org.sing_group.rihana.service.spi.report.PerformedExplorationService;
import org.sing_group.rihana.service.spi.report.ReportService;
import org.sing_group.rihana.service.spi.report.RequestedExplorationService;
import org.sing_group.rihana.service.spi.sign.SignService;
import org.sing_group.rihana.service.spi.sign.SignTypeService;
import org.sing_group.rihana.service.spi.user.UserService;

@Path("exploration")
@Produces({
	APPLICATION_JSON + ";charset=utf-8", APPLICATION_XML + ";charset=utf-8"
})
@Consumes({
	APPLICATION_JSON + ";charset=utf-8", APPLICATION_XML + ";charset=utf-8"
})
@Api(value = "exploration")
@Stateless
@Default
@CrossDomain(allowedHeaders = {"X-Pagination-Total-Items", "charset"})
public class DefaultExplorationResource implements ExplorationResource {

	@Resource
	private SessionContext context;

	@Inject
	private PermissionService permissionService;

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
	private RequestedExplorationService requestedExplorationService;

	@Inject
	private PerformedExplorationService performedExplorationService;

	@Inject
	private ExplorationCodeService explorationCodeService;

	@Inject
	private RadiographService radiographService;

	@Inject
	private ExplorationStorage explorationStorage;

	@Inject
	private SignService signService;

	@Inject
	private ExplorationMapper explorationMapper;

	@Inject
	private DateFormatter dateFormatter;

	@Context
	private UriInfo uriInfo;

	public DefaultExplorationResource() {}

	@PostConstruct
	public void init() {
		this.explorationMapper.setRequestURI(this.uriInfo);
	}

	@Path("{id}")
	@GET
	@ApiOperation(
		value = "Return the data of an exploration.", response = ExplorationData.class, code = 200
	)
	@ApiResponses(
		@ApiResponse(code = 400, message = "Unknown exploration: {id}")
	)
	@Override
	public Response getExploration(
		@PathParam("id") String id
	) {
		return Response
			.ok(this.explorationMapper.toExplorationData(this.service.getExploration(id)))
			.build();
	}

	@Path("recover/{id}")
	@GET
	@ApiOperation(
		value = "Return the data of a deleted exploration.", response = ExplorationData.class, code = 200
	)
	@ApiResponses(
		@ApiResponse(code = 400, message = "Unknown exploration: {id}")
	)
	@Override
	public Response getExplorationDeleted(
		@PathParam("id") String id
	) {
		return Response
			.ok(this.explorationMapper.toExplorationData(this.service.getExplorationDeleted(id)))
			.build();
	}

	@GET
	@ApiOperation(
		value = "Return the data of all explorations or explorations of a specified user. In addition, can be filtered by the sign types detected in the explorations.",
		response = ExplorationData.class, responseContainer = "List", code = 200
	)
	@ApiResponses(
		@ApiResponse(code = 400, message = "Invalid page or pageSize. They must be an integer.")
	)
	@Override
	public Response listExplorations(
		@QueryParam("user") String userId,
		@QueryParam("page") int page, @QueryParam("pageSize") int pageSize,
		@QueryParam("initialDate") String initialDateStr, @QueryParam("finalDate") String finalDateStr,
		@QueryParam("signType") String signTypes
	) {
		User user = null;
		int countExplorations;
		Set<SignType> signTypeSet;
		Date initialDate = null;
		Date finalDate = null;
		String operator = "AND";

		if (initialDateStr != null && !initialDateStr.isEmpty() && initialDateStr != "") {
			initialDate = dateFormatter.getDateInitialDayTime(initialDateStr);
		}

		if (finalDateStr != null && !finalDateStr.isEmpty() && finalDateStr != "") {
			finalDate = dateFormatter.getDateFinalDayTime(finalDateStr);
		}

		if (signTypes == null || signTypes.equals("")) {
			signTypeSet = new HashSet<>();
		} else {
			if (signTypes.contains(",")) {
				operator = "OR";
				signTypeSet = Arrays.stream(signTypes.split(","))
					.map(signType -> this.signTypeService.get(signType)).collect(Collectors.toSet());
			} else {
				operator = "AND";
				signTypeSet = Arrays.stream(signTypes.split(";"))
					.map(signType -> this.signTypeService.get(signType)).collect(Collectors.toSet());
			}
		}

		String loginLogged = context.getCallerPrincipal().getName();
		if (userId == null || userId.equals("")) {
			countExplorations = this.service.countExplorationsByUserAndSignTypesInDateRange(null, initialDate,
				finalDate, signTypeSet, operator);
		} else {
			user = this.userService.get(userId);
			countExplorations = this.service.countExplorationsByUserAndSignTypesInDateRange(user, initialDate,
				finalDate, signTypeSet, operator);
		}


		if (this.permissionService.hasPermission(loginLogged, "EXPLORATION_MANAGEMENT", "RETRIEVE") ||
			this.permissionService.isAdmin(loginLogged)) {
			return Response.ok(
				this.service.listExplorationsByUserInDateRange(page, pageSize, null, initialDate, finalDate,
						signTypeSet, operator)
					.map(this.explorationMapper::toExplorationAdminData).toArray(ExplorationAdminData[]::new)
			).header("X-Pagination-Total-Items", countExplorations).build();
		} else {
			return Response.ok(
				this.service.listExplorationsByUserInDateRange(page, pageSize, user, initialDate, finalDate,
						signTypeSet, operator)
					.map(this.explorationMapper::toExplorationData).toArray(ExplorationData[]::new)
			).header("X-Pagination-Total-Items", countExplorations).build();
		}
	}

	@POST
	@ApiOperation(
		value = "Creates a new exploration.", response = ExplorationData.class, code = 201
	)
	@ApiResponses({
		@ApiResponse(code = 400, message = "Unknown exploration: {id}"),
		@ApiResponse(code = 409, message = "The report already exists")
	})
	@Override
	public Response create(ExplorationEditionData explorationEditionData) {

		if (explorationEditionData.getReport().getReportN() != null && reportService.existsReportNBy(explorationEditionData.getReport().getReportN())) {
			return Response.status(Response.Status.CONFLICT).entity("The report already exists").build();
		}

		User user = this.userService.get(explorationEditionData.getUser());

		Patient patient = createPatient(explorationEditionData);

		String title = "Exploration " + (this.service.getLastTitleExploration() + 1);
		Exploration exploration = new Exploration(
			title,
			explorationEditionData.getExplorationDate(),
			explorationEditionData.getSource(),
			user,
			patient
		);

		exploration = this.service.create(exploration);
		createReport(explorationEditionData, exploration);
		createRadiographs(explorationEditionData, exploration);

		this.explorationStorage.storeExplorationXml(exploration);

		return Response.created(UriBuilder.fromResource(DefaultExplorationResource.class).path(exploration.getId()).build())
			.entity(explorationMapper.toExplorationData(exploration)).build();
	}

	@PUT
	@Path("{id}")
	@ApiOperation(
		value = "Modifies an existing exploration.", response = ExplorationData.class, code = 200
	)
	@ApiResponses({
		@ApiResponse(code = 400, message = "Unknown exploration: {id}"),
		@ApiResponse(code = 409, message = "The report already exists")
	})
	@Override
	public Response edit(@PathParam("id") String id, ExplorationEditionData explorationEditionData) {

		Exploration exploration = this.service.getExploration(id);

		exploration.setSource(explorationEditionData.getSource());

		if (explorationEditionData.getReport() != null &&
			(exploration.getCurrentReport() == null ||
			(explorationEditionData.getReport() != null && exploration.getCurrentReport() != null &&
				explorationEditionData.getReport().getReportN() != null && exploration.getCurrentReport().getReportN() != null &&
				!explorationEditionData.getReport().getReportN().equals(exploration.getCurrentReport().getReportN())))) {
			if (reportService.existsReportNBy(explorationEditionData.getReport().getReportN())) {
				return Response.status(Response.Status.CONFLICT).entity("The report already exists").build();
			}
		}

		if (exploration.getPatient() != null && exploration.getPatient().getPatientID() == null &&
			explorationEditionData.getPatient().getPatientID() == null) {
			Patient patient = editPatient(exploration, explorationEditionData);
			exploration.setPatient(patient);
		} else {
			Patient patient = createPatient(explorationEditionData);
			exploration.setPatient(patient);
		}

		this.service.edit(exploration);

		exploration = this.service.getExploration(id);

		Iterator<Report> iterator = new HashSet<>(exploration.getReports()).iterator();
		while (iterator.hasNext()) {
			Report report = iterator.next();
			if (!report.isDeleted()) {
				this.reportService.delete(report);
			}
		}

		createReport(explorationEditionData, exploration);

		this.explorationStorage.deleteRadiographsExploration(exploration);

		for (Radiograph radiograph: exploration.getCurrentRadiographs()) {
			if (!radiograph.isDeleted()) {
				this.radiographService.delete(radiograph);
			}
		}

		createRadiographs(explorationEditionData, exploration);

		this.explorationStorage.storeExplorationXml(exploration);

		return Response.ok(this.explorationMapper.toExplorationData(exploration)).build();
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
	@Path("recover/{id}")
	@ApiOperation(
		value = "Recovers an existing deleted exploration.", code = 200
	)
	@ApiResponses({
		@ApiResponse(code = 400, message = "Unknown exploration: {id}"),
		@ApiResponse(code = 409, message = "The report already exists"),
		@ApiResponse(code = 430, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
	})
	@Override
	public Response recover(@PathParam("id") String id) {
		Exploration exploration = this.service.getExplorationDeleted(id);

		if (exploration.getCurrentReport() != null &&
			reportService.existsReportNBy(exploration.getCurrentReport().getReportN())) {
			return Response.status(Response.Status.CONFLICT).entity("The report already exists").build();
		}

		this.service.recover(exploration);
		return Response.ok().build();
	}

	private Patient editPatient(Exploration exploration, ExplorationEditionData explorationEditionData) {
		Patient patient = null;

		if  (explorationEditionData.getPatient() != null) {
			PatientEditionData patientEditionData = explorationEditionData.getPatient();

			patient = this.patientService.get(exploration.getPatient().getId());
			patient.setSex(explorationEditionData.getPatient().getSex());
			patient.setBirthdate(explorationEditionData.getPatient().getBirthdate());

			patient = this.patientService.edit(patient);
		}

		return patient;
	}


	private Patient createPatient(ExplorationEditionData explorationEditionData) {
		Patient patient = null;

		if  (explorationEditionData.getPatient() != null) {
			PatientEditionData patientEditionData = explorationEditionData.getPatient();

			if (patientEditionData.getPatientID() == null ||
				!this.patientService.existsPatientBy(patientEditionData.getPatientID())) {
				patient = new Patient(patientEditionData.getPatientID(),
					patientEditionData.getSex(),
					patientEditionData.getBirthdate());
				patient = this.patientService.create(patient);
			} else {
				patient = this.patientService.getPatientBy(patientEditionData.getPatientID());
			}
		}

		return patient;
	}

	private void createReport(ExplorationEditionData explorationEditionData, Exploration exploration) {
		if (explorationEditionData.getReport() != null) {
			ReportEditionData reportEditionData = explorationEditionData.getReport();
			Report report = new Report(reportEditionData.getType(),
				reportEditionData.getReportN(),
				reportEditionData.getCompletionDate(),
				reportEditionData.getApplicant(),
				reportEditionData.getPriority(),
				reportEditionData.getStatus(),
				reportEditionData.getBed(),
				reportEditionData.getClinicalData(),
				reportEditionData.getFindings(),
				reportEditionData.getConclusions());

			report.setExploration(exploration);
			this.reportService.create(report);

			if (reportEditionData.getRequestedExplorations() != null) {
				for (RequestedExplorationEditionData r : reportEditionData.getRequestedExplorations()) {
					ExplorationCode explorationCode;
					if (!this.explorationCodeService.existsExplorationCodeBy(r.getCode())) {
						explorationCode = new ExplorationCode(r.getCode(), r.getDescription());
						this.explorationCodeService.create(explorationCode);
					} else {
						explorationCode = this.explorationCodeService.getExplorationCode(r.getCode());
					}

					RequestedExploration requestedExploration = new RequestedExploration(r.getDate(), report, explorationCode);
					this.requestedExplorationService.create(requestedExploration);
					report.internalAddRequestedExploration(requestedExploration);
				}
			}

			if (reportEditionData.getPerformedExplorations() != null) {
				for (PerformedExplorationEditionData p : reportEditionData.getPerformedExplorations()) {
					ExplorationCode explorationCode;
					if (!this.explorationCodeService.existsExplorationCodeBy(p.getCode())) {
						explorationCode = new ExplorationCode(p.getCode(), p.getDescription());
						this.explorationCodeService.create(explorationCode);
					} else {
						explorationCode = this.explorationCodeService.getExplorationCode(p.getCode());
					}

					PerformedExploration performedExploration = new PerformedExploration(p.getDate(), p.getPortable(), p.getSurgery(), report, explorationCode);
					this.performedExplorationService.create(performedExploration);
					report.internalAddPerformedExploration(performedExploration);
				}
			}
			this.reportService.edit(report);
		}
	}

	private void createRadiographs(ExplorationEditionData explorationEditionData, Exploration exploration) {
		Set<RadiographEditionData> radiographEditionDataList = explorationEditionData.getRadiographs();

		for(RadiographEditionData r: radiographEditionDataList) {
			Radiograph radiograph = new Radiograph(r.getType(), r.getObservations());
			radiograph.setExploration(exploration);

			this.radiographService.create(radiograph, r.getSource());

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
	}
}
