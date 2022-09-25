package com.kakaopaysec.controller;


import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopaysec.constent.ErrCode;
import com.kakaopaysec.exception.ExceptionMessage;
import com.kakaopaysec.exception.ServiceException;
import com.kakaopaysec.service.RankRedisSevice;
import com.kakaopaysec.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TransactionController {

    private static Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private static final HttpHeaders httpHeaders = new HttpHeaders();
    private final ExceptionMessage exceptionMessage;
    private final TransactionService transactionService;
    private final RankRedisSevice rankRedisSevice;

    @GetMapping(value = {"/rank/{id}", "/rank"})
    public ResponseEntity<?> searchRankList(@PathVariable(required = false) Integer id, @RequestParam(required = false) Integer paging){
        try {
        	
            if((id != null && id > 3)
	    || (paging != null && paging > 100)) {
        	return new ResponseEntity<>(exceptionMessage.errMsg(ErrCode.E0102.getErrMsg(), ErrCode.E0102.getCode()) ,httpHeaders,HttpStatus.BAD_REQUEST);
            }
        	
            return new ResponseEntity<Map<String, Object>>(transactionService.searchRankList(id, paging), httpHeaders, HttpStatus.OK);
        	
        }catch (ServiceException se){
            logger.error(se.toString());
            return new ResponseEntity<>(exceptionMessage.errMsg(ErrCode.E0101.getErrMsg(), ErrCode.E0101.getCode()) ,httpHeaders,se.getERR_CODE());
        } catch (Exception e) {
            logger.error(e.toString());
            return new ResponseEntity<>(exceptionMessage.errMsg(ErrCode.E0000.getErrMsg(), ErrCode.E0000.getCode()), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(value = {"/randomRank"})
    public ResponseEntity<?> updateRandomRank(){
        try {
            rankRedisSevice.updateRandomRank();
            return new ResponseEntity<>(exceptionMessage.errMsg("success", String.valueOf("000")), httpHeaders, HttpStatus.OK);
        }catch (ServiceException se){
            logger.error(se.toString());
            return new ResponseEntity<>(exceptionMessage.errMsg(ErrCode.E0101.getErrMsg(), ErrCode.E0101.getCode()) ,httpHeaders,se.getERR_CODE());
        } catch (Exception e) {
            logger.error(e.toString());
            return new ResponseEntity<>(exceptionMessage.errMsg(ErrCode.E0000.getErrMsg(), ErrCode.E0000.getCode()), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
