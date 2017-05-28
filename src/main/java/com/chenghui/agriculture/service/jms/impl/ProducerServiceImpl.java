package com.chenghui.agriculture.service.jms.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.chenghui.agriculture.core.utils.JsonMapper;
import com.chenghui.agriculture.model.mml.messages.Message60003;
import com.chenghui.agriculture.model.mml.messages.Message60005;
import com.chenghui.agriculture.model.mml.messages.Message60009;
import com.chenghui.agriculture.service.jms.ProducerService;

/**
 * JMS生产者
 * @author kongdy
 *
 */
public class ProducerServiceImpl implements ProducerService {

	private final static Logger logger = LoggerFactory.getLogger(ProducerServiceImpl.class);

	
	private JmsTemplate jmsTemplate;
    private ActiveMQQueue queueSource;
    private ActiveMQQueue queueDestination;

	private JmsTemplate jmsTemplate2nd;
    private ActiveMQQueue queueSource2nd;
    private ActiveMQQueue queueDestination2nd;

    private static Map<String,String> routeMap = new HashMap<String,String>();
    private static Map<String,String> messageQueueMap = new HashMap<String,String>();
    
//    @Value("${lte.message_queue_mapping}")
    private String message_queue_mapping_str;
    
    @PostConstruct
    public void initRouteMap() {
//    	List<Unit> unitList = unitService.findAllWithDHLR();
//    	for (Unit unit : unitList) {
//    		logger.debug(unit.getUnit()+"===["+unit.getIp()+"]==>"+unit.getNe().getParent());
//    		routeMap.put(unit.getUnit(), unit.getNe().getParent());
//		}
//    	String[] tempArray = message_queue_mapping_str.split(";");
//    	for (String string : tempArray) {
//    		String[] item = string.split(":");
//    		if(item.length>1) {
//    			String compName = item[0];
//        		String DHLRNames = item[1];
//        		String[] DHLRNameArray = DHLRNames.split(",");
//        		for (String DHLRName : DHLRNameArray) {
//        			messageQueueMap.put(DHLRName, compName);
//				}
//    		}
//		}
    	logger.debug("message_queue_mapping_str"+messageQueueMap.toString());
    }

	@Override
	public void sendMessage60003(final String sessionId, final String ne, final String command, final String params) {
		getJmsTemplate(ne).send(getQueueDestination(ne), new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				Message60003 message = new Message60003();
				message.setCommandName(command);
				message.setCommandParams(params);
				message.setDestQ(getQueueDestination(ne).getQueueName());
				message.setExeTimeoutMinutes(30);
				message.setIdentification("");
				message.setNeName(ne);
				message.setSession(sessionId);
				message.setSrcQ(getQueueSource(ne).getQueueName());

				TextMessage txtMessage = session.createTextMessage("");
				txtMessage.setStringProperty("msgBody", new JsonMapper().toJson(message));
				txtMessage.setIntProperty("msgCode", 60003);
				txtMessage.setJMSPriority(9);
				logger.debug("message.getSrcQ() = {},message.getDestQ() = {}",message.getSrcQ(),message.getDestQ());
				logger.debug(txtMessage.toString());
				return txtMessage;
			}
		});
	}

	@Override
	public void sendMessage60005(final String sessionId, final String ne, final String command) {
		
		getJmsTemplate(ne).send(getQueueDestination(ne), new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				Message60005 message = new Message60005();
				message.setCommandName(command);
				message.setDestQ(getQueueDestination(ne).getQueueName());
				message.setExeTimeoutMinutes(30);
				message.setNeName(ne);
				message.setSession(sessionId);
				message.setSrcQ(getQueueSource(ne).getQueueName());

				TextMessage txtMessage = session.createTextMessage("");
				txtMessage.setStringProperty("msgBody", new JsonMapper().toJson(message));
				txtMessage.setIntProperty("msgCode", 60005);
				txtMessage.setJMSPriority(9);

				logger.debug("message.getSrcQ() = {},message.getDestQ() = {}",message.getSrcQ(),message.getDestQ());
				logger.debug(txtMessage.toString());
				return txtMessage;
			}
		});
	}
	
	@Override
	public void sendMessage60009(final String sessionId, final String neName,final String neTime,
			final String commandParams) {
		getJmsTemplate(neName).send(getQueueDestination(neName), new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				Message60009 message = new Message60009();
				message.setSession(sessionId);
				message.setNeName(neName);
				message.setDestQ(getQueueDestination(neName).getQueueName());
				message.setSrcQ(getQueueSource(neName).getQueueName());
				message.setCommandParams(commandParams);
				message.setMetataskName("DHLR_PGW");
				message.setCommandName("DHLR_PGW");
				message.setMetataskParams("1::"+neTime+";;2::"+neName);
				message.setExeTimeoutMinutes(30);
				message.setParseTimeoutMinutes(30);
				TextMessage txtMessage = session.createTextMessage("");
				txtMessage.setStringProperty("msgBody", new JsonMapper().toJson(message));
				txtMessage.setIntProperty("msgCode", 60009);
				txtMessage.setJMSPriority(9);
				logger.debug("message.getSrcQ() = {},message.getDestQ() = {}",message.getSrcQ(),message.getDestQ());
						logger.debug(txtMessage.toString());
						return txtMessage;
			}
		});
				
	}

//	@Override
//	public void sendMessage60013(final String sessionId, final Unit unit,final String action) {
//		getJmsTemplate(unit.getUnit()).send(getQueueDestination(unit.getUnit()), new MessageCreator() {
//			public Message createMessage(Session session) throws JMSException {
//				Message60013 message = new Message60013();
//				message.setDestQ(getQueueDestination(unit.getUnit()).getQueueName());
//				message.setSession(sessionId);
//				message.setSrcQ(getQueueSource(unit.getUnit()).getQueueName());
//				message.setNeName(unit.getUnit());
//				message.setIp(unit.getIp());
//				if("0".equals(action)){
//					Long pro = unit.getProtocol().getId();
//					String protocol = "";
//					if(pro==2){
//						protocol="Telnet";
//					}else if(pro==1){
//						protocol = "SSH2";
//					}
//					message.setProtocal(protocol);
//				}
//				message.setUsername(unit.getAccountList().toArray(new Account [unit.getAccountList().size()])[0].getUsername());
//				message.setPassword(unit.getAccountList().toArray(new Account [unit.getAccountList().size()])[0].getHexPassword());
//				message.setPort(unit.getPort()+"");
//				message.setAction(action);
//				TextMessage txtMessage = session.createTextMessage("");
//				txtMessage.setStringProperty("msgBody", new JsonMapper().toJson(message));
//				txtMessage.setIntProperty("msgCode", 60013);
//				logger.debug("message.getSrcQ() = {},message.getDestQ() = {}",message.getSrcQ(),message.getDestQ());
//				logger.debug(txtMessage.toString()); 
//				return txtMessage;
//			}
//		});
//	}


	private JmsTemplate getJmsTemplate(String ne) {

		String compName = routeMQ(ne);
		if(compName.equalsIgnoreCase("COMP01")) {
			return getJmsTemplate();
		}else if(compName.equalsIgnoreCase("COMP02")){
			return getJmsTemplate2nd();
		}
		return null;
	}


	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public ActiveMQQueue getQueueSource() {
		return queueSource;
	}

	public void setQueueSource(ActiveMQQueue queueSource) {
		this.queueSource = queueSource;
	}

	public ActiveMQQueue getQueueDestination() {
		return queueDestination;
	}

	public void setQueueDestination(ActiveMQQueue queueDestination) {
		this.queueDestination = queueDestination;
	}

	public JmsTemplate getJmsTemplate2nd() {
		return jmsTemplate2nd;
	}

	public void setJmsTemplate2nd(JmsTemplate jmsTemplate2nd) {
		this.jmsTemplate2nd = jmsTemplate2nd;
	}

	public ActiveMQQueue getQueueSource2nd() {
		return queueSource2nd;
	}

	public void setQueueSource2nd(ActiveMQQueue queueSource2nd) {
		this.queueSource2nd = queueSource2nd;
	}

	public ActiveMQQueue getQueueDestination2nd() {
		return queueDestination2nd;
	}

	public void setQueueDestination2nd(ActiveMQQueue queueDestination2nd) {
		this.queueDestination2nd = queueDestination2nd;
	}
	
	private ActiveMQQueue getQueueSource(String ne) {
		String compName = routeMQ(ne);
		if(compName.equalsIgnoreCase("COMP01")) {
			return getQueueSource();
		}else if(compName.equalsIgnoreCase("COMP02")){
			return getQueueSource2nd();
		}
		return null;
	}


	private ActiveMQQueue getQueueDestination(String ne) {
		String compName = routeMQ(ne);
		if(compName.equalsIgnoreCase("COMP01")) {
			return getQueueDestination();
		}else if(compName.equalsIgnoreCase("COMP02")){
			return getQueueDestination2nd();
		}
		return null;

	}

	private String routeMQ(String ne) {
		String dhlrName = routeMap.get(ne);
		String compName = messageQueueMap.get(dhlrName);
		logger.debug("QueueSource : CompName is {} ,DHLRName is {},UnitName is {}", compName,dhlrName,ne);
		return compName;
	}
}
