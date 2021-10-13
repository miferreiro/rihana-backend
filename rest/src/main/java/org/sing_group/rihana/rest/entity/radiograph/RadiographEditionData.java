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
package org.sing_group.rihana.rest.entity.radiograph;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import org.sing_group.rihana.domain.entities.radiograph.RadiographType;
import org.sing_group.rihana.rest.entity.sign.SignData;

@XmlRootElement(name = "radiograph-edition-data", namespace = "http://entity.resource.rest.rihana.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "radiograph-edition-data", description = "Information of a radiograph for edition.")
public class RadiographEditionData implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "source", required = true)
	private String source;

	@XmlElement(name = "type", required = true)
	private RadiographType type;

	@XmlElement(name = "observations")
	private String observations;

	@XmlElement(name = "signs")
	private List<SignData> signs;

	public RadiographEditionData() { }

	public RadiographEditionData(String source, RadiographType type, String observations, List<SignData> signs) {
		this.source = source;
		this.type = type;
		this.observations = observations;
		this.signs = signs;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public RadiographType getType() {
		return type;
	}

	public void setType(RadiographType type) {
		this.type = type;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public List<SignData> getSigns() {
		return signs;
	}

	public void setSigns(List<SignData> signs) {
		this.signs = signs;
	}
}
