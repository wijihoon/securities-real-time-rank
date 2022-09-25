package com.kakaopaysec.constent;

public enum CommCode {
    /**
     * 공통코드 정의
     */
    // 금융회사코드 (fico_cd)
	BK000("000",	"카카오뱅크"),
    BK001("001",	"한국은행"),
    BK002("002",	"산업은행"),
    BK003("003",	"기업은행"),
    BK004("004",	"국민은행"),
    
    // 입출금알람코드 (depo_mnrc_ar_cd)
    AC001("001", "STEP01"),
    AC002("002", "STEP02"),
    AC003("003", "STEP03"),
    AC004("004", "STEP04"),
	
	// 입출금상태코드 (depo_mnrc_stat_cd)
    SC001("001", "요청"),
    SC002("002", "완료"),
    SC003("003", "취소"),
    SC004("004", "실패"),

	// 입출금구분코드 (depo_mnpc_cd)
    DM001("001", "입금"),
    DM002("002", "출금"),
	
	// 시스템직원번호 (sys_emno)
    SE001("MB99999", "모바일"),
    SE002("HP99999", "홈페이지");

    public final String code;
    public final String codeNm;

    CommCode(String code, String codeNm){
        this.code = code;
        this.codeNm = codeNm;
    }

    public String getCode(){
        return code;
    }

    public String getCodeNm(){
        return  codeNm;
    }
}