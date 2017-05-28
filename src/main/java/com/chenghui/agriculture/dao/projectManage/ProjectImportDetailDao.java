package com.chenghui.agriculture.dao.projectManage;

import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.ProjectImportDetail;

/**
 * 模版访问接口
 * @author yudq
 * @version V2.0
 */
public interface ProjectImportDetailDao extends GenericDao<ProjectImportDetail, Long> {

	
	ProjectImportDetail findProjectNumber(Long projectDetailId);
}
