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
package org.sing_group.rihana.rest.entity.patient;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import org.sing_group.rihana.domain.entities.patient.Patient.SEX;

@XmlRootElement(name = "patient-data", namespace = "http://entity.resource.rest.rihana.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "patient-data", description = "Information of a patient.")
public class PatientData {

	@XmlElement(name = "id", required = true)
	private String id;
	@XmlElement(name = "patientID", required = true)
	private String patientID;
	@XmlElement(name = "sex")
	private SEX sex;
	@XmlElement(name = "birthdate")
	private Date birthdate;

	public PatientData() {}

	public PatientData(String id, String patientID, SEX sex, Date birthdate) {
		this.id = id;
		this.patientID = patientID;
		this.sex = sex;
		this.birthdate = birthdate;
	}

	public String getId() {
		return id;
	}

	public String getPatientID() {
		return patientID;
	}

	public SEX getSex() {
		return sex;
	}

	public Date getBirthdate() {
		return birthdate;
	}
}
