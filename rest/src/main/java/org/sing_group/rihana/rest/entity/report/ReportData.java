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
package org.sing_group.rihana.rest.entity.report;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import org.sing_group.rihana.rest.entity.UuidAndUri;

@XmlRootElement(name = "report-data", namespace = "http://entity.resource.rest.rihana.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "report-data", description = "Information of a report.")
public class ReportData implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "id", required = true)
	private String id;

	@XmlElement(name = "reportNumber", required = true)
	private String reportN;

	@XmlElement(name = "completionDate", required = true)
	private Date completion_date;

	@XmlElement(name = "applicant", required = true)
	private String applicant;

	@XmlElement(name = "priority", required = true)
	private String priority;

	@XmlElement(name = "status", required = true)
	private String status;

	@XmlElement(name = "bed", required = true)
	private String bed;

	@XmlElement(name = "clinicalData", required = true)
	private String clinical_data;

	@XmlElement(name = "findings", required = true)
	private String findings;

	@XmlElement(name = "conclusions", required = true)
	private String conclusions;

	@XmlElement(name = "exploration", required = true)
	private UuidAndUri exploration;

	@XmlElement(name = "requestedExplorations")
	private List<RequestedExplorationData> requestedExplorations;

	@XmlElement(name = "performedExplorations")
	private List<PerformedExplorationData> performedExplorations;

	public ReportData() { }

	public ReportData(String id, String reportN, Date completion_date, String applicant, String priority,
					  String status, String bed, String clinical_data, String findings, String conclusions,
					  UuidAndUri exploration, List<RequestedExplorationData> requestedExplorations,
					  List<PerformedExplorationData> performedExplorations) {
		this.id = id;
		this.reportN = reportN;
		this.completion_date = completion_date;
		this.applicant = applicant;
		this.priority = priority;
		this.status = status;
		this.bed = bed;
		this.clinical_data = clinical_data;
		this.findings = findings;
		this.conclusions = conclusions;
		this.exploration = exploration;
		this.requestedExplorations = requestedExplorations;
		this.performedExplorations = performedExplorations;
	}

	public String getId() {
		return id;
	}

	public String getReportN() {
		return reportN;
	}

	public Date getCompletion_date() {
		return completion_date;
	}

	public String getApplicant() {
		return applicant;
	}

	public String getPriority() {
		return priority;
	}

	public String getStatus() {
		return status;
	}

	public String getBed() {
		return bed;
	}

	public String getClinical_data() {
		return clinical_data;
	}

	public String getFindings() {
		return findings;
	}

	public String getConclusions() {
		return conclusions;
	}

	public UuidAndUri getExploration() {
		return exploration;
	}

	public List<RequestedExplorationData> getRequestedExplorations() {
		return requestedExplorations;
	}

	public List<PerformedExplorationData> getPerformedExplorations() {
		return performedExplorations;
	}
}
