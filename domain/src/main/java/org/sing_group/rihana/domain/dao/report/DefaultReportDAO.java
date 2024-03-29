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
package org.sing_group.rihana.domain.dao.report;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.sing_group.rihana.domain.dao.DAOHelper;
import org.sing_group.rihana.domain.dao.spi.report.ReportDAO;
import org.sing_group.rihana.domain.entities.report.Report;

@Default
@Transactional(value = Transactional.TxType.MANDATORY)
public class DefaultReportDAO implements ReportDAO {

	@PersistenceContext
	protected EntityManager em;

	protected DAOHelper<String, Report> dh;

	public DefaultReportDAO() {
		super();
	}

	public DefaultReportDAO(EntityManager em) {
		this.em = em;
		createDAOHelper();
	}

	@PostConstruct
	protected void createDAOHelper() {
		this.dh = DAOHelper.of(String.class, Report.class, this.em);
	}

	@Override
	public Report get(String id) {
		return this.dh.get(id).filter(e -> !e.isDeleted())
			.orElseThrow(() -> new IllegalArgumentException("Unknown report: " + id));
	}

	@Override
	public Report getReportBy(String reportN) {
		return this.em
			.createQuery("SELECT r FROM Report r WHERE r.reportN=:reportN AND r.deleted=0 AND r.exploration.deleted=0", Report.class)
			.setParameter("reportN", reportN).getSingleResult();
	}

	@Override
	public boolean existsReportNBy(String reportN) {
		return this.em
			.createQuery("SELECT r FROM Report r WHERE r.reportN=:reportN AND r.deleted=0 AND r.exploration.deleted=0", Report.class)
			.setParameter("reportN", reportN).getResultList().size() != 0;
	}

	@Override
	public Report create(Report report) {
		return this.dh.persist(report);
	}

	@Override
	public Report edit(Report report) {
		return this.dh.update(report);
	}

	@Override
	public void delete(Report report) {
		report.setDeleted(true);
		this.dh.update(report);
	}
}
