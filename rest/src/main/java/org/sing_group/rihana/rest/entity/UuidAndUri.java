/*-
 * #%L
 * REST
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
package org.sing_group.rihana.rest.entity;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import org.sing_group.rihana.domain.entities.Identifiable;

@XmlRootElement(name = "uuid-and-uri", namespace = "http://entity.resource.rest.rihana.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "id-and-uri", description = "URI and uuid of an entity.")
public class UuidAndUri implements Serializable {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "id", required = true)
	private String id;

	@XmlElement(name = "uri", required = true)
	private URI uri;

	UuidAndUri() { }

	public UuidAndUri(String id, URI uri) {
		this.id = id;
		this.uri = uri;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UuidAndUri other = (UuidAndUri) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}

	public static List<UuidAndUri> fromEntities(
		UriInfo requestURI, List<? extends Identifiable> list, Class<?> resourceClass
	) {
		return fromEntities(requestURI, list, resourceClass, null);
	}

	public static List<UuidAndUri> fromEntities(
		UriInfo requestURI, List<? extends Identifiable> list, Class<?> resourceClass, String concatURL
	) {
		Function<UriBuilder, UriBuilder> uriBuilderMap =
			(concatURL != null) ? (uriBuilder) -> uriBuilder.path(concatURL) : null;
		List<UuidAndUri> urls = new ArrayList<>();
		for (Identifiable object : list) {
			urls.add(_fromEntity(requestURI, uriBuilderMap, object, resourceClass));
		}
		return urls;
	}

	public static UuidAndUri fromEntity(UriInfo requestURI, Identifiable entity, Class<?> resourceClass) {
		return _fromEntity(requestURI, null, entity, resourceClass);
	}

	public static UuidAndUri fromEntity(UriInfo requestURI, Identifiable entity, Class<?> resourceClass, String concatURL) {
		Function<UriBuilder, UriBuilder> uriBuilderMap =
			(concatURL != null) ? (uriBuilder) -> uriBuilder.path(concatURL) : null;
		return _fromEntity(requestURI, uriBuilderMap, entity, resourceClass);
	}

	private static UuidAndUri _fromEntity(
		UriInfo requestURI, Function<UriBuilder, UriBuilder> uriBuilderMap, Identifiable entity, Class<?> resourceClass
	) {
		if (entity == null) {
			return null;
		}

		UriBuilder pathUntilId = UriBuilder.fromResource(resourceClass).path(entity.getId());
		return new UuidAndUri(
			entity.getId(),
			requestURI.getBaseUriBuilder().path(
					(uriBuilderMap != null ? uriBuilderMap.apply(pathUntilId) : pathUntilId)
						.build()
						.toString())
				.build()
		);
	}
}
