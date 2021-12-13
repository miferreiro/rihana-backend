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
package org.sing_group.rihana.rest.entity.exploration;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import org.sing_group.rihana.rest.entity.patient.PatientEditionData;
import org.sing_group.rihana.rest.entity.radiograph.RadiographEditionData;
import org.sing_group.rihana.rest.entity.report.ReportEditionData;

@XmlRootElement(name = "exploration-edition-data", namespace = "http://entity.resource.rest.rihana.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "exploration-edition-data", description = "Information of an exploration for edition.")
public class ExplorationEditionData implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "id")
	private String id;

	@XmlElement(name = "explorationDate", required = true)
	private Date explorationDate;

	@XmlElement(name = "user", required = true)
	private String user;

	@XmlElement(name = "patient", required = true)
	private PatientEditionData patient;

	@XmlElement(name = "report", required = true)
	private ReportEditionData report;

	@XmlElement(name = "radiographs", required = true)
	private List<RadiographEditionData> radiographs;

	public ExplorationEditionData() { }

	public ExplorationEditionData(Date explorationDate, String user, PatientEditionData patient, List<RadiographEditionData> radiographs) {
		this.setExplorationDate(explorationDate);
		this.setUser(user);
		this.setPatient(patient);
		this.setRadiographs(radiographs);
	}

	public ExplorationEditionData(Date explorationDate, String user, PatientEditionData patient, ReportEditionData reportEditionData, List<RadiographEditionData> radiographs) {
		this.setExplorationDate(explorationDate);
		this.setUser(user);
		this.setPatient(patient);
		this.setReport(reportEditionData);
		this.setRadiographs(radiographs);
	}

	public ExplorationEditionData(String id, Date explorationDate, String user, PatientEditionData patient, ReportEditionData reportEditionData, List<RadiographEditionData> radiographs) {
		this.setId(id);
		this.setExplorationDate(explorationDate);
		this.setUser(user);
		this.setPatient(patient);
		this.setReport(reportEditionData);
		this.setRadiographs(radiographs);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getExplorationDate() {
		return this.explorationDate;
	}

	public void setExplorationDate(Date explorationDate) {
		this.explorationDate = explorationDate;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public PatientEditionData getPatient() {
		return patient;
	}

	public void setPatient(PatientEditionData patient) {
		this.patient = patient;
	}

	public ReportEditionData getReport() {
		return report;
	}

	public void setReport(ReportEditionData report) {
		this.report = report;
	}

	public List<RadiographEditionData> getRadiographs() {
		return radiographs;
	}

	public void setRadiographs(List<RadiographEditionData> radiographs) {
		this.radiographs = radiographs;
	}
}
