/*-
 * #%L
 * Domain
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
package org.sing_group.rihana.domain.entities.acl.permission;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FunctionalityActionKey implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "functionality_id")
	private int functionalityId;

	@Column(name = "action_id")
	private int actionId;

	public FunctionalityActionKey() { }

	public FunctionalityActionKey(int functionalityId, int actionId) {
		this.setFunctionalityId(functionalityId);
		this.setActionId(actionId);
	}

	public int getFunctionalityId() {
		return functionalityId;
	}

	public void setFunctionalityId(int functionalityId) {
		this.functionalityId = functionalityId;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FunctionalityActionKey that = (FunctionalityActionKey) o;
		return Objects.equals(functionalityId, that.functionalityId) && Objects.equals(actionId, that.actionId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(functionalityId, actionId);
	}
}
