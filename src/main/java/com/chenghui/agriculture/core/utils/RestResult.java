package com.chenghui.agriculture.core.utils;

public class RestResult {
	private String title;
	private Object message;
	private Integer resultCode;
	public RestResult(String title, Integer resultCode, String message) {
		this.title = title;
		this.message = message;
		this.resultCode = resultCode;
	}
	public RestResult(String title, String message) {
		this.title = title;
		this.message = message;
		this.resultCode = 0;
	}
	public RestResult(Integer resultCode, String message) {
		this.resultCode = resultCode;
		this.message = message;
	}
	public Object getMessage() {
		return message;
	}
	public void setMessage(Object message) {
		this.message = message;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getResultCode() {
		return resultCode;
	}
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}
	
}
