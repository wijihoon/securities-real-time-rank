package com.kakaopaysec.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionMessage {

    public Map<String, String> errMsg(String msg, String errCode){
        Map<String, String> result = new HashMap<>();
        result.put("code", errCode);
        result.put("messege", msg);
        return result;

    }

}
