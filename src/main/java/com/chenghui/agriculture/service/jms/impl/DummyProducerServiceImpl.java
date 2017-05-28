package com.chenghui.agriculture.service.jms.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chenghui.agriculture.service.jms.ProducerService;

/**
 * JMS生产者
 * @author kongdy
 *
 */
@Service("producerService")
public class DummyProducerServiceImpl implements ProducerService {
	private static final Logger logger = LoggerFactory.getLogger(DummyProducerServiceImpl.class);
	@Override
	public void sendMessage60003(String sessionId, String ne, String command,String params) {
		logger.info(params);
	}

	@Override
	public void sendMessage60005(String sessionId, String ne, String command) {
		logger.info(command);
	}

//	@Override
//	public void sendMessage60013(String sessionId, Unit unit,String action) {
//		logger.info(unit.toString());//			
//	}

	@Override
	public void sendMessage60009(String sessionId, String neName,String neTime,
			String commandParams) {
		logger.info(commandParams.toString());
		
	}
	
}
