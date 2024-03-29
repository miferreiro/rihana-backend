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
package org.sing_group.rihana.domain.dao.user;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.rihana.domain.dao.DAOHelper;
import org.sing_group.rihana.domain.dao.spi.user.UserDAO;
import org.sing_group.rihana.domain.entities.user.User;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultUserDAO implements UserDAO {

	@PersistenceContext
	private EntityManager em;

	private DAOHelper<String, User> dh;

	DefaultUserDAO() {}

	public DefaultUserDAO(EntityManager em) {
		this.em = em;
		createDAOHelper();
	}

	@PostConstruct
	private void createDAOHelper() {
		this.dh = DAOHelper.of(String.class, User.class, this.em);
	}

	@Override
	public User get(String login) {
		return dh.get(login).filter(e -> !e.isDeleted())
			.orElseThrow(() -> new IllegalArgumentException("Unknown user: " + login));
	}

	@Override
	public User create(User user) {
		return this.dh.persist(user);
	}

	@Override
	public User edit(User user) {
		return this.dh.update(user);
	}

	@Override
	public void delete(User user) {
		user.setDeleted(true);
		this.dh.update(user);
	}

	@Override
	public Stream<User> getUsers() {
		return this.dh.listBy("deleted", 0).stream();
	}
}
