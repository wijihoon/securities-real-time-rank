package com.kakaopaysec.exception;

import org.springframework.http.HttpStatus;

public class ServiceException  extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private final HttpStatus ERR_CODE;

    public ServiceException(String msg, HttpStatus errCode){
        super(msg);
        ERR_CODE = errCode;
    }

    public HttpStatus getERR_CODE(){
        return ERR_CODE;
    }

}
