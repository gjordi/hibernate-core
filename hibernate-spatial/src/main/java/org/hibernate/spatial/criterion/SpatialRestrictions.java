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
package org.hibernate.spatial.criterion;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

import org.hibernate.criterion.Criterion;
import org.hibernate.spatial.SpatialRelation;

/**
 * A factory for spatial criteria.
 *
 * The criterion types created by this class implement the spatial query
 * expressions of the OpenGIS Simple Features Specification for SQL, Revision
 * 1.1. In addition, it provides for a simple spatial <code>filter</code> that
 * works mostly using the spatial index. This corresponds to the Oracle
 * Spatial's "SDO_FILTER" function, or the "&&" operator of PostGIS.
 *
 * @author Karel Maesen
 */
public class SpatialRestrictions {

	private SpatialRestrictions() {
	}

	/**
	 * Apply a "spatially equal" constraint to the named property
	 *
	 * @param propertyName The name of the property
	 * @param value The geometry value to use in comparison
	 *
	 * @return SpatialRelateExpression
	 *
	 * @see SpatialRelateExpression
	 */
	public static SpatialRelateExpression eq(String propertyName, Geometry value) {
		return new SpatialRelateExpression(
				propertyName, value,
				SpatialRelation.EQUALS
		);
	}

	/**
	 * Apply a "spatially within" constraint to the named property
	 *
	 * @param propertyName The name of the property
	 * @param value The geometry value to use in comparison
	 *
	 * @return SpatialRelateExpression
	 *
	 * @see SpatialRelateExpression
	 */
	public static SpatialRelateExpression within(String propertyName, Geometry value) {
		return new SpatialRelateExpression(
				propertyName, value,
				SpatialRelation.WITHIN
		);
	}

	/**
	 * Apply a "spatially contains" constraint to the named property
	 *
	 * @param propertyName The name of the property
	 * @param value The geometry value to use in comparison
	 *
	 * @return SpatialRelateExpression
	 *
	 * @see SpatialRelateExpression
	 */
	public static SpatialRelateExpression contains(String propertyName, Geometry value) {
		return new SpatialRelateExpression(
				propertyName, value,
				SpatialRelation.CONTAINS
		);
	}

	/**
	 * Apply a "spatially crosses" constraint to the named property
	 *
	 * @param propertyName The name of the property
	 * @param value The geometry value to use in comparison
	 *
	 * @return SpatialRelateExpression
	 *
	 * @see SpatialRelateExpression
	 */
	public static SpatialRelateExpression crosses(String propertyName, Geometry value) {
		return new SpatialRelateExpression(
				propertyName, value,
				SpatialRelation.CROSSES
		);
	}

	/**
	 * Apply a "spatially disjoint" constraint to the named property
	 *
	 * @param propertyName The name of the property
	 * @param value The geometry value to use in comparison
	 *
	 * @return SpatialRelateExpression
	 *
	 * @see SpatialRelateExpression
	 */
	public static SpatialRelateExpression disjoint(String propertyName, Geometry value) {
		return new SpatialRelateExpression(
				propertyName, value,
				SpatialRelation.DISJOINT
		);
	}

	/**
	 * Apply a "spatially intersects" constraint to the named property
	 *
	 * @param propertyName The name of the property
	 * @param value The geometry value to use in comparison
	 *
	 * @return SpatialRelateExpression
	 *
	 * @see SpatialRelateExpression
	 */
	public static SpatialRelateExpression intersects(String propertyName, Geometry value) {
		return new SpatialRelateExpression(
				propertyName, value,
				SpatialRelation.INTERSECTS
		);
	}

	/**
	 * Apply a "spatially overlaps" constraint to the named property
	 *
	 * @param propertyName The name of the property
	 * @param value The geometry value to use in comparison
	 *
	 * @return SpatialRelateExpression
	 *
	 * @see SpatialRelateExpression
	 */
	public static SpatialRelateExpression overlaps(String propertyName, Geometry value) {
		return new SpatialRelateExpression(
				propertyName, value,
				SpatialRelation.OVERLAPS
		);
	}

	/**
	 * Apply a "spatially touches" constraint to the named property
	 *
	 * @param propertyName The name of the property
	 * @param value The geometry value to use in comparison
	 *
	 * @return SpatialRelateExpression
	 *
	 * @see SpatialRelateExpression
	 */
	public static SpatialRelateExpression touches(String propertyName, Geometry value) {
		return new SpatialRelateExpression(
				propertyName, value,
				SpatialRelation.TOUCHES
		);
	}

	/**
	 * Apply a bounding box overlap constraint to the named property
	 *
	 * @param propertyName The name of the property
	 * @param value The geometry value whose bounding box to use in the comparison
	 *
	 * @return SpatialFilter
	 *
	 * @see SpatialFilter
	 */
	public static SpatialFilter filter(String propertyName, Geometry value) {
		return new SpatialFilter( propertyName, value );
	}

	/**
	 * Apply a bounding box overlap constraint to the named property
	 *
	 * @param propertyName The name of the property
	 * @param envelope The envelope or bounding box to use in the comparison
	 * @param srid the SRID of the bounding box
	 *
	 * @return SpatialFilter
	 *
	 * @see SpatialFilter
	 */
	public static SpatialFilter filter(String propertyName, Envelope envelope, int srid) {
		return new SpatialFilter( propertyName, envelope, srid );
	}

	/**
	 * Apply a "distance within" constraint to the named property
	 *
	 * @param propertyName The name of the property
	 * @param geometry The geometry value to use in the comparison
	 * @param distance The distance
	 *
	 * @return DWithinExpression
	 *
	 * @see DWithinExpression
	 */
	public static Criterion distanceWithin(String propertyName, Geometry geometry, double distance) {
		return new DWithinExpression( propertyName, geometry, distance );
	}

	/**
	 * Apply a "having srid" constraint to the named property
	 *
	 * @param propertyName The name of the property
	 * @param srid The SRID value to use in the comparison
	 *
	 * @return A HavingSridExpression
	 *
	 * @see HavingSridExpression
	 */
	public static Criterion havingSRID(String propertyName, int srid) {
		return new HavingSridExpression( propertyName, srid );
	}

	/**
	 * Apply an "is empty" constraint to the named property
	 *
	 * @param propertyName The name of the property
	 *
	 * @return A IsEmptyExpression
	 *
	 * @see IsEmptyExpression
	 */
	public static Criterion isEmpty(String propertyName) {
		return new IsEmptyExpression( propertyName, true );
	}

	/**
	 * Apply an "is not empty" constraint to the named property
	 *
	 * @param propertyName The name of the property
	 *
	 * @return A IsEmptyExpression
	 *
	 * @see IsEmptyExpression
	 */
	public static Criterion isNotEmpty(String propertyName) {
		return new IsEmptyExpression( propertyName, false );
	}

	/**
	 * Apply the specified spatial relation constraint to the named property.
	 *
	 * @param relation The spatial relation to apply
	 * @param propertyName The name of the property
	 * @param value The geometry value to use in the comparison
	 *
	 * @return SpatialFilter
	 *
	 * @see SpatialFilter
	 */
	public static Criterion spatialRestriction(int relation, String propertyName, Geometry value) {
		switch ( relation ) {
			case SpatialRelation.CONTAINS:
				return contains( propertyName, value );
			case SpatialRelation.CROSSES:
				return crosses( propertyName, value );
			case SpatialRelation.DISJOINT:
				return disjoint( propertyName, value );
			case SpatialRelation.INTERSECTS:
				return intersects( propertyName, value );
			case SpatialRelation.EQUALS:
				return eq( propertyName, value );
			case SpatialRelation.FILTER:
				return filter( propertyName, value );
			case SpatialRelation.OVERLAPS:
				return overlaps( propertyName, value );
			case SpatialRelation.TOUCHES:
				return touches( propertyName, value );
			case SpatialRelation.WITHIN:
				return within( propertyName, value );
			default:
				throw new IllegalArgumentException(
						"Non-existant spatial relation passed."
				);
		}
	}
}
