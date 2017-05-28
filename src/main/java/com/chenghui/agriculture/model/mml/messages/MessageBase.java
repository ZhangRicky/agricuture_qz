package com.chenghui.agriculture.model.mml.messages;
/**
 * @author Mars
 * @date 2014-02-24
 *
 */
public class MessageBase {

	//发送方监听的队列名称
	private String srcQ;
	
	//接收方监听的队列名称
	private String destQ;
	
	//当前请求的会话标识，使用不带分割符的小写uuid串
	private String session;
	
	//网元名称
	private String neName;

	public String getSrcQ() {
		return srcQ;
	}

	public void setSrcQ(String srcQ) {
		this.srcQ = srcQ;
	}

	public String getDestQ() {
		return destQ;
	}

	public void setDestQ(String destQ) {
		this.destQ = destQ;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}
	
	public String getNeName() {
		return neName;
	}

	public void setNeName(String neName) {
		this.neName = neName;
	}
}
