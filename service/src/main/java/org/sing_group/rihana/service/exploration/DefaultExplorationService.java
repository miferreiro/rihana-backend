/*-
 * #%L
 * Service
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
package org.sing_group.rihana.service.exploration;

import java.util.List;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.rihana.domain.dao.spi.exploration.ExplorationDAO;
import org.sing_group.rihana.domain.entities.exploration.Exploration;
import org.sing_group.rihana.domain.entities.sign.SignType;
import org.sing_group.rihana.domain.entities.user.User;
import org.sing_group.rihana.service.spi.exploration.ExplorationService;

@Stateless
@PermitAll
public class DefaultExplorationService implements ExplorationService {

	@Inject
	private ExplorationDAO explorationDao;

	@Override
	public Exploration getExploration(String id) {
		return explorationDao.getExploration(id);
	}

	@Override
	public Stream<Exploration> listExplorationsByUser(int page, int pageSize, User user, List<SignType> signTypeList) {
		return explorationDao.listExplorationsByUser(page, pageSize, user, signTypeList);
	}

	@Override
	public int countAllExplorations() {
		return explorationDao.countAllExplorations();
	}

	@Override
	public int countExplorations() {
		return explorationDao.countExplorations();
	}

	@Override
	public int countExplorationsByUser(User user) {
		return explorationDao.countExplorationsByUser(user);
	}

	@Override
	public int countExplorationsByUserAndSignTypes(User user, List<SignType> signTypeList) {
		return explorationDao.countExplorationsByUserAndSignTypes(user, signTypeList);
	}

	@Override
	public Exploration create(Exploration exploration) {
		return explorationDao.create(exploration);
	}

	@Override
	public Exploration edit(Exploration exploration) {
		return explorationDao.edit(exploration);
	}

	@Override
	public void delete(Exploration exploration) {
		explorationDao.delete(exploration);
	}
}
