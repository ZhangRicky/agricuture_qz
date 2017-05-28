package com.chenghui.agriculture.dao.projectManage;

import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.Files;

/**
 * 文件DAO
 * @author LLJ
 *
 */
public interface FilesDao extends GenericDao<Files, Long>{
	void executeLoad(String loadSql);
}
