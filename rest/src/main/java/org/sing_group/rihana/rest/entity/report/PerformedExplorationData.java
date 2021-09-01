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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "Performed-exploration-data", namespace = "http://entity.resource.rest.rihana.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "Performed-exploration-data", description = "Information of a performed exploration.")
public class PerformedExplorationData implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "id", required = true)
	private String id;

	@XmlElement(name = "date", required = true)
	private Date date;

	@XmlElement(name = "portable", required = true)
	private String portable;

	@XmlElement(name = "surgery", required = true)
	private String surgery;

	@XmlElement(name = "code", required = true)
	private String code;

	public PerformedExplorationData() { }

	public PerformedExplorationData(String id, Date date, String portable, String surgery, String code) {
		this.id = id;
		this.date = date;
		this.portable = portable;
		this.surgery = surgery;
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public String getPortable() {
		return portable;
	}

	public String getSurgery() {
		return surgery;
	}

	public String getCode() {
		return code;
	}
}