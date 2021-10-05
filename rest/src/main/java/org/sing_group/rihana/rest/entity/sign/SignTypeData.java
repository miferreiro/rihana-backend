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

@XmlRootElement(name = "sign-type-data", namespace = "http://entity.resource.rest.rihana.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "sign-type-data", description = "Information of a sign type.")
public class SignTypeData implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "code", required = true)
	private String code;

	@XmlElement(name = "name", required = true)
	private String name;

	@XmlElement(name = "description", required = true)
	private String description;

	@XmlElement(name = "target", required = true)
	private Integer target;

	public SignTypeData() { }

	public SignTypeData(String code, String name, String description, Integer target) {
		this.code = code;
		this.name = name;
		this.description = description;
		this.target = target;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Integer getTarget() {
		return target;
	}
}
