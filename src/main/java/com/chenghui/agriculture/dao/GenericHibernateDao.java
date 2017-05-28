package com.chenghui.agriculture.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import com.chenghui.agriculture.core.utils.PaginationSupport;

@SuppressWarnings("unchecked")
public abstract class GenericHibernateDao<M extends Serializable, PK extends Serializable>
		extends HibernateDaoSupport implements GenericDao<M, PK> {
	// 实体类类型(由构造方法自动赋值)
	private Class<M> entityClass;
	private String entityName;

	// 构造方法，根据实例类自动获取实体类类型
	@SuppressWarnings({ "rawtypes" })
	public GenericHibernateDao() {
		this.entityClass = null;
		Class c = getClass();
		Type t = c.getGenericSuperclass();
		if (t instanceof ParameterizedType) {
			Type[] p = ((ParameterizedType) t).getActualTypeArguments();
			this.entityClass = (Class<M>) p[0];
			String getSimpleName = entityClass.getSimpleName();
			String upperCase = getSimpleName.substring(0, 1);
			String lowerCase = upperCase.toLowerCase();
			entityName = entityClass.getSimpleName().replaceFirst(upperCase,lowerCase);
		}
	}

	@Autowired
	public void setMySessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	public Session getSession() {
		return getSessionFactory().getCurrentSession();
	}

	@Override
	public void update(M model) {
		getHibernateTemplate().update(model);

	}

	@Override
	public PK save(M model) throws DataAccessException{
		return (PK) getHibernateTemplate().save(model);
	}

	@Override
	public PK add(M model) throws DataAccessException{
		return (PK) getHibernateTemplate().save(model);
	}

	@Override
	public List<M> findByCriteria(DetachedCriteria detachedCriteria) throws DataAccessException{
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<M>) getHibernateTemplate().findByCriteria(detachedCriteria);
	}

	@Override
	public List<M> findByExample(M model) throws DataAccessException{
		return (List<M>) getHibernateTemplate().findByExample(model);

	}

	@Override
	public List<M> findByHQL(String string,Object... values) throws DataAccessException{
		return (List<M>) getHibernateTemplate().find(string, values);

	}

	@Override
	public M get(PK id) throws DataAccessException{
		return (M) getHibernateTemplate().get(entityClass, id);
	}

	@Override
	public M load(PK id) throws DataAccessException{
		return (M) getHibernateTemplate().load(entityClass, id);
	}

	
	@Override
	public void remove(M model) throws DataAccessException{
		getHibernateTemplate().delete(model);
	}

	@Override
	public void removeByID(PK id) throws DataAccessException{
		remove(get(id));
	}

	@Override
	public List<M> findAll()  throws DataAccessException{
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(entityClass);
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		detachedCriteria.addOrder(Order.asc("id"));
		return (List<M>) getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	@Override
	public List<M> findByName(String name)  throws DataAccessException{
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(entityClass);
		detachedCriteria.add(Restrictions.eq(entityName+"Name", name));
		return (List<M>) getHibernateTemplate().findByCriteria(detachedCriteria);
	}

	/**
	 * 以下分页:抄的 http://www.iteye.com/topic/14657
	 */
	@Override
    public PaginationSupport<M> findPageByCriteria(final DetachedCriteria detachedCriteria) {  
        return findPageByCriteria(detachedCriteria, PaginationSupport.PAGESIZE, 0);  
    }  
  
    public PaginationSupport<M> findPageByCriteria(final DetachedCriteria detachedCriteria, final int startIndex) {  
        return findPageByCriteria(detachedCriteria, PaginationSupport.PAGESIZE, startIndex);  
    }  
  
    
    @Override
    @SuppressWarnings({ "rawtypes" })
	public PaginationSupport<M> findPageByCriteria(final DetachedCriteria detachedCriteria, final int pageSize,  
            final int startIndex) {  
        return (PaginationSupport) getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {  
            public Object doInHibernate(Session session) throws HibernateException {  
                Criteria criteria = detachedCriteria.getExecutableCriteria(session);  
                int totalCount = ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();  
                criteria.setProjection(null);  
                List items = criteria.setFirstResult(startIndex).setMaxResults(pageSize).list();  
                PaginationSupport ps = new PaginationSupport(items, totalCount, pageSize, startIndex);  
                return ps;  
            }  
        });  
    }  
    @Override
    @SuppressWarnings({ "rawtypes" })
	public List findAllByCriteria(final DetachedCriteria detachedCriteria) {  
        return (List) getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {  
            public Object doInHibernate(Session session) throws HibernateException {  
                Criteria criteria = detachedCriteria.getExecutableCriteria(session);  
                return criteria.list();  
            }  
        });  
    }
    @Override
    @SuppressWarnings({ "rawtypes" })
    public int getCountByCriteria(final DetachedCriteria detachedCriteria) {  
        Integer count = (Integer) getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {  
            public Object doInHibernate(Session session) throws HibernateException {  
                Criteria criteria = detachedCriteria.getExecutableCriteria(session);  
                return criteria.setProjection(Projections.rowCount()).uniqueResult();  
            }  
        });  
        return count.intValue();  
    }

	@Override
	public boolean findNameExists(String queryColumn,String columnValue,String flag) {

		DetachedCriteria criteria = DetachedCriteria.forClass(entityClass);
		if(StringUtils.isNotEmpty(queryColumn)) {
			if(flag.equals("1")){
				criteria.add(Restrictions.ne(queryColumn, columnValue));//过滤当前修改的单元名称
			}
			criteria.add(Restrictions.eq(queryColumn, columnValue));
		}
		List<M> listSize =(List<M>) getHibernateTemplate().findByCriteria(criteria);
		return listSize.size() == 0?false:true;
	}
	
	@Override
	public List<M> findByName(String queryColumn,String columnValue) {

		DetachedCriteria criteria = DetachedCriteria.forClass(entityClass);
		if("User".equals(entityClass.getSimpleName())){
			criteria.add(Restrictions.ne("userName", "admin"));//过滤admin用户
			}
		criteria.add(Restrictions.like(queryColumn, columnValue, MatchMode.ANYWHERE));
		return (List<M>) getHibernateTemplate().findByCriteria(criteria);
	}
	
	@Override
	public List<M> findByNameStrict(String queryColumn,String columnValue) {

		DetachedCriteria criteria = DetachedCriteria.forClass(entityClass);
		criteria.add(Restrictions.eq(queryColumn, columnValue));
		return (List<M>) getHibernateTemplate().findByCriteria(criteria);
	}
	
	@Override
	public List<M> findByLongColumn(String queryColumn,Long columnValue) {
		DetachedCriteria criteria = DetachedCriteria.forClass(entityClass);
		criteria.add(Restrictions.eq(queryColumn, columnValue));
		return (List<M>) getHibernateTemplate().findByCriteria(criteria);
	}
	
	@Override
	public List<M> findByIntegerColumn(String queryColumn,Integer columnValue) {
		DetachedCriteria criteria = DetachedCriteria.forClass(entityClass);
		criteria.add(Restrictions.eq(queryColumn, columnValue));
		return (List<M>) getHibernateTemplate().findByCriteria(criteria);
	}
	
}
