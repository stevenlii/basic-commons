package com.paymoon.basic.commons.exception;
public class NullRecordExistsException extends BaseException {  
  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String retCd ;  //异常对应的返回码  
    private String msgDes;  //异常对应的描述信息  
      
    public NullRecordExistsException() {  
        super();  
    }  
  
    public NullRecordExistsException(String message) {  
        super(message);  
        msgDes = message;  
    }  
  
    public NullRecordExistsException(String retCd, String msgDes) {  
        super(retCd,msgDes);  

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