/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2021 David A. Ruano Ordás, José Ramón Méndez Reboredo,
 * 			Miguel Ferreiro Díaz
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
package org.sing_group.rihana.domain.dao;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

public class ListingOptionsQueryBuilder {
	private final ListingOptions options;

	public ListingOptionsQueryBuilder(ListingOptions options) {
		this.options = requireNonNull(options, "options can't be null");
	}

	public <T> CriteriaQuery<T> addOrder(CriteriaBuilder cb, CriteriaQuery<T> query, Root<T> root) {
		if (options.hasOrder()) {
			final List<Order> order = this.options.getSortFields()
				.map(sortField -> {
					switch (sortField.getSortDirection()) {
						case ASCENDING:
							return cb.asc(getField(sortField.getSortField(), root));
						case DESCENDING:
							return cb.desc(getField(sortField.getSortField(), root));
						default:
							throw new IllegalStateException("Invalid sort direction: " + sortField.getSortDirection());
					}
				})
				.collect(toList());

			return query.orderBy(order);
		} else {
			return query;
		}
	}

	private static Path<Object> getField(String field, Root<?> root) {
		final String[] subfields = field.split("\\.");

		From<?, ?> join = root;
		for (int i = 0; i < subfields.length - 1; i++) {
			join = join.join(subfields[i]);
		}

		return join.get(subfields[subfields.length - 1]);
	}

	public <T> TypedQuery<T> addLimits(TypedQuery<T> query) {
		if (this.options.hasResultLimits()) {
			return query
				.setFirstResult(options.getStart().getAsInt())
				.setMaxResults(options.getMaxResults().getAsInt());
		} else {
			return query;
		}
	}
}
