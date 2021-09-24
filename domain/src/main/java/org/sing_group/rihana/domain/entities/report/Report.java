/*-
 * #%L
 * Domain
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
package org.sing_group.rihana.domain.entities.report;

import static java.util.Objects.requireNonNull;
import static org.sing_group.fluent.checker.Checks.checkArgument;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.sing_group.rihana.domain.entities.Identifiable;
import org.sing_group.rihana.domain.entities.exploration.Exploration;

@Entity
@Table(name = "report", uniqueConstraints = @UniqueConstraint(columnNames = {
	"reportN"
}))
public class Report implements Identifiable {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "reportN", nullable = false, unique = true)
	private String reportN;

	@Column(name = "completion_date")
	private Timestamp completionDate;

	@Column(name = "applicant")
	private String applicant;

	@Column(name = "priority")
	private String priority;

	@Column(name = "status")
	private String status;

	@Column(name = "bed")
	private String bed;

	@Column(name = "clinical_data")
	private String clinicalData;

	@Column(name = "findings")
	private String findings;

	@Column(name = "conclusions")
	private String conclusions;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Exploration exploration;

	@OneToMany(mappedBy = "report", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RequestedExploration> requestedExplorations = new ArrayList<>();

	@OneToMany(mappedBy = "report", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PerformedExploration> performedExplorations = new ArrayList<>();

	@Column(name = "creation_date", columnDefinition = "DATETIME(3)")
	private Timestamp creationDate;

	@Version
	@Column(name = "update_date", columnDefinition = "DATETIME(3)")
	private Timestamp updateDate;

	@Column(name = "deleted", columnDefinition="BIT(1) DEFAULT FALSE")
	private boolean deleted;

	@Column(name = "delete_date", columnDefinition = "DATETIME(3)")
	private Timestamp deleteDate;

	Report() { }

	public Report(String reportN, Timestamp completionDate, String applicant, String priority, String status,
				  String bed, String clinicalData, String findings, String conclusions, Exploration exploration) {
		this.id = UUID.randomUUID().toString();
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
		this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
		this.setDeleted(false);
		this.deleteDate = null;
	}

	@Override
	public String getId() {
		return id;
	}

	public String getReportN() {
		return reportN;
	}

	public void setReportN(String reportN) {
		checkArgument(reportN, t -> requireNonNull(t, "reportN cannot be null"));
		this.reportN = reportN;
	}

	public Timestamp getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Timestamp completionDate) {
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

	public Exploration getExploration() {
		return exploration;
	}

	public void setExploration(Exploration exploration) {
		this.exploration = exploration;
	}

	public void internalRemoveRequestedExploration(RequestedExploration requestedExploration) {
		this.requestedExplorations.remove(requestedExploration);
	}

	public void internalAddRequestedExploration(RequestedExploration requestedExploration) {
		this.requestedExplorations.add(requestedExploration);
	}

	public List<RequestedExploration> getRequestedExplorations() {
		return requestedExplorations;
	}

	public void internalRemovePerformedExploration(PerformedExploration performedExploration) {
		this.performedExplorations.remove(performedExploration);
	}

	public void internalAddPerformedExploration(PerformedExploration performedExploration) {
		this.performedExplorations.add(performedExploration);
	}

	public List<PerformedExploration> getPerformedExplorations() {
		return performedExplorations;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
