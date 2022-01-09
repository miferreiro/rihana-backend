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
package org.sing_group.rihana.domain.entities.report;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "explorationcode")
public class ExplorationCode implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "code")
	private String code;

	@Column(name = "description")
	private String description;

	@OneToMany(mappedBy = "explorationCode", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<RequestedExploration> requestedExplorations = new HashSet<>();

	@OneToMany(mappedBy = "explorationCode", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PerformedExploration> performedExplorations = new HashSet<>();

	@Column(name = "deleted", columnDefinition="BIT(1) DEFAULT FALSE")
	private boolean deleted;

	@Column(name = "delete_date", columnDefinition = "DATETIME(3)")
	private Timestamp deleteDate;

	ExplorationCode() { }

	public ExplorationCode(String code, String description) {
		this.code = code;
		this.description = description;
		this.setDeleted(false);
		this.deleteDate = null;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void internalRemoveRequestedExploration(RequestedExploration requestedExploration) {
		this.requestedExplorations.remove(requestedExploration);
	}

	public void internalAddRequestedExploration(RequestedExploration requestedExploration) {
		this.requestedExplorations.add(requestedExploration);
	}

	public Set<RequestedExploration> getRequestedExplorations() {
		return requestedExplorations;
	}

	public void internalRemovePerformedExploration(PerformedExploration performedExploration) {
		this.performedExplorations.remove(performedExploration);
	}

	public void internalAddPerformedExploration(PerformedExploration performedExploration) {
		this.performedExplorations.add(performedExploration);
	}

	public Set<PerformedExploration> getPerformedExplorations() {
		return performedExplorations;
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
