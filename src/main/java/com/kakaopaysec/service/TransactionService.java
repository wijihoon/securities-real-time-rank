package com.kakaopaysec.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kakaopaysec.controller.TransactionController;
import com.kakaopaysec.exception.ServiceException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

public static final String[] KEY = { "viewALot", "riseALot", "dropALot", "volumeHigh" };
	
	private static Logger logger = LoggerFactory.getLogger(TransactionController.class);
	private final RankRedisSevice rankRedisSevice;

	public Map<String, Object> searchRankList(Integer id, Integer paging) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			if(id == null) {
				for (String str : KEY) {
					resultMap.put(str, rankRedisSevice.getRankingByCondition(str, 0, 4));
				}
			}else {
				resultMap.put(KEY[id], rankRedisSevice.getRankingByCondition(KEY[id], 0, paging == null ? 19 : --paging));
			}

			if (resultMap.size() == 0) {
				throw new ServiceException("데이터가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
			}

		} catch (ServiceException se) {
			logger.error(se.toString());
			throw se;
		} catch (Exception e) {
			logger.error(e.toString());
			throw e;
		}

		return resultMap;
	}
	
}
