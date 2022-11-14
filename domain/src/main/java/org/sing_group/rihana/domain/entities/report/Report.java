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

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.sing_group.rihana.domain.entities.Identifiable;
import org.sing_group.rihana.domain.entities.TimestampAdapter;
import org.sing_group.rihana.domain.entities.exploration.Exploration;

@Entity
@Table(name = "report")
@XmlRootElement(name = "report")
@XmlAccessorType(XmlAccessType.FIELD)
public class Report implements Identifiable {

	@Id
	@Column(name = "id")
	private String id;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private Type type;

	public enum Type {
		FILE("FILE"),
		MANUAL("MANUAL");

		public final String label;

		Type(String label) {
			this.label = label;
		}

		public String getSEX(){
			return label;
		}

		public String toString() {
			return this.label;
		}

		public String getString() {
			return this.label;
		}
	}

	@Column(name = "reportN")
	private String reportN;

	@Column(name = "completion_date")
	@XmlJavaTypeAdapter(TimestampAdapter.class)
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

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "exploration_id", referencedColumnName = "id")
	private Exploration exploration;

	@XmlTransient
	@OneToMany(mappedBy = "report", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<RequestedExploration> requestedExplorations = new HashSet<>();

	@XmlTransient
	@OneToMany(mappedBy = "report", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PerformedExploration> performedExplorations = new HashSet<>();

	@Column(name = "creation_date", columnDefinition = "DATETIME(3)")
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp creationDate;

	@Version
	@Column(name = "update_date", columnDefinition = "DATETIME(3)")
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp updateDate;

	@Column(name = "deleted", columnDefinition="BIT(1) DEFAULT FALSE")
	private boolean deleted;

	@Column(name = "delete_date", columnDefinition = "DATETIME(3)")
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp deleteDate;

	Report() { }

	public Report(Type type, String reportN, Date completionDate, String applicant, String priority, String status,
				  String bed, String clinicalData, String findings, String conclusions) {
		this.id = UUID.randomUUID().toString();
		this.setType(type);
		this.setReportN(reportN);
		this.setCompletionDate(completionDate);
		this.setApplicant(applicant);
		this.setPriority(priority);
		this.setStatus(status);
		this.setBed(bed);
		this.setClinicalData(clinicalData);
		this.setFindings(findings);
		this.setConclusions(conclusions);
		this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
		this.setDeleted(false);
		this.deleteDate = null;
	}

	@Override
	public String getId() {
		return id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getReportN() {
		return reportN;
	}

	public void setReportN(String reportN) {
		this.reportN = reportN;
	}

	public Timestamp getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		if (completionDate != null) {
			this.completionDate = new Timestamp(completionDate.getDate());
		} else {
			this.completionDate = null;
		}

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
		if (this.exploration != null) {
			this.exploration.internalRemoveReport(this);
		}
		this.exploration = exploration;
		if (exploration != null) {
			this.exploration.internalAddReport(this);
		}
	}

	public void internalRemoveRequestedExploration(RequestedExploration requestedExploration) {
		this.requestedExplorations.remove(requestedExploration);
	}

	public void internalAddRequestedExploration(RequestedExploration requestedExploration) {
		this.requestedExplorations.add(requestedExploration);
	}

	public Set<RequestedExploration> getRequestedExplorations() {
		return requestedExplorations;
	}

	public void internalRemovePerformedExploration(PerformedExploration performedExploration) {
		this.performedExplorations.remove(performedExploration);
	}

	public void internalAddPerformedExploration(PerformedExploration performedExploration) {
		this.performedExplorations.add(performedExploration);
	}

	public Set<PerformedExploration> getPerformedExplorations() {
		return performedExplorations;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
		if(this.deleted) {
			this.deleteDate = new Timestamp(System.currentTimeMillis());
		} else {
			this.deleteDate = null;
		}
	}
}
