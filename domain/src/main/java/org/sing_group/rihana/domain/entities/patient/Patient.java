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
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.sing_group.rihana.domain.entities.Identifiable;

@Entity
@Table(name = "patient", uniqueConstraints = @UniqueConstraint(columnNames = {
  "patientID"
}))
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
		MALE, FEMALE
	}

	@Column(name = "birthdate")
	private Timestamp birthdate;

	@Column(name = "creation_date", columnDefinition = "DATETIME(3)")
	private Timestamp creationDate;

	@Version
	@Column(name = "update_date", columnDefinition = "DATETIME(3)")
	private Timestamp updateDate;

	Patient() { }

	public Patient(String patientID, SEX sex, Date birthdate) {
		this.id = UUID.randomUUID().toString();
		this.setPatientID(patientID);
		this.setSex(sex);
		this.setBirthdate(birthdate);
		this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
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
		this.birthdate = new Timestamp(birthdate.getTime());
	}
}
