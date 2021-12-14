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
package org.sing_group.rihana.domain.dao.acl.permission;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.sing_group.rihana.domain.dao.DAOHelper;
import org.sing_group.rihana.domain.dao.spi.acl.permission.FunctionalityActionDAO;
import org.sing_group.rihana.domain.entities.acl.permission.FunctionalityAction;
import org.sing_group.rihana.domain.entities.acl.permission.FunctionalityActionKey;

@Default
@Transactional(value = Transactional.TxType.MANDATORY)
public class DefaultFunctionalityActionDAO implements FunctionalityActionDAO {

	@PersistenceContext
	protected EntityManager em;

	protected DAOHelper<FunctionalityActionKey, FunctionalityAction> dh;

	public DefaultFunctionalityActionDAO() {
		super();
	}

	public DefaultFunctionalityActionDAO(EntityManager em) {
		this.em = em;
		createDAOHelper();
	}

	@PostConstruct
	protected void createDAOHelper() {
		this.dh = DAOHelper.of(FunctionalityActionKey.class, FunctionalityAction.class, this.em);
	}

	@Override
	public FunctionalityAction create(FunctionalityAction functionalityAction) {
		return this.dh.persist(functionalityAction);
	}

	@Override
	public Stream<FunctionalityAction> getFunctionalityActions() {
		return this.dh.listBy("deleted", 0).stream();
	}

	@Override
	public FunctionalityAction get(FunctionalityActionKey id) {
		return this.dh.get(id).filter(e -> !e.isDeleted())
			.orElseThrow(() -> new IllegalArgumentException("Unknown functionality action: " + id));
	}

	@Override
	public void delete(FunctionalityAction functionalityAction) {
		functionalityAction.setDeleted(true);
		this.dh.update(functionalityAction);
	}
}
