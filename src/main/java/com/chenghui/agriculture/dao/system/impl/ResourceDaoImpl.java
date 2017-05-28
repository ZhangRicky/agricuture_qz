package com.chenghui.agriculture.dao.system.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.system.ResourceDao;
import com.chenghui.agriculture.model.Resources;
import com.chenghui.agriculture.model.Role;

@SuppressWarnings("unchecked")
@Repository
public class ResourceDaoImpl extends GenericHibernateDao<Resources, Long> implements ResourceDao {

	@Override
	public List<Resources> findResourceSetByRole(Role role) {
		
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Resources.class);
		dCriteria.createAlias("role", "roleEntity");
		dCriteria.add(Restrictions.eq("roleEntity.id", role.getId()));
		List<Resources> resourceList = (List<Resources>) getHibernateTemplate().findByCriteria(dCriteria);
		//add parent resources
		Set<Long> parentIdSet = new HashSet<Long>();
		Set<Resources> parentSet = new HashSet<Resources>();
		for(Resources resource: resourceList){
			if(resource.getParentId() == null || StringUtils.isEmpty(resource.getParentId()+"") || parentIdSet.contains(resource.getParentId())){
				continue;
			}
			Resources parent = get(resource.getParentId());
			parentSet.add(parent);
			parentIdSet.add(resource.getParentId());
		}
		resourceList.addAll(parentSet);
		return resourceList;
	}
}
