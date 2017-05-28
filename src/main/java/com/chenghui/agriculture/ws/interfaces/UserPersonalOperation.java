package com.chenghui.agriculture.ws.interfaces;

import java.util.Date;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface UserPersonalOperation {
	
	/**
	 * 普通居民用户登录
	 * @param idNumber 身份证号
	 * @param password 密码
	 * @return 如果登陆成功返回该用户的PersonalID，{"success":"1","personalId":"442"},如果不成功返回{"success":"0","errorMsg":"用户名密码不正确"}
	 */
	String login(@WebParam(name="idNumber")String idNumber, @WebParam(name="password") String password);
	
	/**
	 * 查询居民的诚信档案
	 * @param personalId 居民Id，personalinfo表中的ID字段
	 * @return {"success":"1","data":{"name":"吴七","IdNumber":"520113197301270022","village":"龚中居委会","fzValue":"103","cxLevel":"诚信居民","startLevel":"1"}}
	 * 		   没查着：{"success":"1","data":{}}
	 * 	              出错:{"success":"0","errorMsg":"请求超时"}
	 */
	String queryCreditRating( @WebParam(name="personalId") Long personalId);
	
	/**
	 * 查询居民诚信记录，只查询已经审核通过的记录
	 * @param personalId 居民Id，personalinfo表中的ID字段
	 * @param startTime 起始时间
	 * @param endTime 结束时间
	 * @return 成功：{"success":"1","data":[{"name":"吴七","IdNumber":"520113197301270022","village":"龚中居委会","xxlx":"诚信失分项","fzValue":"-2","xxydw":"公安局","fssj":"2013-10-10"}]}
	 * 			失败：{"success":"0","errorMsg":"请求超时"}
	 */			
	String queryCreditRecords( @WebParam(name="personalId") Long personalId,@WebParam(name="startTime") Date startTime,@WebParam(name="endTime") Date endTime);
}
