package com.chenghui.agriculture.dao.projectManage.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.projectManage.SubProjectDao;
import com.chenghui.agriculture.model.Project;
import com.chenghui.agriculture.model.SubProject;

/**
 * 子项目DAO实现
 * @author LLJ
 * @version V1.0
 */
@SuppressWarnings("unchecked")
@Repository
public class SubProjectDaoImpl extends GenericHibernateDao<SubProject, Long> implements SubProjectDao {

	@Override
	public void executeLoad(String loadSql) {
		
		getSessionFactory().getCurrentSession().createSQLQuery(loadSql).executeUpdate();
		
	}

	
	/**
	 * 通过父项目Id查询所有子项目
	 * @param pId 父项目Id
	 * @return
	 */
	@Override
	public List<SubProject> getSubProjectsByPId(long pId){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SubProject.class);
//		detachedCriteria.createAlias("project","project");
		Project project = new Project();
		project.setId(pId);
		detachedCriteria.add(Restrictions.eq("project", project));
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.addOrder(Order.desc("id"));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		return (List<SubProject>) getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	@Override
	public int subProjectNumber(Long projectId) {
		String sql = "select * from sub_project where flag=0 and project="+projectId;
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		int count = sqlQuery.list().size();
		return count;
	}
	
	
}
