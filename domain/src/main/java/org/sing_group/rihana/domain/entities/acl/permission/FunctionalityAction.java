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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.rihana.domain.entities.acl.action.Action;
import org.sing_group.rihana.domain.entities.acl.functionality.Functionality;

@Entity
@Table(name = "functionalityaction")
@XmlRootElement(name = "functionalityaction")
@XmlAccessorType(XmlAccessType.FIELD)
public class FunctionalityAction implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FunctionalityActionKey id;

	@ManyToOne
	@JoinColumn(name = "functionality_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Functionality functionality;

	@ManyToOne
	@JoinColumn(name = "action_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Action action;

	@OneToMany(mappedBy = "functionalityAction", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Permission> permissions = new HashSet<>();

	@Column(name = "creation_date", columnDefinition = "DATETIME(3)")
	private Timestamp creationDate;

	@Version
	@Column(name = "update_date", columnDefinition = "DATETIME(3)")
	private Timestamp updateDate;

	@Column(name = "deleted", columnDefinition="BIT(1) DEFAULT FALSE")
	private boolean deleted;

	@Column(name = "delete_date", columnDefinition = "DATETIME(3)")
	private Timestamp deleteDate;

	FunctionalityAction() { }

	public FunctionalityAction(FunctionalityActionKey functionalityActionKey) {
		this.id = functionalityActionKey;
		this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
		this.setDeleted(false);
		this.deleteDate = null;
	}

	public FunctionalityActionKey getFunctionalityActionKey() {
		return id;
	}

	public Functionality getFunctionality() {
		return functionality;
	}

	public void setFunctionality(Functionality functionality) {
		this.functionality = functionality;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public void internalRemovePermission(Permission permission) {
		permissions.remove(permission);
	}

	public void internalAddPermission(Permission permission) {
		permissions.add(permission);
	}

	public Set<Permission> getPermissions() {
		return permissions;
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
