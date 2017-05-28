package com.chenghui.agriculture.dao.projectManage;

import java.util.List;

import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.PictureUpload;


/**
 * 模版类型访问接口
 * @author yudq
 * @version V2.0
 */
public interface PictureUploadDao extends GenericDao<PictureUpload, Long> {
	
	void executeLoad(String loadSql);

}
