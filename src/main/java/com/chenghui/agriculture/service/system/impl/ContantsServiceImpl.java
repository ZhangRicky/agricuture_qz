package com.chenghui.agriculture.service.system.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.chenghui.agriculture.core.annotation.SystemOperationLogAnnotation;
import com.chenghui.agriculture.core.constant.AppModule;
import com.chenghui.agriculture.core.constant.OperationType;
import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.dao.system.ConstantsDao;
import com.chenghui.agriculture.model.Constants;
import com.chenghui.agriculture.service.GenericServiceImpl;
import com.chenghui.agriculture.service.system.ConstantsService;
@Service("constantsService")
public class ContantsServiceImpl extends GenericServiceImpl<Constants, Long> implements ConstantsService {
	
	@Autowired
	private ConstantsDao constantsDao;

	public ConstantsDao getConstantsDao() {
		return constantsDao;
	}

	public void setConstantsDao(ConstantsDao constantsDao) {
		this.constantsDao = constantsDao;
	}
		
	public List<Constants> getXxlx(){
		List<Constants> xxlx = constantsDao.getConstantsByType(Constants.TYPE_XXLX);
		return xxlx;
	}

	@Override
	public List<Constants> getXxydw() {
		List<Constants> xxydw = constantsDao.getConstantsByType(Constants.TYPE_XXYDW);
		return xxydw;
	}

	@Override
	public List<Constants> getJmlx() {
		List<Constants> jmlx = constantsDao.getConstantsByType(Constants.TYPE_JMLX);
		return jmlx;
	}

	//获取所有的信息类型，
	@Override
	public List<Constants> getXxlxType() {
		List<Constants> xxlx = constantsDao.getByType(Constants.TYPE_XXLX);
		return xxlx;
	}

	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.MAINTENANCE_CN, opType=OperationType.Update , opText="修改信息类别")
	public void updateConstants(Constants constants) {
		Constants c =  constantsDao.get(constants.getId());
		if(!c.getDisplayName().equals(constants.getDisplayName())){
			if(constantsDao.getConstantsByDisplayName(constants)!=null){
				throw new BusinessServiceException("修改类别失败，类别名称不能重复！");
			}
		}
		c.setId(constants.getId());
		c.setDisplayName(constants.getDisplayName());
		c.setOrderBy(constants.getOrderBy());
		try{
			constantsDao.update(c);
		}catch(Exception e){
			throw new BusinessServiceException("修改类别失败！");
		}
		
	}

	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.MAINTENANCE_CN, opType=OperationType.Update , opText="修改居民")
	public void updateJuminConstants(Constants constants) {
		Constants c =  constantsDao.get(constants.getId());
		if(!c.getDisplayName().equals(constants.getDisplayName())){
			if(constantsDao.getConstantsByDisplayName(constants)!=null){
				throw new BusinessServiceException("修改类别失败，类别名称不能重复！");
			}
		}
		c.setId(constants.getId());
		c.setDisplayName(constants.getDisplayName());
		c.setOrderBy(constants.getOrderBy());
		try{
			constantsDao.update(c);
		}catch(Exception e){
			throw new BusinessServiceException("修改类别失败！");
		}
		
	}
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.MAINTENANCE_CN, opType=OperationType.Update , opText="修改信息源单位")
	public void updateXxDanWeiConstants(Constants constants) {
		Constants c =  constantsDao.get(constants.getId());
		if(!c.getDisplayName().equals(constants.getDisplayName())){
			if(constantsDao.getConstantsByDisplayName(constants)!=null){
				throw new BusinessServiceException("修改类别失败，类别名称不能重复！");
			}
		}
		c.setId(constants.getId());
		c.setDisplayName(constants.getDisplayName());
		c.setOrderBy(constants.getOrderBy());
		try{
			constantsDao.update(c);
		}catch(Exception e){
			throw new BusinessServiceException("修改类别失败！");
		}
		
	}
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.MAINTENANCE_CN, opType=OperationType.Add, opText="添加信息类别")
	public Long addConstants(Constants constants) {
		if(constantsDao.getConstantsByDisplayName(constants) != null){
			throw new BusinessServiceException("系统中已存在名为"+constants.getDisplayName()+" 的类别");
		}
		constants.setIsDeleted(false);
		try {
			constantsDao.add(constants);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessServiceException(e.getLocalizedMessage());
		}
		return constants.getId();
	}
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.MAINTENANCE_CN, opType=OperationType.Add, opText="添加信息源单位")
	public Long addXxDanWeiConstants(Constants constants) {
		if(constantsDao.getConstantsByDisplayName(constants) != null){
			throw new BusinessServiceException("系统中已存在名为"+constants.getDisplayName()+" 的类别");
		}
		constants.setIsDeleted(false);
		try {
			constantsDao.add(constants);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessServiceException(e.getLocalizedMessage());
		}
		return constants.getId();
	}
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.MAINTENANCE_CN, opType=OperationType.Add, opText="添加居民")
	public Long addJuminConstants(Constants constants) {
		if(constantsDao.getConstantsByDisplayName(constants) != null){
			throw new BusinessServiceException("系统中已存在名为"+constants.getDisplayName()+" 的类别");
		}
		constants.setIsDeleted(false);
		try {
			constantsDao.add(constants);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessServiceException(e.getLocalizedMessage());
		}
		return constants.getId();
	}
//居民类型
	@Override
	public List<Constants> getJuminglxType() {
		List<Constants> xxlx = constantsDao.getByType(Constants.TYPE_JMLX);
		return xxlx;
	}

	//启用
	@Override
	public void resourceConstants(Long constants_id) {
		Constants c = constantsDao.get(constants_id);
		if(c.getIsDeleted()){
			c.setIsDeleted(false);
		}else{
			c.setIsDeleted(true);
		}
		constantsDao.update(c);
		
	}

	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.MAINTENANCE_CN, opType=OperationType.Del, opText="删除信息类别")
	public void removeConstants(Long constants_id) {
		Constants c = constantsDao.get(constants_id);
		constantsDao.remove(c);
		
	}
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.MAINTENANCE_CN, opType=OperationType.Del, opText="删除信息源单位")
	public void removeXxDanWeiConstants(Long constants_id) {
		Constants c = constantsDao.get(constants_id);
		constantsDao.remove(c);
		
	}
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.MAINTENANCE_CN, opType=OperationType.Del, opText="删除居民")
	public void removeJuminConstants(Long constants_id) {
		Constants c = constantsDao.get(constants_id);
		constantsDao.remove(c);
		
	}
	
	@Override
	public List<Constants> getXxDanWeiType() {
		List<Constants> xxlx = constantsDao.getByType(Constants.TYPE_XXYDW);
		return xxlx;
	}
	
	

}
