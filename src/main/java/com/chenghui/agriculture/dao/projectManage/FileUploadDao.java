package com.chenghui.agriculture.dao.projectManage;


import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.FileUpload;



/**
 * 模版类型访问接口
 * @author yudq
 * @version V2.0
 */
public interface FileUploadDao extends GenericDao<FileUpload, Long> {
	
	void executeLoad(String loadSql);

}
