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
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.sing_group.rihana.domain.entities.TimestampAdapter;
import org.sing_group.rihana.domain.entities.acl.role.Role;

@Entity
@Table(name = "permission")
@XmlRootElement(name = "permission")
@XmlAccessorType(XmlAccessType.FIELD)
public class Permission implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PermissionKey id;

	@ManyToOne
	@JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Role role;

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "functionality_id", referencedColumnName = "functionality_id", insertable = false, updatable = false),
		@JoinColumn(name = "action_id", referencedColumnName = "action_id", insertable = false, updatable = false)
	})
	private FunctionalityAction functionalityAction;

	@Column(name = "creation_date", columnDefinition = "DATETIME(3)")
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp creationDate;

	@Version
	@Column(name = "update_date", columnDefinition = "DATETIME(3)")
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp updateDate;

	@Column(name = "deleted", columnDefinition="BIT(1) DEFAULT FALSE")
	private boolean deleted;

	@Column(name = "delete_date", columnDefinition = "DATETIME(3)")
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp deleteDate;

	Permission() { }

	public Permission(PermissionKey permissionKey) {
		this.id = permissionKey;
		this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
		this.setDeleted(false);
		this.deleteDate = null;
	}

	public PermissionKey getPermissionKey() {
		return id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public FunctionalityAction getFunctionalityAction() {
		return functionalityAction;
	}

	public void setFunctionalityAction(FunctionalityAction functionalityAction) {
		this.functionalityAction = functionalityAction;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
		if(this.deleted) {
			this.deleteDate = new Timestamp(System.currentTimeMillis());
		} else {
			this.deleteDate = null;
		}
	}
}
