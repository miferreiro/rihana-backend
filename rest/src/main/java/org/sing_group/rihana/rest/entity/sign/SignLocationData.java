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


@XmlRootElement(name = "Sign-location-data", namespace = "http://entity.resource.rest.rihana.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "Sign-location-data", description = "Information of a sign location.")
public class SignLocationData implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "x", required = true)
	private Integer x;

	@XmlElement(name = "y", required = true)
	private Integer y;

	@XmlElement(name = "width", required = true)
	private Integer width;

	@XmlElement(name = "height", required = true)
	private Integer height;

	@XmlElement(name = "brightness", required = true)
	private Integer brightness;

	@XmlElement(name = "contrast", required = true)
	private Integer contrast;

	public SignLocationData() { }

	public SignLocationData(Integer x, Integer y, Integer width, Integer height, Integer brightness, Integer contrast) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.brightness = brightness;
		this.contrast = contrast;
	}

	public Integer getX() {
		return x;
	}

	public Integer getY() {
		return y;
	}

	public Integer getWidth() {
		return width;
	}

	public Integer getHeight() {
		return height;
	}

	public Integer getBrightness() {
		return brightness;
	}

	public Integer getContrast() {
		return contrast;
	}
}
