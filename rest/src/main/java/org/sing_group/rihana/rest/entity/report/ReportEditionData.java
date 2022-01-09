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

@XmlRootElement(name = "report-edition-data", namespace = "http://entity.resource.rest.rihana.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "report-edition-data", description = "Information of a report for edition.")
public class ReportEditionData implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "reportNumber", required = true)
	private String reportN;

	@XmlElement(name = "completionDate", required = true)
	private Date completionDate;

	@XmlElement(name = "applicant", required = true)
	private String applicant;

	@XmlElement(name = "priority", required = true)
	private String priority;

	@XmlElement(name = "status", required = true)
	private String status;

	@XmlElement(name = "bed", required = true)
	private String bed;

	@XmlElement(name = "clinicalData", required = true)
	private String clinicalData;

	@XmlElement(name = "findings", required = true)
	private String findings;

	@XmlElement(name = "conclusions", required = true)
	private String conclusions;

	@XmlElement(name = "exploration", required = true)
	private UuidAndUri exploration;

	@XmlElement(name = "requestedExplorations")
	private List<RequestedExplorationEditionData> requestedExplorations;

	@XmlElement(name = "performedExplorations")
	private List<PerformedExplorationEditionData> performedExplorations;

	public ReportEditionData() { }

	public ReportEditionData(String reportN, Date completionDate, String applicant, String priority,
							  String status, String bed, String clinicalData, String findings, String conclusions,
							  UuidAndUri exploration, List<RequestedExplorationEditionData> requestedExplorations,
							  List<PerformedExplorationEditionData> performedExplorations) {

		this.reportN = reportN;
		this.completionDate = completionDate;
		this.applicant = applicant;
		this.priority = priority;
		this.status = status;
		this.bed = bed;
		this.clinicalData = clinicalData;
		this.findings = findings;
		this.conclusions = conclusions;
		this.exploration = exploration;
		this.requestedExplorations = requestedExplorations;
		this.performedExplorations = performedExplorations;
	}

	public String getReportN() {
		return reportN;
	}

	public void setReportN(String reportN) {
		this.reportN = reportN;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBed() {
		return bed;
	}

	public void setBed(String bed) {
		this.bed = bed;
	}

	public String getClinicalData() {
		return clinicalData;
	}

	public void setClinicalData(String clinicalData) {
		this.clinicalData = clinicalData;
	}

	public String getFindings() {
		return findings;
	}

	public void setFindings(String findings) {
		this.findings = findings;
	}

	public String getConclusions() {
		return conclusions;
	}

	public void setConclusions(String conclusions) {
		this.conclusions = conclusions;
	}

	public UuidAndUri getExploration() {
		return exploration;
	}

	public void setExploration(UuidAndUri exploration) {
		this.exploration = exploration;
	}

	public List<RequestedExplorationEditionData> getRequestedExplorations() {
		return requestedExplorations;
	}

	public void setRequestedExplorations(List<RequestedExplorationEditionData> requestedExplorations) {
		this.requestedExplorations = requestedExplorations;
	}

	public List<PerformedExplorationEditionData> getPerformedExplorations() {
		return performedExplorations;
	}

	public void setPerformedExplorations(List<PerformedExplorationEditionData> performedExplorations) {
		this.performedExplorations = performedExplorations;
	}
}
