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

import static java.util.Collections.singleton;
import static java.util.Objects.requireNonNull;
import static org.sing_group.fluent.checker.Checks.requireNonEmpty;
import static org.sing_group.fluent.checker.Checks.requireNonNullCollection;
import static org.sing_group.rihana.domain.dao.ListingOptions.allResults;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DAOHelper<K, T> {
	private final EntityManager em;
	private final Class<T> entityClass;

	public static <K, T> DAOHelper<K, T> of(Class<K> keyClass, Class<T> entityClass, EntityManager em) {
		return new DAOHelper<>(keyClass, entityClass, em);
	}

	private DAOHelper(Class<K> keyClass, Class<T> entityClass, EntityManager em) {
		this.entityClass = entityClass;
		this.em = em;
	}

	public Class<T> getEntityType() {
		return this.entityClass;
	}

	public Optional<T> get(K key) {
		return Optional.ofNullable(this.em.find(this.getEntityType(), requireNonNull(key)));
	}

	public T persist(T entity) {
		try {
			this.em.persist(entity);
			this.em.flush();

			return entity;
		} catch (PersistenceException pe) {
			throw new IllegalArgumentException("Entity already exists", pe);
		}
	}

	public T update(T entity) {
		final T mergedEntity = this.em.merge(entity);
		this.em.flush();

		return mergedEntity;
	}

	public List<T> list() {
		final CriteriaQuery<T> query = createCBQuery();

		return em.createQuery(
			query.select(query.from(getEntityType()))
		).getResultList();
	}

	public List<T> list(ListingOptions options) {
		return this.list(options, context -> new Predicate[0]);
	}

	public List<T> list(Function<ListQueryContext, Predicate[]> predicatesBuilder) {
		return this.list(ListingOptions.allResults().unsorted(), predicatesBuilder);
	}

	public List<T> list(ListingOptions options, Function<ListQueryContext, Predicate[]> predicatesBuilder) {
		final ListingOptionsQueryBuilder optionsBuilder = new ListingOptionsQueryBuilder(options);

		final CriteriaQuery<T> queryBuilder = createCBQuery();
		final Root<T> root = queryBuilder.from(getEntityType());

		CriteriaQuery<T> select = optionsBuilder.addOrder(cb(), queryBuilder.select(root), root);

		final Predicate[] predicates = predicatesBuilder.apply(
			new ListQueryContext(root, select)
		);

		if (predicates.length > 0)
			select = select.where(predicates);

		final TypedQuery<T> query = optionsBuilder.addLimits(em.createQuery(select));

		return query.getResultList();
	}

	public void removeByKey(K key) {
		this.em.remove(get(key).orElseThrow(() -> new IllegalArgumentException("No entity found with id: " + key)));
		this.em.flush();
	}

	public void remove(T entity) {
		this.em.remove(entity);
		this.em.flush();
	}

	public final <F> List<T> listBy(String fieldName, F values) {
		return this.listBy(fieldName, singleton(values), allResults().unsorted());
	}

	public final <F> List<T> listBy(String fieldName, F values, ListingOptions listingOptions) {
		return createFieldQuery(fieldName, singleton(values), listingOptions)
			.getResultList();
	}

	public final <F> List<T> listBy(String fieldName, Set<F> values) {
		return this.listBy(fieldName, values, allResults().unsorted());
	}

	public final <F> List<T> listBy(String fieldName, Set<F> values, ListingOptions listingOptions) {
		return createFieldQuery(fieldName, values, listingOptions)
			.getResultList();
	}

	public <F> Optional<T> getBy(String fieldName, F value) {
		return getBy(fieldName, value, allResults().unsorted());
	}

	public <F> Optional<T> getBy(String fieldName, F value, ListingOptions listingOptions) {
		try {
			return Optional.of(
				createFieldQuery(fieldName, singleton(value), listingOptions)
					.getSingleResult()
			);
		} catch (NoResultException e) {
			return Optional.ofNullable(null);
		}
	}

	public long count() {
		CriteriaQuery<Long> query = cb().createQuery(Long.class);

		query = query.select(cb().count(query.from(this.getEntityType())));

		return this.em.createQuery(query).getSingleResult();
	}

	public long count(BiFunction<CriteriaBuilder, Root<T>, Predicate[]> predicatesBuilder) {
		final CriteriaQuery<Long> queryBuilder = cb().createQuery(Long.class);
		final Root<T> root = queryBuilder.from(this.getEntityType());

		final Predicate[] predicates = predicatesBuilder.apply(cb(), root);

		CriteriaQuery<Long> select = queryBuilder.select(cb().count(root));

		if (predicates.length > 0) {
			select = queryBuilder.where(predicates);
		}

		return this.em.createQuery(select).getSingleResult();
	}

	public int countRelated(String key, K keyValue, String relationFieldName) {
		CriteriaQuery<Integer> query = cb().createQuery(Integer.class);

		final Root<T> root = query.from(this.getEntityType());

		query = query.select(cb().size(root.get(relationFieldName)))
			.where(cb().equal(root.get(key), keyValue));

		return this.em.createQuery(query).getSingleResult();
	}

	public final <F> TypedQuery<T> createFieldQuery(
		String fieldName,
		Set<F> values,
		ListingOptions listingOptions
	) {
		requireNonNull(fieldName, "Fieldname can't be null");
		requireNonNullCollection(values);
		requireNonEmpty(values, "At least one value must be provided for the field");

		CriteriaQuery<T> query = createCBQuery();
		final Root<T> root = query.from(getEntityType());

		final Function<F, Predicate> fieldEqualsTo = value -> cb().equal(root.get(fieldName), value);

		final ListingOptionsQueryBuilder queryBuilder = new ListingOptionsQueryBuilder(listingOptions);

		final Predicate predicate =
			values.size() == 1 ? fieldEqualsTo.apply(values.iterator().next()) : cb().or(values.stream()
				.map(fieldEqualsTo)
				.toArray(Predicate[]::new)
			);

		query = query.select(root).where(predicate);

		if (listingOptions == null) {
			return em.createQuery(
				query.select(root).where(predicate)
			);
		} else {
			query = queryBuilder.addOrder(cb(), query, root);

			return queryBuilder.addLimits(
				em.createQuery(
					query.select(root).where(predicate)
				)
			);
		}
	}

	public EntityManager em() {
		return this.em;
	}

	public CriteriaQuery<T> createCBQuery() {
		return cb().createQuery(this.getEntityType());
	}

	public CriteriaBuilder cb() {
		return em.getCriteriaBuilder();
	}

	public class ListQueryContext {
		private final Root<T> root;
		private final CriteriaQuery<T> query;

		public ListQueryContext(Root<T> root, CriteriaQuery<T> query) {
			this.root = root;
			this.query = query;
		}

		public CriteriaBuilder cb() {
			return DAOHelper.this.cb();
		}

		public Root<T> root() {
			return root;
		}

		public CriteriaQuery<T> query() {
			return query;
		}
	}
}
