package com.chenghui.agriculture.dao;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SQLCriterion;
import org.hibernate.type.Type;

public class ChengHuiRestrictions extends Restrictions {
	/**
	 * Create a restriction expressed in SQL with JDBC parameters.  Any occurrences of <tt>{alias}</tt> will be
	 * replaced by the table alias.
	 *
	 * @param sql The SQL restriction
	 * @param values The parameter values
	 * @param types The parameter types
	 *
	 * @return The Criterion
	 *
	 * @see SQLCriterion
	 */
	public static Criterion sqlRestriction(String sql, Object[] values, Type[] types) {
		return new ChengHuiSQLCriterion( sql, values, types );
	}

	/**
	 * Create a restriction expressed in SQL with one JDBC parameter.  Any occurrences of <tt>{alias}</tt> will be
	 * replaced by the table alias.
	 *
	 * @param sql The SQL restriction
	 * @param value The parameter value
	 * @param type The parameter type
	 *
	 * @return The Criterion
	 *
	 * @see SQLCriterion
	 */
	public static Criterion sqlRestriction(String sql, Object value, Type type) {
		return new ChengHuiSQLCriterion( sql, value, type );
	}

	/**
	 * Apply a constraint expressed in SQL with no JDBC parameters.  Any occurrences of <tt>{alias}</tt> will be
	 * replaced by the table alias.
	 *
	 * @param sql The SQL restriction
	 *
	 * @return The Criterion
	 *
	 * @see SQLCriterion
	 */
	public static Criterion sqlRestriction(String sql) {
		return new ChengHuiSQLCriterion( sql );
	}
}
