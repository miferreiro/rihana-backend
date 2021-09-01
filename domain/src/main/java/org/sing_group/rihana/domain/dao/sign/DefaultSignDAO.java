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
package org.sing_group.rihana.domain.dao.sign;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.sing_group.rihana.domain.dao.DAOHelper;
import org.sing_group.rihana.domain.dao.spi.sign.SignDAO;
import org.sing_group.rihana.domain.entities.sign.Sign;
import org.sing_group.rihana.domain.entities.user.User;

@Default
@Transactional(value = Transactional.TxType.MANDATORY)
public class DefaultSignDAO implements SignDAO {

	@PersistenceContext
	protected EntityManager em;

	protected DAOHelper<String, Sign> dh;

	public DefaultSignDAO() {
		super();
	}

	public DefaultSignDAO(EntityManager em) {
		this.em = em;
		createDAOHelper();
	}

	@PostConstruct
	protected void createDAOHelper() {
		this.dh = DAOHelper.of(String.class, Sign.class, this.em);
	}

	@Override
	public Stream<Sign> listSigns() {
		return dh.list().stream();
	}

	@Override
	public Stream<Sign> listSignsByUser(User user) {
		final String query = "SELECT s FROM Sign s, Radiography r, Exploration e " +
			"WHERE s.radiography.id=r.id AND r.exploration.id=e.id AND :login=e.user.login";

		return this.em.createQuery(query, Sign.class).setParameter("login", user.getLogin()).getResultList().stream();
	}
}