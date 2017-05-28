package com.chenghui.agriculture.service.jms.impl;

import org.springframework.stereotype.Service;

/**
 * 检查结果临时交换服务
 * 1.向AMQ发送消息时，将检查记录临时存放此处
 * 2. 异步从AMQ接收到消息时，更新检查记录，并移除
 * @author kongdy
 *
 */
@Service("CheckResultCache")
public class CheckResultCache {
//	
//	private final static Logger logger = LoggerFactory.getLogger(ConsumerMessageListener.class);
//
//	@Autowired
//	CheckResultService checkResultService;
//
//	@Autowired
//	CheckOperationService checkOperationService;
//	
//	@Autowired
//	UnitService unitService;
//	
//	
//	@Autowired
//	NeOperationService neLogService;
//	
//	private String account;
//
//	private String newPassword;
//	
//	
//	/**
//	 * 执行机成功标识状态、存放单元ID，定时清理非0单元数据
//	 */
//	public static  Vector<Long> unitids = new Vector<Long>();
//	/**
//	 * /**
//	 * 使用方法：
//	 * 1.向AMQ发送消息前，将持久化的CheckResult添加到此，key为向AMQ所发消息的session
//	 * 2.从AMQ接收到消息后，按照session从该对象获取unitCache，更新检查结果并持久化，将unitCache从此移除
//	 */
//	private static Map<String, Unit> unitCache = new LinkedHashMap<String, Unit>();
//
//	/**
//	 * 使用方法：
//	 * 1.向AMQ发送消息前，将持久化的CheckResult添加到此，key为向AMQ所发消息的session
//	 * 2.从AMQ接收到消息后，按照session从该对象获取CheckResult，更新检查结果并持久化，将CheckResult从此移除
//	 */
//	private Map<String, CheckResult> resultCache = new LinkedHashMap<String, CheckResult>();
//
//	/**
//	 * 使用方法：
//	 * 1.向AMQ发送消息前，将持久化的CheckOperation添加到此，key为向AMQ所发消息的session
//	 * 2.从AMQ接收到消息后，按照session从该对象获取CheckOperation，将CheckOperation从此移除。
//	 *   移除后，判断CheckOperation是否还在该缓存中，若仍然存在，则说明CheckOperation所对应的CheckResult未完全得到结果；
//	 *   若不存在，则说明CheckOperation所对应的CheckResult全部得到结果，更新CheckOperation对象的done属性，并持久化。
//	 */
//	private Map<String, CheckOperation> operationCache = new LinkedHashMap<String, CheckOperation>();
//
//	/**
//	 * 将CheckResult、CheckOperation加入缓存前，应首先将两对象持久化
//	 * @param session 发送AMQ消息的唯一标识
//	 * @param checkResult 一个检查项
//	 * @param checkOperation 一次检查操作
//	 */
//	public void put(String session, CheckResult checkResult, CheckOperation checkOperation) {
//		resultCache.put(session, checkResult);
//		operationCache.put(session, checkOperation);
//	}
//	
//	public void put(String session, Unit unit) {
//		unitCache.put(session, unit);
//	}
//	
//	public void put(String session,String account,String newPassword,CheckResult checkResult,CheckOperation checkOperation){
//		resultCache.put(session, checkResult);
//		operationCache.put(session, checkOperation);
//		this.account = account;
//		this.newPassword = newPassword;
//	}
//
//	/**
//	 * 从AMQ接收到命令执行结果后执行该方法
//	 * @param session 发送AMQ消息的唯一标识
//	 * @param resultCode 检查项命令执行结果码
//	 * @param action 操作类型   0:增加  1:删除
//	 */
	public void persistUnit(String session, String resultCode,String action) {
//		if (unitCache.containsKey(session)) {
//			Unit unit = unitCache.get(session);
//			if("0".equals(action)){//增加单元
//				if(!"0".equals(resultCode)){//未成功、更新isSuccess标识
//					unit.setIsSuccess(Integer.parseInt(resultCode));
//					unitService.update(unit);
//				}else{
//					unitids.add(unit.getId());//存在单元Id，定期清理需要
//				}
//			}else if("2".equals(action)){//修改单元
//				if(!"0".equals(resultCode)){//修改未成功、重新updata unit
//					unit.setIsSuccess(Integer.parseInt(resultCode));
//					unitService.update(unit);
//				}
//			}else if("1".equals(action)){//删除单元
//				if(!"0".equals(resultCode)){//未删除成功重新添加入库、状态isSuccess为0
//					unit.setIsSuccess(0);
//					unitService.add(unit);
//				}
//			}
//			unitCache.remove(session);
//		}
	}
//	
//	
//	/**
//	 * 从AMQ接收到命令执行结果后执行该方法
//	 * @param unit 网元操作日志根据key返回
//	 * @param resultCode 检查项命令执行结果码
//	 * @param path 检查项命令结果日志路径
//	 */
//	public void persistNeLog(Unit unit, String resultCode,String path){
//		// 成功进行网元操作日志add
//		if ("66010".equalsIgnoreCase(resultCode)) {
//			if(null!=unit){
//				neLogService.addNeOperationLog(unit,path);
//			}
//		}
//	}
//	
//
//	/**
//	 * 从AMQ接收到命令执行结果后执行该方法
//	 * @param session 发送AMQ消息的唯一标识
//	 * @param resultCode 检查项命令执行结果码
//	 * @param log 检查项命令结果日志路径
//	 */
	public void persist(String session, String resultCode, String log) {
//		
//		if (resultCache.containsKey(session)) {
//			CheckResult checkResult = resultCache.get(session);
//			logger.debug("msession="+session+"resultCode="+resultCode+"location="+log +"itemName="+checkResult.getItemName());
//			
//			if("0".equalsIgnoreCase(resultCode) && 
//					  CheckItem.CHANGE_PASSWORD_ITEM.equalsIgnoreCase(checkResult.getItemName())) {
//				if (operationCache.containsKey(session)) {
//					CheckOperation checkOperation = operationCache.get(session);
//					Set<CheckResult> resultList = checkOperation.getResultList();
//					if (null != resultList) {
//						for (CheckResult result : resultList) {
//							  //TO-DO 更新 unit 密码
//							  Unit unit = unitService.get(result.getUnitId());
//							  if("root".equals(account)){
//								  unit.getAccountList().toArray(new Account [unit.getAccountList().size()])[0].setRootPassword(newPassword);
//							  }else{
//								String oldAccount =   unit.getAccountList().toArray(new Account [unit.getAccountList().size()])[0].getUsername();
//								if(oldAccount.equals(account)){
//									unit.getAccountList().toArray(new Account [unit.getAccountList().size()])[0].setPassword(newPassword);
//								}
//							  }
//							  unitService.update(unit);
//							
//						}
//					}
//				}
//				
//			}
//				
//			checkResult.setResultCode(resultCode);
//			checkResult.setLog(log);
//			checkResultService.update(checkResult);
//			resultCache.remove(session);
//			
//		}
//
//		if (operationCache.containsKey(session)) {
//			CheckOperation checkOperation = operationCache.get(session);
//			operationCache.remove(session);
//			if (!operationCache.containsValue(checkOperation)) {
//				
//				logger.debug("msession="+session+"resultCode="+resultCode+"==============");
//				checkOperation.setDone(CheckOperation.DONE);
//				checkOperationService.update(checkOperation);
//			}
//		}
	}
//
//	public static Map<String, Unit> getUnitCache() {
//		return unitCache;
//	}
//
}
