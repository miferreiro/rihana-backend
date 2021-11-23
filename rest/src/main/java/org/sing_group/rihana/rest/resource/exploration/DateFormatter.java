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
package org.sing_group.rihana.rest.resource.exploration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

	private final String DATE_FORMAT = "dd/MM/yyyy_HH:mm:ss";

	public DateFormatter() {
	}

	public Date getDateInitialDayTime(String partialDate) {
		try {
			return new SimpleDateFormat(this.DATE_FORMAT).parse(partialDate + "_00:00:00");
		} catch (ParseException e) {
			return new Date();
		}
	}

	public Date getDateFinalDayTime(String partialDate) {
		try {
			return new SimpleDateFormat(this.DATE_FORMAT).parse(partialDate + "_23:59:59");
		} catch (ParseException e) {
			return new Date();
		}
	}
}
