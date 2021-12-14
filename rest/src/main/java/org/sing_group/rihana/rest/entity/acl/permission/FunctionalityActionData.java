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
package org.sing_group.rihana.rest.entity.acl.permission;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "functionality-action-data", namespace = "http://entity.resource.rest.rihana.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "functionality-action-data", description = "Information of a functionality action.")
public class FunctionalityActionData implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "functionalityId", required = true)
	private int functionalityId;

	@XmlElement(name = "actionId", required = true)
	private int actionId;

	public FunctionalityActionData() { }

	public FunctionalityActionData(int functionalityId, int actionId) {
		this.functionalityId = functionalityId;
		this.actionId = actionId;
	}

	public int getFunctionalityId() {
		return functionalityId;
	}

	public int getActionId() {
		return actionId;
	}
}
