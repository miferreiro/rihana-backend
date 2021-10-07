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
package org.sing_group.rihana.service.sign;

import java.util.stream.Stream;

import javax.inject.Inject;

import org.sing_group.rihana.domain.dao.spi.sign.SignTypeDAO;
import org.sing_group.rihana.domain.entities.sign.SignType;
import org.sing_group.rihana.service.spi.sign.SignTypeService;

public class DefaultSingTypeService implements SignTypeService {

	@Inject
	private SignTypeDAO signTypeDAO;

	@Override
	public SignType get(String code) {
		return signTypeDAO.get(code);
	}

	@Override
	public SignType create(SignType signType) {
		return signTypeDAO.create(signType);
	}

	@Override
	public SignType edit(SignType signType) {
		return signTypeDAO.edit(signType);
	}

	@Override
	public void delete(SignType signType) {
		signTypeDAO.delete(signType);
	}

	@Override
	public Stream<SignType> listSignTypes() {
		return signTypeDAO.listSignTypes();
	}
}
