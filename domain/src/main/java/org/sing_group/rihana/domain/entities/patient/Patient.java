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
package org.sing_group.rihana.domain.entities.patient;

import static java.util.Objects.requireNonNull;
import static org.sing_group.fluent.checker.Checks.checkArgument;

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
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
@Table(name = "patient", uniqueConstraints = @UniqueConstraint(columnNames = {
	"patientID"
}))
@XmlRootElement(name = "patient")
@XmlAccessorType(XmlAccessType.FIELD)
public class Patient implements Identifiable {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "patientID", nullable = false)
	private String patientID;

	@Enumerated(EnumType.STRING)
	@Column(name = "sex")
	private SEX sex;

	public enum SEX {
		MALE("Male"),
		FEMALE("Female");

		public final String label;

		SEX(String label) {
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

	@Column(name = "birthdate")
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp birthdate;

	@XmlTransient
	@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
	private Set<Exploration> explorations = new HashSet<>();

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

	Patient() { }

	public Patient(String patientID, SEX sex, Date birthdate) {
		this.id = UUID.randomUUID().toString();
		this.setPatientID(patientID);
		this.setSex(sex);
		this.setBirthdate(birthdate);
		this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
		this.setDeleted(false);
		this.deleteDate = null;
	}

	@Override
	public String getId() {
		return id;
	}

	public String getPatientID() {
		return patientID;
	}

	public void setPatientID(String publicID) {
		checkArgument(publicID, p -> requireNonNull(p, "patient identifier cannot be null"));
		this.patientID = publicID;
	}

	public SEX getSex() {
		return sex;
	}

	public void setSex(SEX sex) {
		this.sex = sex;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		if (birthdate == null) {
			this.birthdate = null;
		} else {
			this.birthdate = new Timestamp(birthdate.getTime());
		}
	}

	public void internalRemoveExploration(Exploration exploration) {
		this.explorations.remove(exploration);
	}

	public void internalAddExploration(Exploration exploration) {
		this.explorations.add(exploration);
	}

	public Set<Exploration> getExplorations() {
		return explorations;
	}

	public void addExploration(Exploration exploration) {
		exploration.setPatient(this);
	}

	public void removeExploration(Exploration exploration) {
		exploration.setPatient(null);
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
