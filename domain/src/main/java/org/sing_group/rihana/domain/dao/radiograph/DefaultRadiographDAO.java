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
package org.sing_group.rihana.domain.dao.radiograph;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.sing_group.rihana.domain.dao.DAOHelper;
import org.sing_group.rihana.domain.dao.spi.radiograph.RadiographDAO;
import org.sing_group.rihana.domain.entities.radiograph.Radiograph;

@Default
@Transactional(value = Transactional.TxType.MANDATORY)
public class DefaultRadiographDAO implements RadiographDAO {

	@PersistenceContext
	protected EntityManager em;

	protected DAOHelper<String, Radiograph> dh;

	public DefaultRadiographDAO() {
		super();
	}

	public DefaultRadiographDAO(EntityManager em) {
		this.em = em;
		createDAOHelper();
	}

	@PostConstruct
	protected void createDAOHelper() {
		this.dh = DAOHelper.of(String.class, Radiograph.class, this.em);
	}

	@Override
	public Radiograph get(String id) {
		return this.dh.get(id).filter(e -> !e.isDeleted())
			.orElseThrow(() -> new IllegalArgumentException("Unknown radiograph: " + id));
	}

	@Override
	public Radiograph create(Radiograph radiograph) {
		return this.dh.persist(radiograph);
	}

	@Override
	public void delete(Radiograph radiograph) {
		radiograph.setDeleted(true);
		this.dh.update(radiograph);
	}
}
