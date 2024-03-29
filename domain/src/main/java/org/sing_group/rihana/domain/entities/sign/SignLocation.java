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

import static java.util.Objects.requireNonNull;
import static org.sing_group.fluent.checker.Checks.checkArgument;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.sing_group.rihana.domain.entities.Identifiable;
import org.sing_group.rihana.domain.entities.TimestampAdapter;

@Entity
@Table(name = "signlocation")
@XmlRootElement(name = "signlocation")
@XmlAccessorType(XmlAccessType.FIELD)
public class SignLocation implements Identifiable {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "x", nullable = false)
	private int x;

	@Column(name = "y", nullable = false)
	private int y;

	@Column(name = "width", nullable = false)
	private int width;

	@Column(name = "height", nullable = false)
	private int height;

	@Column(name = "creation_date", columnDefinition = "DATETIME(3)")
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp creationDate;

	@XmlTransient
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Sign sign;

	SignLocation() { }

	public SignLocation(int x, int y, int width, int height, Sign sign) {
		this.id = UUID.randomUUID().toString();
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		this.setSign(sign);
		this.creationDate = new Timestamp(System.currentTimeMillis());
	}

	@Override
	public String getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		checkArgument(x, coordinate -> requireNonNull(coordinate, "X can not be null"));
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		checkArgument(y, coordinate -> requireNonNull(coordinate, "Y can not be null"));
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		checkArgument(width, w -> requireNonNull(w, "Width can not be null"));
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		checkArgument(height, h -> requireNonNull(h, "Height can not be null"));
		this.height = height;
	}

	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sign) {
		if (sign == null) {
			if (this.sign != null) {
				this.sign.setSignLocation(null);
			}
		} else {
			sign.setSignLocation(this);
		}
		this.sign = sign;
	}
}
