package com.chenghui.agriculture.service.jms.impl;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.chenghui.agriculture.core.utils.JsonMapper;

/**
 * JMS消费者
 * @author kongdy
 *
 */
public class ConsumerMessageListener implements MessageListener {
	private final static Logger logger = LoggerFactory.getLogger(ConsumerMessageListener.class);

//	private static  final String  SHARE_FILE_NAME="/report";
	
//	@Value("${lte.comp_base_dir}")
	private String SHARE_FILE_NAME;
	
	@Autowired
	private CheckResultCache checkResultCache;



	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(Message message) {
		String msgBody = null;
		Integer msgCode = new Integer(0);

		try {
			
			msgBody = message.getStringProperty("msgBody");
			msgCode = message.getIntProperty("msgCode");
			logger.debug("消息返回信息：{}",msgBody);
			logger.debug("消息返回Code：{}",msgCode);


		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (null != msgBody) {
			Map<String, String> json = (Map<String, String>) new JsonMapper().fromJson(msgBody, Map.class);
			String session = String.valueOf(json.get("session"));
			String resultCode = String.valueOf(json.get("resultCode"));
			
//			String msgCode = String.valueOf(json.get("msgCode"));//根据msgCode来区分执行类型
			if(msgCode==60014){
				String action = String.valueOf(json.get("action"));
				checkResultCache.persistUnit(session, resultCode,action);
				logger.debug("session="+session+"resultCode="+resultCode);
			}else if(msgCode==60004){
				//resultCode.equalsIgnoreCase("0") 表示正常返回结果，需要保存 Log 的路径，
				//否则返回的是错误日志，直接入库 
//				 Map<String, Unit> mapUnit = CheckResultCache.getUnitCache();
//				 if(mapUnit.containsKey(session)){//网元操作日志add处理
//					 Unit unit = mapUnit.get(session);
//					 String msg = String.valueOf(json.get("message"));//网元操作日志返回msg
//					 String path = "";
//					 if("66010".equals(resultCode) && msg.indexOf("3933")!=-1){
//						 path =  msg.substring(msg.indexOf(unit.getUnit()), msg.indexOf(";"));
//					 }
//					 logger.debug("session="+session+"resultCode="+resultCode+"path="+path);
//					 checkResultCache.persistNeLog(unit, resultCode, path);
//				 }else{
//					 String location = (resultCode.equalsIgnoreCase("0")?SHARE_FILE_NAME:"") + String.valueOf(json.get("message"));
//					 checkResultCache.persist(session, resultCode, location);
//					 logger.debug("session="+session+"resultCode="+resultCode+"location="+location);
//				 }
				
			}else if(msgCode==60006){
				//resultCode.equalsIgnoreCase("0") 表示正常返回结果，需要保存 Log 的路径，
				//否则返回的是错误日志，直接入库 
				String location = (resultCode.equalsIgnoreCase("0")?SHARE_FILE_NAME:"") + String.valueOf(json.get("message"));
				checkResultCache.persist(session, resultCode, location);
				logger.debug("session="+session+"resultCode="+resultCode+"location="+location);
				
			}else {
				logger.error("UNKNOWN msgCode:"+msgCode);
			}

		}
	}
}
