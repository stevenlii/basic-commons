package com.paymoon.basic.commons.exception;
public class BaseException extends Exception {  
  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String retCd ;  //异常对应的返回码  
    private String msgDes;  //异常对应的描述信息  
      
    public BaseException() {  
        super();  
    }  
  
    public BaseException(String message) {  
        super(message);  
        msgDes = message;  
    }  
  
    public BaseException(String retCd, String msgDes) {  
        super(msgDes);  
        this.retCd = retCd;  
        this.msgDes = msgDes;  
    }  
  
    public String getRetCd() {  
        return retCd;  
    }  
  
    public String getMsgDes() {  
        return msgDes;  
    }  
}  