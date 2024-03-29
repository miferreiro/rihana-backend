/*-
 * #%L
 * REST
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
package org.sing_group.rihana.rest.entity.mapper;

import org.sing_group.rihana.domain.entities.acl.role.Role;
import org.sing_group.rihana.domain.entities.user.User;
import org.sing_group.rihana.rest.entity.mapper.spi.UserMapper;
import org.sing_group.rihana.rest.entity.user.UserData;
import org.sing_group.rihana.rest.entity.user.UserEditionData;

public class DefaultUserMapper implements UserMapper {

	@Override
	public UserData toUserData(User user) {
		return new UserData(user.getLogin(), user.getPassword(), user.getRole().getName());
	}
	@Override
	public void assignUserWithRoleEditionData(User user, UserEditionData userEditionData, Role role) {
		// Checks if the password has been modified
		if (!userEditionData.getPassword().isEmpty()) {
			user.setPassword(userEditionData.getPassword());
		}
		user.setRole(role);
	}

	@Override
	public void assignUserEditionData(User user, UserEditionData userEditionData) {
		// Checks if the password has been modified
		if (!userEditionData.getPassword().isEmpty()) {
			user.setPassword(userEditionData.getPassword());
		}
	}
}
