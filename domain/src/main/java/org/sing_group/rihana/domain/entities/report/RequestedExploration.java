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
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.sing_group.rihana.domain.entities.Identifiable;

@Entity
@Table(name = "requestedexploration")
@XmlRootElement(name = "requestedexploration")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestedExploration implements Identifiable {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "date")
	private Timestamp date;

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "report_id", referencedColumnName = "id")
	private Report report;

	@ManyToOne
	@JoinColumn(name = "exploration_code")
	private ExplorationCode explorationCode;

	RequestedExploration() { }

	public RequestedExploration(Date date, Report report, ExplorationCode explorationCode) {
		this.id = UUID.randomUUID().toString();
		this.setDate(date);
		this.setReport(report);
		this.setExplorationCode(explorationCode);
	}

	@Override
	public String getId() {
		return id;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = new Timestamp(date.getDate());
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		if (this.report != null) {
			this.report.internalRemoveRequestedExploration(this);
		}
		this.report = report;
		if (report != null) {
			this.report.internalAddRequestedExploration(this);
		}
	}

	public ExplorationCode getExplorationCode() {
		return explorationCode;
	}

	public void setExplorationCode(ExplorationCode explorationCode) {
		this.explorationCode = explorationCode;
	}
}
