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
package org.sing_group.rihana.domain.entities.sign;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.sing_group.rihana.domain.entities.Identifiable;
import org.sing_group.rihana.domain.entities.radiograph.Radiograph;

@Entity
@Table(name = "sign")
public class Sign implements Identifiable {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "signtype_code")
	private SignType type;

	@ManyToOne
	@JoinColumn(name = "radiograph_id")
	private Radiograph radiograph;

	@OneToOne(mappedBy = "sign", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	private SignLocation signLocation;

	@Column(name = "brightness")
	private int brightness;

	@Column(name = "contrast")
	private int contrast;

	@Column(name = "creation_date", columnDefinition = "DATETIME(3)")
	private Timestamp creationDate;

	@Version
	@Column(name = "update_date", columnDefinition = "DATETIME(3)")
	private Timestamp updateDate;

	@Column(name = "deleted", columnDefinition="BIT(1) DEFAULT FALSE")
	private boolean deleted;

	@Column(name = "delete_date", columnDefinition = "DATETIME(3)")
	private Timestamp deleteDate;

	Sign() { }

	public Sign(SignType type, int brightness, int contrast) {
		this.id = UUID.randomUUID().toString();
		this.type = type;
		this.brightness = brightness;
		this.contrast = contrast;
		this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
		this.setDeleted(false);
		this.deleteDate = null;
	}

	@Override
	public String getId() {
		return id;
	}

	public SignType getType() {
		return type;
	}

	public void setType(SignType type) {
		if (this.type != null) {
			this.type.internalRemoveSign(this);
		}
		this.type = type;
		if (type != null) {
			this.type.internalAddSign(this);
		}
	}

	public SignLocation getSignLocation() {
		return signLocation;
	}

	public void setSignLocation(SignLocation signLocation) {
		this.signLocation = signLocation;
	}

	public int getBrightness() {
		return brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	public int getContrast() {
		return contrast;
	}

	public void setContrast(int contrast) {
		this.contrast = contrast;
	}

	public Radiograph getRadiograph() {
		return radiograph;
	}

	public void setRadiograph(Radiograph radiograph) {
		if (this.radiograph != null) {
			this.radiograph.internalRemoveSign(this);
		}
		this.radiograph = radiograph;
		if (radiograph != null) {
			this.radiograph.internalAddSign(this);
		}
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
