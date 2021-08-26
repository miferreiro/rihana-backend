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
package org.sing_group.rihana.rest.entity.radiography;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import org.sing_group.rihana.domain.entities.radiography.RadiographyType;
import org.sing_group.rihana.rest.entity.sign.SignData;
import org.sing_group.rihana.rest.entity.UuidAndUri;

@XmlRootElement(name = "Radiography-data", namespace = "http://entity.resource.rest.rihana.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "Radiography-data", description = "Information of a radiography.")
public class RadiographyData implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "id", required = true)
	private String id;

	@XmlElement(name = "source", required = true)
	private String source;

	@XmlElement(name = "type", required = true)
	private RadiographyType type;

	@XmlElement(name = "exploration")
	private UuidAndUri exploration;

	@XmlElement(name = "signs")
	private List<SignData> signs;

	public RadiographyData() { }

	public RadiographyData(String id, String source, RadiographyType type, UuidAndUri exploration, List<SignData> signs) {
		this.id = id;
		this.source = source;
		this.type = type;
		this.exploration = exploration;
		this.signs = signs;
	}

	public String getId() {
		return id;
	}

	public String getSource() {
		return source;
	}

	public RadiographyType getType() {
		return type;
	}

	public UuidAndUri getExploration() {
		return exploration;
	}

	public List<SignData> getSigns() {
		return signs;
	}
}
