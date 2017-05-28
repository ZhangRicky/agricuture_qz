package com.chenghui.agriculture.dao.system;

import java.util.List;

import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.Resources;
import com.chenghui.agriculture.model.Role;

public interface ResourceDao extends GenericDao<Resources, Long>{

	List<Resources> findResourceSetByRole(Role role);
}
