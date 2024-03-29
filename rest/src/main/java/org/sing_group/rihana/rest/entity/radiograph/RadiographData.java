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
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import org.sing_group.rihana.domain.entities.radiograph.RadiographType;
import org.sing_group.rihana.rest.entity.UuidAndUri;
import org.sing_group.rihana.rest.entity.sign.SignData;

@XmlRootElement(name = "radiograph-data", namespace = "http://entity.resource.rest.rihana.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "radiograph-data", description = "Information of a radiograph.")
public class RadiographData implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "id", required = true)
	private String id;

	@XmlElement(name = "source", required = true)
	private String source;

	@XmlElement(name = "type", required = true)
	private RadiographType type;

	@XmlElement(name = "observations", required = true)
	private String observations;

	@XmlElement(name = "exploration")
	private UuidAndUri exploration;

	@XmlElement(name = "signs")
	private Set<SignData> signs;

	public RadiographData() { }

	public RadiographData(String id, String source, RadiographType type, String observations, UuidAndUri exploration,
						  Set<SignData> signs) {
		this.id = id;
		this.source = source;
		this.type = type;
		this.observations = observations;
		this.exploration = exploration;
		this.signs = signs;
	}

	public String getId() {
		return id;
	}

	public String getSource() {
		return source;
	}

	public RadiographType getType() {
		return type;
	}

	public String getObservations() {
		return observations;
	}

	public UuidAndUri getExploration() {
		return exploration;
	}

	public Set<SignData> getSigns() {
		return signs;
	}
}
