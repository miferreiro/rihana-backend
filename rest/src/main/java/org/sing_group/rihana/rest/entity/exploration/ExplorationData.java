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
package org.sing_group.rihana.rest.entity.exploration;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import org.sing_group.rihana.domain.entities.user.User;
import org.sing_group.rihana.rest.entity.UuidAndUri;

@XmlRootElement(name = "Exploration-data", namespace = "http://entity.resource.rest.rihana.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "Exploration-data", description = "Information of an exploration.")
public class ExplorationData implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "id", required = true)
	private String id;

	@XmlElement(name = "title", required = true)
	private String title;

	@XmlElement(name = "explorationDate", required = true)
	private Date explorationDate;

	@XmlElement(name = "user")
	private String user;

	@XmlElement(name = "patient")
	private UuidAndUri patient;

	@XmlElement(name = "report")
	private UuidAndUri report;

	@XmlElement(name = "radiographies")
	private List<UuidAndUri> radiographies;

	public ExplorationData() { }

	public ExplorationData(String id, String title, Date explorationDate, User user, UuidAndUri patient, UuidAndUri report, List<UuidAndUri> radiographies) {
		this.id = id;
		this.title = title;
		this.explorationDate = explorationDate;
		this.user = user.getLogin();
		this.patient = patient;
		this.report = report;
		this.radiographies = radiographies;
	}

	public String getId() {
		return this.id;
	}

	public String getTitle() {
		return this.title;
	}

	public Date getExplorationDate() {
		return this.explorationDate;
	}

	public String getUser() {
		return this.user;
	}

	public UuidAndUri getPatient() {
		return this.patient;
	}

	public UuidAndUri getReport() {
		return this.report;
	}

	public List<UuidAndUri> getRadiographies() {
		return this.radiographies;
	}
}
