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
package org.sing_group.rihana.domain.entities.exploration;

import static java.util.Objects.requireNonNull;
import static org.sing_group.fluent.checker.Checks.checkArgument;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.sing_group.rihana.domain.entities.Identifiable;
import org.sing_group.rihana.domain.entities.patient.Patient;
import org.sing_group.rihana.domain.entities.radiograph.Radiograph;
import org.sing_group.rihana.domain.entities.report.Report;
import org.sing_group.rihana.domain.entities.user.User;

@Entity
@Table(name = "exploration", uniqueConstraints = @UniqueConstraint(columnNames = {
	"title"
}))
public class Exploration implements Identifiable {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "date", nullable = false)
	private Timestamp date;

	@ManyToOne
	private User user;

	@ManyToOne
	private Patient patient;

	@OneToOne(mappedBy = "exploration", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private Report report;

	@OneToMany(mappedBy = "exploration", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Radiograph> radiographs = new ArrayList<>();

	@Column(name = "creation_date", columnDefinition = "DATETIME(3)")
	private Timestamp creationDate;

	@Version
	@Column(name = "update_date", columnDefinition = "DATETIME(3)")
	private Timestamp updateDate;

	@Column(name = "deleted", columnDefinition="BIT(1) DEFAULT FALSE")
	private boolean deleted;

	@Column(name = "delete_date", columnDefinition = "DATETIME(3)")
	private Timestamp deleteDate;

	Exploration() { }

	public Exploration(String title, Date date, User user, Patient patient, Report report) {
		id = UUID.randomUUID().toString();
		this.setTitle(title);
		this.setDate(date);
		this.setUser(user);
		this.setPatient(patient);
		this.setReport(report);
		this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
		this.setDeleted(false);
		this.deleteDate = null;
	}

	public Exploration(String title, User user, Patient patient, Report report, List<Radiograph> radiographs) {
		id = UUID.randomUUID().toString();
		this.setTitle(title);
		this.setUser(user);
		this.setPatient(patient);
		this.setReport(report);
		this.radiographs = radiographs;
		this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
		this.setDeleted(false);
		this.deleteDate = null;
	}

	@Override
	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		checkArgument(title, t -> requireNonNull(t, "title cannot be null"));
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		checkArgument(date, d -> requireNonNull(d, "exploration date cannot be null"));
		this.date = new Timestamp(date.getTime());
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		if (this.user != null) {
			this.user.internalRemoveExploration(this);
		}
		this.user = user;
		if (user != null) {
			this.user.internalAddExploration(this);
		}
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		if (this.patient != null) {
			this.patient.internalRemoveExploration(this);
		}
		this.patient = patient;
		if (patient != null) {
			this.patient.internalAddExploration(this);
		}
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public void internalRemoveRadiograph(Radiograph radiograph) {
		this.radiographs.remove(radiograph);
	}

	public void internalAddRadiograph(Radiograph radiograph) {
		this.radiographs.add(radiograph);
	}

	public List<Radiograph> getRadiographs() {
		return radiographs;
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
