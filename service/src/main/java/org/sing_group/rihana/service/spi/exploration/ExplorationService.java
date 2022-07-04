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
package org.sing_group.rihana.service.spi.exploration;

import java.util.Date;
import java.util.Set;
import java.util.stream.Stream;

import javax.ejb.Local;

import org.sing_group.rihana.domain.entities.exploration.Exploration;
import org.sing_group.rihana.domain.entities.sign.SignType;
import org.sing_group.rihana.domain.entities.user.User;

@Local
public interface ExplorationService {

	Exploration getExploration(String id);

	Exploration getExplorationDeleted(String id);

	int getLastTitleExploration();

	Stream<Exploration> listExplorationsByUserInDateRange(int page, int pageSize,
														  User user,
														  Date initialDate, Date finalDate,
														  Set<SignType> signTypes, String operator);

	int countAllExplorations();

	int countExplorationsByUserAndSignTypesInDateRange(User user,
													   Date initialDate, Date finalDate,
													   Set<SignType> signTypes, String operator);

	Exploration create(Exploration exploration);

	Exploration edit(Exploration exploration);

	void delete(Exploration exploration);

	void recover(Exploration exploration);
}
