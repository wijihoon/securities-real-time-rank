package com.kakaopaysec.constent;

public enum ErrCode {
    
    /**
     * 에러코드 정의
     */
    
    // HttpStatus 400
    E0101(-1, "처리 중에 에러가 발생했습니다."),
    E0102(-2, "파라미터 확인부탁드립니다."),
	E0103(-770, "순위 조회 시 주제를 선택해주세요."),
	E0104(-771, "순위는 최대 100건만 조회가 가능합니다."),
	E0105(-772, "순위 정보가 존재하지 않습니다."),
	E0106(-773, "순위 랜덤 변경 중 오류가 발생했습니다.);


    public final int code;
    public final String errMsg;

    ErrCode(int code, String errMsg){
        this.code = code;
        this.errMsg = errMsg;
    }

    public int getCode(){
        return code;
    }

    public String getErrMsg(){
        return  errMsg;
    }
}
