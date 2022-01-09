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
package org.sing_group.rihana.rest.entity.mapper;

import static org.sing_group.rihana.rest.entity.UuidAndUri.fromEntities;
import static org.sing_group.rihana.rest.entity.UuidAndUri.fromEntity;

import java.util.ArrayList;
import java.util.Set;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

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
import org.sing_group.rihana.rest.resource.patient.DefaultPatientResource;
import org.sing_group.rihana.rest.resource.radiograph.DefaultRadiographResource;
import org.sing_group.rihana.rest.resource.report.DefaultReportResource;
import org.sing_group.rihana.service.spi.patient.PatientService;
import org.sing_group.rihana.service.spi.radiograph.RadiographService;
import org.sing_group.rihana.service.spi.report.ExplorationCodeService;
import org.sing_group.rihana.service.spi.report.ReportService;
import org.sing_group.rihana.service.spi.sign.SignService;
import org.sing_group.rihana.service.spi.sign.SignTypeService;

@Default
public class DefaultExplorationMapper implements ExplorationMapper {

	private UriInfo requestURI;

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
	private SignTypeService signTypeService;

	@Override
	public void setRequestURI(UriInfo requestURI) {
		this.requestURI = requestURI;
	}

	@Override
	public ExplorationData toExplorationData(Exploration exploration) {
		return new ExplorationData(
			exploration.getId(), exploration.getTitle(), exploration.getDate(), exploration.getUser(),
			fromEntity(requestURI, exploration.getPatient(), DefaultPatientResource.class),
			fromEntity(requestURI, exploration.getReport(), DefaultReportResource.class),
			fromEntities(requestURI, new ArrayList<>(exploration.getRadiographs()), DefaultRadiographResource.class)
		);
	}
	@Override
	public ExplorationAdminData toExplorationAdminData(Exploration exploration) {
		return new ExplorationAdminData(
			exploration.getId(), exploration.getTitle(), exploration.getDate(), exploration.getUser(),
			fromEntity(requestURI, exploration.getPatient(), DefaultPatientResource.class),
			fromEntity(requestURI, exploration.getReport(), DefaultReportResource.class),
			fromEntities(requestURI, new ArrayList<>(exploration.getRadiographs()), DefaultRadiographResource.class),
			exploration.isDeleted()
		);
	}

	@Override
	public void assignExplorationEditData(Exploration exploration, ExplorationEditionData explorationEditionData) {
		Patient patient = null;

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
		}

		exploration.setPatient(patient);

		if (explorationEditionData.getReport() != null) {
			ReportEditionData reportEditionData = explorationEditionData.getReport();
			Report report = new Report(reportEditionData.getReportN(),
				reportEditionData.getCompletionDate(),
				reportEditionData.getApplicant(),
				reportEditionData.getPriority(),
				reportEditionData.getStatus(),
				reportEditionData.getBed(),
				reportEditionData.getClinicalData(),
				reportEditionData.getFindings(),
				reportEditionData.getConclusions());

			report.setExploration(exploration);
			report = this.reportService.create(report);
			exploration.setReport(report);

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
		}

		Set<RadiographEditionData> radiographEditionDataList = explorationEditionData.getRadiographs();

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
	}
}
