package com.kakaopaysec.constent;

public enum ErrCode {
    /**
     * 에러코드 정의
     */
    // HttpStatus 500
    E0000("001", "예상치 못한 오류가 발생하였습니다."),
    
    // HttpStatus 400
    E0101("101", "서버 내부에서 처리 중에 에러가 발생했습니다."),
    E0102("102", "파라미터 확인부탁드립니다."),
    E0103("103", "서비스 점검 중입니다. 공지사항을 확인해주세요."),
    E0104("104", "헤더 정보 확인부탁드립니다.");


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
