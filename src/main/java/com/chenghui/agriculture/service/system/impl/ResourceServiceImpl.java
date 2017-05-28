package com.chenghui.agriculture.service.system.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.dao.system.ResourceDao;
import com.chenghui.agriculture.model.Resources;
import com.chenghui.agriculture.model.Role;
import com.chenghui.agriculture.service.GenericServiceImpl;
import com.chenghui.agriculture.service.system.ResourceService;

@Service("resourceService")
public class ResourceServiceImpl extends GenericServiceImpl<Resources, Long> implements ResourceService {

	@Autowired
	private ResourceDao resourceDao;
	
	
	@Override
	public List<Resources> findResourceSetByRole(Role role) {
		return resourceDao.findResourceSetByRole(role);
	}
	
	@Override
	public List<Resources> findResourceSetByShiroUser(ShiroUser shiroUser) {
		List<Resources> resources = new ArrayList<Resources>();
		Set<Role> roles = shiroUser.getRoles();
		for (Role role : roles) {
			resources.addAll(resourceDao.findResourceSetByRole(role));
		}
		return resources;
	}
	
}
