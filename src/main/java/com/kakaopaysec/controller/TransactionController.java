package com.kakaopaysec.controller;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopaysec.constent.ErrCode;
import com.kakaopaysec.service.TransactionService;
import com.kakaopaysec.util.ResponseSender;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stock")
public class TransactionController {

    private static Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;
    private final ResponseSender responseSender;

    /**
	 * Desc : 주제별 랭킹 조회 API, 모든 주제 랭킹 조회 API
	 * @Date    : 2022. 9. 27. 오후 1:23:00
	 * @Author  : Jihoon Wi
	 * @param Integer id, Integer paging
	 * @return ResponseEntity<Object>
	 */
    @GetMapping(value = {"/rank/{id}", "/rank"})
    public ResponseEntity<Object> searchRankList(@PathVariable(required = false) Integer id, @RequestParam(required = false) Integer paging){
        
    	Map<String, Object> rankList;
    	
        if((id != null && id > 3)) {
        	//순위 조회 시 주제를 선택해주세요.
        	return responseSender.send(ErrCode.E0102.getCode(), ErrCode.E0102.getErrMsg());
        }else if(paging != null && paging > 100){
        	//순위는 최대 100건만 조회가 가능합니다.
        	return responseSender.send(ErrCode.E0103.getCode(), ErrCode.E0103.getErrMsg());
        }else {
        	try {
        		rankList = transactionService.searchRankList(id, paging);
        	} catch (Exception e) {
        		//처리 중에 에러가 발생했습니다.
        		logger.error(e.toString());
        		return responseSender.send(ErrCode.E0101.getCode(), ErrCode.E0101.getErrMsg());
        	}
        }
        
        return responseSender.send(200, rankList);
    }
    
    /**
	 * Desc : 순위 랜덤 변경 API
	 * @Date    : 2022. 9. 27. 오후 1:23:00
	 * @Author  : Jihoon Wi
	 * @param 
	 * @return ResponseEntity<Object>
	 */
    @PostMapping(value = {"/random"})
    public ResponseEntity<Object> updateRandomRank(){
        try {
        	transactionService.updateRandomRank();
            return responseSender.send(201, "success");
        } catch (Exception e) {
        	//순위 랜덤 변경 중 오류가 발생했습니다.
            logger.error(e.toString());
            return responseSender.send(ErrCode.E0104.getCode(), ErrCode.E0104.getErrMsg());
        }
    }

}
