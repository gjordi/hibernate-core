/*
 * This file is part of Hibernate Spatial, an extension to the
 *  hibernate ORM solution for spatial (geographic) data.
 *
 *  Copyright © 2007-2012 Geovise BVBA
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.hibernate.spatial.testing;

import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.spatial.Log;
import org.hibernate.spatial.SpatialDialect;
import org.hibernate.spatial.SpatialFunction;
import org.hibernate.testing.AfterClassOnce;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Karel Maesen, Geovise BVBA
 *         creation-date: Sep 30, 2010
 */
public abstract class SpatialFunctionalTestCase extends BaseCoreFunctionalTestCase {

	protected TestData testData;
	protected DataSourceUtils dataSourceUtils;
	protected GeometryEquality geometryEquality;
	protected AbstractExpectationsFactory expectationsFactory;

	/**
	 * Inserts the test data via a direct route (JDBC).
	 */
	public void prepareTest() {
		try {
			dataSourceUtils.insertTestData(testData);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Removes the test data.
	 */
	public void cleanupTest() {
		Session session = null;
		Transaction tx = null;
		try {
			session = openSession();
			tx = session.beginTransaction();
			String hql = "delete from GeomEntity";
			Query q = session.createQuery(hql);
			q.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * Override to also ensure that the SpatialTestSupport utility is
	 * initialised together with the Hibernate <code>Configuration</code>.
	 *
	 * @return
	 */
	protected Configuration constructConfiguration() {
		Configuration cfg = super.constructConfiguration();
		initializeSpatialTestSupport(cfg);
		return cfg;
	}

	private void initializeSpatialTestSupport(Configuration cfg) {
		try {
			TestSupport support = TestSupportFactories.instance().getTestSupportFactory(getDialect());
			dataSourceUtils = support.createDataSourceUtil(cfg);
			expectationsFactory = support.createExpectationsFactory(dataSourceUtils);
			testData = support.createTestData(this);
			geometryEquality = support.createGeometryEquality();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Overwrites the afterSessionFactoryBuilt() method in BaseCoreFunctionalTestCase.
	 * <p/>
	 * Mostly used to register spatial metadata in databases such as Oracle Spatial.
	 */
	protected void afterSessionFactoryBuilt() {
		dataSourceUtils.afterCreateSchema();
	}

	/**
	 * Cleans up the dataSourceUtils
	 *
	 * @throws SQLException
	 */
	@AfterClassOnce
	public void closeDataSourceUtils() throws SQLException {
		dataSourceUtils.close();
	}

	public String getBaseForMappings() {
		return "";
	}

	public String[] getMappings() {
		return new String[]{"GeomEntity.hbm.xml"};
	}

	/**
	 * Returns true if the spatial dialect supports the specified function
	 *
	 * @param spatialFunction
	 * @return
	 */
	public boolean isSupportedByDialect(SpatialFunction spatialFunction) {
		SpatialDialect dialect = (SpatialDialect) getDialect();
		return dialect.supports(spatialFunction);
	}

	/**
	 * Supports true if the spatial dialect supports filtering (e.g. ST_overlap, MBROverlap, SDO_FILTER)
	 *
	 * @return
	 */
	public boolean dialectSupportsFiltering() {
		SpatialDialect dialect = (SpatialDialect) getDialect();
		return dialect.supportsFiltering();
	}

	abstract protected Log getLogger();

	/**
	 * Adds the query results to a Map.
	 * <p/>
	 * Each row is added as a Map entry with the first column the key,
	 * and the second the value. It is assumed that the first column is an
	 * identifier of a type assignable to Integer.
	 *
	 * @param result map of
	 * @param query  the source Query
	 * @param <T>    type of the second column in the query results
	 */
	protected <T> void addQueryResults(Map<Integer, T> result, Query query) {
		List<Object[]> rows = (List<Object[]>) query.list();
		if (rows.size() == 0) {
			getLogger().warn("No results returned for query!!");
		}
		for (Object[] row : rows) {
			Integer id = (Integer) row[0];
			T val = (T) row[1];
			result.put(id, val);
		}
	}

	protected <T> void compare(Map<Integer, T> expected, Map<Integer, T> received) {
		for (Integer id : expected.keySet()) {
			getLogger().debug("Case :" + id);
			getLogger().debug("expected: " + expected.get(id));
			getLogger().debug("received: " + received.get(id));
			compare(id, expected.get(id), received.get(id));
		}
	}


	protected void compare(Integer id, Object expected, Object received) {
		assertTrue(expected != null || (expected == null && received == null));
		if (expected instanceof byte[]) {
			assertArrayEquals("Failure on testsuite-suite for case " + id, (byte[]) expected, (byte[]) received);

		} else if (expected instanceof Geometry) {
			if (!(received instanceof Geometry)) {
				fail("Expected a Geometry, but received an object of type " + received.getClass().getCanonicalName());
			}
			assertTrue(
					"Failure on testsuite-suite for case " + id,
					geometryEquality.test((Geometry) expected, (Geometry) received)
			);

		} else {
			if (expected instanceof Long) {
				assertEquals("Failure on testsuite-suite for case " + id, ((Long) expected).intValue(), received);
			} else {
				assertEquals("Failure on testsuite-suite for case " + id, expected, received);
			}
		}
	}

}