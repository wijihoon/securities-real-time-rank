package com.kakaopaysec.constent;

public enum ErrCode {
    /**
     * 에러코드 정의
     */
    // HttpStatus 500
    E0000("001", "예상치 못한 오류가 발생하였습니다."),
    
    // HttpStatus 400
    E0101("101", "Bad Request"),
    E0102("102", "은행/계좌정보를 확인해주세요."),
    E0103("103", "계좌 잔액을 확인해주세요."),
    E0104("104", "이체알림 전송중 오류가 발생했습니다."),
	E0105("105", "세션이 존재하지 않습니다.");


    public final String code;
    public final String errMsg;

    ErrCode(String code, String errMsg){
        this.code = code;
        this.errMsg = errMsg;
    }

    public String getCode(){
        return code;
    }

    public String getErrMsg(){
        return  errMsg;
    }
}