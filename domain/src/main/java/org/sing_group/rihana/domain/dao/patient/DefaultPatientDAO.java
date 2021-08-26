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
package org.sing_group.rihana.domain.dao.patient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.sing_group.rihana.domain.dao.DAOHelper;
import org.sing_group.rihana.domain.dao.spi.patient.PatientDAO;
import org.sing_group.rihana.domain.entities.patient.Patient;

@Default
@Transactional(value = Transactional.TxType.MANDATORY)
public class DefaultPatientDAO implements PatientDAO {

	@PersistenceContext
	protected EntityManager em;

	protected DAOHelper<String, Patient> dh;

	public DefaultPatientDAO() {
		super();
	}

	public DefaultPatientDAO(EntityManager em) {
		this.em = em;
		createDAOHelper();
	}

	@PostConstruct
	protected void createDAOHelper() {
		this.dh = DAOHelper.of(String.class, Patient.class, this.em);
	}

	@Override
	public Patient create(Patient patient) {
		patient.setPatientID(encrypt(patient.getPatientID()));
		return this.dh.persist(patient);
	}

	@Override
	public Stream<Patient> getPatients() {
		return this.dh.list().stream();
	}

	@Override
	public Patient get(String id) {
		return this.dh.get(id)
			.orElseThrow(() -> new IllegalArgumentException("Unknown patient: " + id));
	}

	@Override
	public Patient getPatientBy(String patientID) {
		patientID = encrypt(patientID);
		return this.em
			.createQuery("SELECT p FROM Patient p WHERE p.patientID=:patientID", Patient.class)
			.setParameter("patientID", patientID).getSingleResult();
	}

	@Override
	public Patient edit(Patient patient) {
		patient.setPatientID(encrypt(patient.getPatientID()));
		return this.dh.update(patient);
	}

	@Override
	public void delete(Patient patient) {
		this.dh.remove(patient);
	}

	private String encrypt(String patientID) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] array = md.digest(patientID.getBytes());
			StringBuffer sb = new StringBuffer();
			for (byte b : array) {
				sb.append(
					Integer.toHexString((b & 0xFF) | 0x100)
						.substring(1, 3)
				);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
