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

import static java.nio.file.Files.copy;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.isDirectory;
import static java.nio.file.Files.walk;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.io.FilenameUtils.getExtension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.sing_group.rihana.domain.entities.exploration.Exploration;
import org.sing_group.rihana.domain.entities.radiograph.Radiograph;
import org.sing_group.rihana.service.spi.exploration.ExplorationStorage;
import org.sing_group.rihana.service.spi.radiograph.RadiographService;

@Default
public class DefaultExplorationStorage implements ExplorationStorage {

	private static final String PATH_CONFIG_NAME = "java:global/rihana/defaultexplorationstorage/path";

	@Resource(name = PATH_CONFIG_NAME)
	private String path;

	@Inject
	private RadiographService radiographService;

	public DefaultExplorationStorage() {}

	public DefaultExplorationStorage(String path) {
		this.path = path;
	}

	@Override
	public String storeExplorationXml(Exploration exploration) {
		Path filePath = getExplorationFolderForId(exploration.getId());

		File folder = filePath.toFile();

		folder.setExecutable(true, false);
		folder.setReadable(true, false);
		folder.setWritable(true, false);

		filePath = filePath.resolve(exploration.getId() + ".xml");

		if (exists(filePath)) {
			filePath.toFile().delete();
		}

		File file = filePath.toFile();

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Exploration.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.marshal(exploration, file);

		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}

		file.setExecutable(true, false);
		file.setReadable(true, false);
		file.setWritable(true, false);

		return file.toString();
	}

	@Override
	public String storeRadiograph(Radiograph radiograph, InputStream data) {
		Path filePath = getExplorationFolderForId(radiograph.getExploration().getId());

		File folder = filePath.toFile();

		folder.setExecutable(true, false);
		folder.setReadable(true, false);
		folder.setWritable(true, false);

		filePath = filePath.resolve(radiograph.getType().name() + ".png");

		if (exists(filePath)) {
			throw new IllegalArgumentException("File already exists: " + filePath);
		}

		File file = filePath.toFile();

		try {
			copy(data, file.toPath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		file.setExecutable(true, false);
		file.setReadable(true, false);
		file.setWritable(true, false);

		return file.toString();
	}

	private Path getExplorationFolderForId(String id) {

		final Path p = getBasePath().resolve(id);

		if (!exists(p) || !isDirectory(p)) {
			new File(p.toString()).mkdirs();
		}

		return p;
	}

	private Path getBasePath() {
		final Path p = Paths.get(this.path);

		if (!exists(p) || !isDirectory(p)) {
			throw new IllegalArgumentException(
				String.format(
					"path for file storage is invalid: %s. Check that you have configured the global resource name '%s' correctly",
					this.path, PATH_CONFIG_NAME
				)
			);
		}
		return p;
	}

	@Override
	public FileInputStream retrieveRadiograph(Radiograph radiograph) {
		Path filePath = getExplorationFolderForId(radiograph.getExploration().getId());
		filePath = filePath.resolve(radiograph.getType().name() + ".png");

		if (!exists(filePath)) {
			throw new IllegalArgumentException("Cannot find file for id: " + radiograph.getId());
		}

		File file = filePath.toFile();

		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Exploration deleteRadiographsExploration(Exploration exploration) {

		Set<Radiograph> radiographSet = exploration.getCurrentRadiographs();

		for (Radiograph radiograph: radiographSet) {
			if (!radiograph.isDeleted()) {

				Path filePath = getExplorationFolderForId(radiograph.getExploration().getId());
				filePath = filePath.resolve(radiograph.getType().name() + ".png");

				if (!exists(filePath)) {
					throw new IllegalArgumentException("Cannot find file for id: " + filePath);
				}

				Path backupPath = getBasePath()
					.resolve("backup")
					.resolve(exploration.getId());

				File folder;

				if (!exists(backupPath) || !isDirectory(backupPath)) {
					folder = new File(backupPath.toString());
					folder.mkdirs();
				} else {
					folder = filePath.toFile();
				}

				folder.setExecutable(true, false);
				folder.setReadable(true, false);
				folder.setWritable(true, false);

				backupPath = backupPath.resolve(new SimpleDateFormat("'" + radiograph.getType().name() + "'-yyyyMMddHHmm'.png'")
					.format(new Date()));

				try {
					Files.move(filePath, backupPath, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				File file = filePath.toFile();

				file.setExecutable(true, false);
				file.setReadable(true, false);
				file.setWritable(true, false);

				radiograph.setSource(backupPath.toString());
			}
		}

		return exploration;
	}

	@Override
	public Set<String> getFormatsForRadiographType(Radiograph radiograph) {
		return walkOverFilesWithId(radiograph.getType().name())
			.map(path -> getExtension(path.toFile().getName()))
			.collect(toSet());
	}

	private Stream<Path> walkOverFilesWithId(String id) {
		return walkOverFiles().filter(path -> path.toFile().getName().startsWith(id));
	}

	private Stream<Path> walkOverFiles() {
		try {
			return walk(getBasePath()).filter(path -> !path.equals(getBasePath()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}