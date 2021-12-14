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
public class PermissionKey implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "role_id")
	private int roleId;

	@Column(name = "functionalityaction_id")
	private FunctionalityActionKey functionalityActionId;

	public PermissionKey() { }

	public PermissionKey(int roleId, FunctionalityActionKey functionalityActionId) {
		this.roleId = roleId;
		this.functionalityActionId = functionalityActionId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public FunctionalityActionKey getFunctionalityActionId() {
		return functionalityActionId;
	}

	public void setFunctionalityActionId(FunctionalityActionKey functionalityActionId) {
		this.functionalityActionId = functionalityActionId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PermissionKey that = (PermissionKey) o;
		return Objects.equals(roleId, that.roleId) && Objects.equals(functionalityActionId, that.functionalityActionId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(roleId, functionalityActionId);
	}
}
