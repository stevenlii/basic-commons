package com.paymoon.basic.commons.core;

public class TableException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String message;
	public TableException(){
		super();
	}
	
	public TableException(String message){
		super(message);
		this.message=message;
	}
	public TableException(String message,String code){
		super(message);
		this.code=code;
		this.message=message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
