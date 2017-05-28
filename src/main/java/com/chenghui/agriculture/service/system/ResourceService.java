package com.chenghui.agriculture.service.system;

import java.util.List;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.model.Resources;
import com.chenghui.agriculture.model.Role;
import com.chenghui.agriculture.service.GenericService;

public interface ResourceService extends GenericService<Resources,Long>{

	List<Resources> findResourceSetByRole(Role role);
	
	List<Resources> findResourceSetByShiroUser(ShiroUser shiroUser);


}
