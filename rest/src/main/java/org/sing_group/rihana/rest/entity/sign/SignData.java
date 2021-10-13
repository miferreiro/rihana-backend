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
package org.sing_group.rihana.rest.entity.sign;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "sign-data", namespace = "http://entity.resource.rest.rihana.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "sign-data", description = "Information of a sign.")
public class SignData implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "id", required = true)
	private String id;

	@XmlElement(name = "type", required = true)
	private SignTypeData type;

	@XmlElement(name = "location")
	private SignLocationData location;

	@XmlElement(name = "brightness")
	private Integer brightness;

	@XmlElement(name = "contrast")
	private Integer contrast;

	public SignData() { }

	public SignData(String id, SignTypeData type, SignLocationData location, Integer brightness, Integer contrast) {
		this.id = id;
		this.type = type;
		this.location = location;
		this.brightness = brightness;
		this.contrast = contrast;
	}

	public String getId() {
		return id;
	}

	public SignTypeData getType() {
		return type;
	}

	public SignLocationData getLocation() {
		return location;
	}

	public Integer getBrightness() {
		return brightness;
	}

	public Integer getContrast() {
		return contrast;
	}
}
