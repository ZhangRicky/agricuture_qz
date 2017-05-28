package com.chenghui.agriculture.dao.projectManage;


import java.util.List;
import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.SubProject;

/**
 * 子项目DAO
 * @author LLJ
 * @version V1.0
 *
 */
public interface SubProjectDao extends GenericDao<SubProject, Long>{
	void executeLoad(String loadSql);
	/**
	 * 通过父项目Id查询所有子项目
	 * @param pId 父项目Id
	 * @return
	 */
	List<SubProject> getSubProjectsByPId(long pId);
	
	int subProjectNumber(Long projectId);

}
