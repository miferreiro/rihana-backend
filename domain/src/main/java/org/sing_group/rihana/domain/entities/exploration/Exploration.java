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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.sing_group.rihana.domain.entities.Identifiable;
import org.sing_group.rihana.domain.entities.TimestampAdapter;
import org.sing_group.rihana.domain.entities.patient.Patient;
import org.sing_group.rihana.domain.entities.radiograph.Radiograph;
import org.sing_group.rihana.domain.entities.report.Report;
import org.sing_group.rihana.domain.entities.user.User;

@Entity
@Table(name = "exploration", uniqueConstraints = @UniqueConstraint(columnNames = {
	"title"
}))
@XmlRootElement(name = "exploration")
@XmlAccessorType(XmlAccessType.FIELD)
public class Exploration implements Identifiable {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "date", nullable = false)
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp date;

	@Enumerated(EnumType.STRING)
	@Column(name = "source")
	private Source source;

	public enum Source {
		SERGAS("SERGAS"),
		PADCHEST("PADCHEST");

		public final String label;

		Source(String label) {
			this.label = label;
		}

		public String getSource(){
			return label;
		}

		public String toString() {
			return this.label;
		}

		public String getString() {
			return this.label;
		}
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_login", columnDefinition = "login")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = true)
	private Patient patient;

	@OneToMany(mappedBy = "exploration", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Report> reports = new HashSet<>();

	@OneToMany(mappedBy = "exploration", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Radiograph> radiographs = new HashSet<>();

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

	Exploration() { }

	public Exploration(String title, Date date, Source source, User user, Patient patient) {
		id = UUID.randomUUID().toString();
		this.setTitle(title);
		this.setDate(date);
		this.setSource(source);
		this.setUser(user);
		this.setPatient(patient);
		this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
		this.setDeleted(false);
		this.deleteDate = null;
	}

	public Exploration(String title, Date date, Source source, User user, Patient patient, Report report, Set<Radiograph> radiographs) {
		id = UUID.randomUUID().toString();
		this.setTitle(title);
		this.setDate(date);
		this.setSource(source);
		this.setUser(user);
		this.setPatient(patient);
		this.reports.add(report);
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

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
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

	public void internalRemoveReport(Report report) {
		reports.remove(report);
	}

	public void internalAddReport(Report report) {
		reports.add(report);
	}

	public Set<Report> getReports() {
		return reports;
	}

	public Report getCurrentReport() {

		if (reports.isEmpty()) {
			return null;
		} else {
			return reports.stream().filter(report -> !report.isDeleted()).findFirst().get();
		}
	}

	public void internalRemoveRadiograph(Radiograph radiograph) {
		radiographs.remove(radiograph);
	}

	public void internalAddRadiograph(Radiograph radiograph) {
		radiographs.add(radiograph);
	}

	public Set<Radiograph> getRadiographs() {
		return radiographs;
	}

	public Set<Radiograph> getCurrentRadiographs() {
		return radiographs.stream().filter(radiograph -> !radiograph.isDeleted()).collect(Collectors.toSet());
	}

	public void setRadiographs(Set<Radiograph> radiographs) {
		this.radiographs = radiographs;
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
