package com.kepler.tcm.exception;
/**
 * ApiSql自定义异常
 * @author wangsp
 * @date 2017年4月27日
 * @version V1.0
 */
public class ApiException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ApiException(String msg){  
        super(msg);  
    }
	
    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ApiException(Throwable cause) {
        super(cause);
    }
	
    protected ApiException(String message, Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
    	super(message, cause, enableSuppression, writableStackTrace);
	}
}
