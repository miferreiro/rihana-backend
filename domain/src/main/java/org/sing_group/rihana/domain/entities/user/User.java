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
package org.sing_group.rihana.domain.entities.user;

import static java.util.Objects.requireNonNull;
import static org.sing_group.fluent.checker.Checks.checkArgument;
import static org.sing_group.fluent.checker.Checks.requirePattern;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.sing_group.rihana.domain.entities.TimestampAdapter;
import org.sing_group.rihana.domain.entities.acl.role.Role;
import org.sing_group.rihana.domain.entities.exploration.Exploration;

@Entity
@Table(name = "user")
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 100, nullable = false, unique = true)
	private String login;

	@Column(length = 32, nullable = false)
	private String password;

	@ManyToOne(optional = false)
	private Role role;

	@XmlTransient
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Exploration> explorations = new HashSet<>();

	@Column(name = "deleted", columnDefinition="BIT(1) DEFAULT FALSE")
	private boolean deleted;

	@Column(name = "delete_date", columnDefinition = "DATETIME(3)")
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp deleteDate;

	User() { }

	public User(String login, String password) {
		this.setLogin(login);
		this.setPassword(password);
		this.setDeleted(false);
		this.deleteDate = null;
	}

	public String getLogin() {
		return login;
	}

	void setLogin(String login) {
		this.login =
			requirePattern(
				login, "[a-zA-ZñÑ0-9_]{1,100}",
				"'login' can only contain letters, numbers or underscore and should have a length between 1 and 100");
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		checkArgument(password, p -> requireNonNull(p, "password cannot be null"));
		if (password.length() < 6)
			throw new IllegalArgumentException("password can't be shorter than 6");
		try {
			final MessageDigest digester = MessageDigest.getInstance("MD5");
			final HexBinaryAdapter adapter = new HexBinaryAdapter();

			this.password = adapter.marshal(digester.digest(password.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 algorithm not found", e);
		}
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		if (this.role != null) {
			this.role.internalRemoveUser(this);
		}
		this.role = role;
		if (role != null) {
			this.role.internalAddUser(this);
		}
	}

	public void internalRemoveExploration(Exploration exploration) {
		this.explorations.remove(exploration);
	}

	public void internalAddExploration(Exploration exploration) {
		this.explorations.add(exploration);
	}

	public Set<Exploration> getExplorations() {
		return explorations;
	}

	public void addExploration(Exploration exploration) {
		exploration.setUser(this);
	}

	public void removeExploration(Exploration exploration) {
		exploration.setUser(null);
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
