package com.chenghui.agriculture.service.jms;


/**
 * JMS生产者
 *
 */
public interface ProducerService {
	public void sendMessage60003(String sessionId, String ne, String command, String params);
	
	public void sendMessage60005(String sessionId, String ne, String command);

//	public void sendMessage60013(String sessionId, Unit unit,String action);
	
	public void sendMessage60009( String sessionId, String neName,String neTime, String commandParams);

	
}
