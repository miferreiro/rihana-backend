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
package org.sing_group.rihana.domain.entities.radiograph;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.sing_group.rihana.domain.entities.Identifiable;
import org.sing_group.rihana.domain.entities.TimestampAdapter;
import org.sing_group.rihana.domain.entities.exploration.Exploration;
import org.sing_group.rihana.domain.entities.sign.Sign;

@Entity
@Table(name = "radiograph")
@XmlRootElement(name = "radiograph")
@XmlAccessorType(XmlAccessType.FIELD)
public class Radiograph implements Identifiable {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "source")
	private String source;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private RadiographType type;

	@Column(name = "observations")
	private String observations;

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "exploration_id", referencedColumnName = "id")
	private Exploration exploration;

	@OneToMany(mappedBy = "radiograph", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Sign> signs = new HashSet<>();

	@Column(name = "creation_date", columnDefinition = "DATETIME(3)")
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp creationDate;

	@Version
	@Column(name = "update_date", columnDefinition = "DATETIME(3)")
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp updateDate;

	@Column(name = "deleted", columnDefinition = "BIT(1) DEFAULT FALSE")
	private boolean deleted;

	@Column(name = "delete_date", columnDefinition = "DATETIME(3)")
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp deleteDate;

	Radiograph() { }

	public Radiograph(RadiographType type, String observations) {
		this.id = UUID.randomUUID().toString();
		this.setSource("");
		this.setType(type);
		this.setObservations(observations);
		this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
		this.setDeleted(false);
		this.deleteDate = null;
	}

	public Radiograph(RadiographType type, String observations, Exploration exploration) {
		this.id = UUID.randomUUID().toString();
		this.setSource("");
		this.setType(type);
		this.setObservations(observations);
		this.setExploration(exploration);
		this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
		this.setDeleted(false);
		this.deleteDate = null;
	}

	public Radiograph(String source, RadiographType type, String observations) {
		this.id = UUID.randomUUID().toString();
		this.setSource(source);
		this.setType(type);
		this.setObservations(observations);
		this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
		this.setDeleted(false);
		this.deleteDate = null;
	}

	@Override
	public String getId() {
		return id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public RadiographType getType() {
		return type;
	}

	public void setType(RadiographType type) {
		this.type = type;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public Exploration getExploration() {
		return exploration;
	}

	public void setExploration(Exploration exploration) {
		if (this.exploration != null) {
			this.exploration.internalRemoveRadiograph(this);
		}
		this.exploration = exploration;
		if (exploration != null) {
			this.exploration.internalAddRadiograph(this);
		}
	}

	public void internalRemoveSign(Sign sign) {
		this.signs.remove(sign);
	}

	public void internalAddSign(Sign sign) {
		this.signs.add(sign);
	}

	public Set<Sign> getSigns() {
		return signs;
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
