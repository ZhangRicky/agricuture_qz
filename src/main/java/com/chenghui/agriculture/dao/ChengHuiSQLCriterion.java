package com.chenghui.agriculture.dao;

import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.CriteriaImpl.Subcriteria;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.type.Type;

public class ChengHuiSQLCriterion implements Criterion {
	private final String sql;
	private final TypedValue[] typedValues;

	protected ChengHuiSQLCriterion(String sql, Object[] values, Type[] types) {
		this.sql = sql;
		this.typedValues = new TypedValue[values.length];
		for ( int i=0; i<typedValues.length; i++ ) {
			typedValues[i] = new TypedValue( types[i], values[i] );
		}
	}

	protected ChengHuiSQLCriterion(String sql, Object value, Type type) {
		this.sql = sql;
		this.typedValues = new TypedValue[] { new TypedValue( type, value ) };
	}

	protected ChengHuiSQLCriterion(String sql) {
		this.sql = sql;
		this.typedValues = new TypedValue[0];
	}

	@Override
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) {
        CriteriaImpl rootCriteria = null;  
        if(criteria instanceof CriteriaImpl){  
            rootCriteria = (CriteriaImpl)criteria;  
        }else if(criteria instanceof Subcriteria){  
            rootCriteria = (CriteriaImpl) ((Subcriteria)criteria).getParent();  
        }else {  
            throw new HibernateException("暂不支持其他Criteria的实现");  
        }  
        Iterator iterateSubcriteria = rootCriteria.iterateSubcriteria();  
        String tempSql = sql;  
        //replace subcriterias' alias  
        while (iterateSubcriteria.hasNext()) {  
            Subcriteria subCriteria = (Subcriteria) iterateSubcriteria.next();  
            tempSql = StringHelper.replace( tempSql, "{"+subCriteria.getAlias()+"}", criteriaQuery.getSQLAlias(subCriteria) );  
        }  
        return StringHelper.replace( tempSql, "{alias}", criteriaQuery.getSQLAlias(criteria) );  
	}

	@Override
	public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) {
		return typedValues;
	}

	@Override
	public String toString() {
		return sql;
	}
}
