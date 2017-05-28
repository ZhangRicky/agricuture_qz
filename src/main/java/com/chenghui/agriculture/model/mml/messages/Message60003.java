package com.chenghui.agriculture.model.mml.messages;


/**
 * @author Mars
 * @date 2014-02-24
 *
 */

public class Message60003 extends MessageSendBase {

	//组合指令参数
	private String commandParams;
	
	//vip
	private String identification;
	
	public String getCommandParams() {
		return commandParams;
	}

	public void setCommandParams(String commandParams) {
		this.commandParams = commandParams;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}
	
}
