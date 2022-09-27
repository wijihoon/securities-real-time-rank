package com.kakaopaysec.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakaopaysec.controller.TransactionController;
import com.kakaopaysec.entity.InvestAgentVolInfo;
import com.kakaopaysec.entity.ItemInfo;
import com.kakaopaysec.entity.OhlcvInfo;
import com.kakaopaysec.entity.RankResponseInfo;
import com.kakaopaysec.repository.InvestAgentVolRepository;
import com.kakaopaysec.repository.ItemRepository;
import com.kakaopaysec.repository.OhlcvRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

public static final String[] KEY = { "viewALot", "riseALot", "dropALot", "volumeHigh" };
	
	private static Logger logger = LoggerFactory.getLogger(TransactionController.class);
	
	private RedisTemplate<String, String> redisTemplate;
	private ZSetOperations<String, String> zSetOps;
	private ItemRepository itemRepository;
	private OhlcvRepository ohlcvRepository;
	private InvestAgentVolRepository investAgentVolRepository;
	
	@PostConstruct
	public void init() {
		zSetOps = redisTemplate.opsForZSet();
	}
	
	/**
	 * Desc : 주제별 랭킹 조회, 모든 주제 랭킹 조회
	 * @Date    : 2022. 9. 27. 오후 1:23:00
	 * @Author  : Jihoon Wi
	 * @param Integer id, Integer paging
	 * @return Map<String, Object>
	 */
	public Map<String, Object> searchRankList(Integer id, Integer paging) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			
			if(id == null) {
				for (String str : KEY) {
					resultMap.put(str, getRankingByCondition(str, 0, 4));
				}
			}else {
				resultMap.put(KEY[id], getRankingByCondition(KEY[id], 0, paging == null ? 19 : --paging));
			}

			if (resultMap.size() == 0) {
				throw new Exception("searchRankList > select > ");
			}

		} catch (Exception e) {
			logger.error(e.toString());
			throw new Exception("searchRankList > select > " + e);
		}

		return resultMap;
	}
	
	/**
	 * Desc : 주제별 랭킹 조회
	 * @Date    : 2022. 9. 27. 오후 1:23:00
	 * @Author  : Jihoon Wi
	 * @param String key, int startIndex, int endIndex
	 * @return List<RankResponseInfo>
	 */
	public List<RankResponseInfo> getRankingByCondition(String key, int startIndex, int endIndex) throws Exception{
		
		List<RankResponseInfo> list = null;
		
		try {
			
			Set<String> rankReverseSet;
			
			if("riseALot".equals(key))		rankReverseSet = zSetOps.reverseRange("volumeOfLot", startIndex, endIndex);
			else if("dropALot".equals(key)) rankReverseSet = zSetOps.range("volumeOfLot", startIndex, endIndex);
			else							rankReverseSet = zSetOps.reverseRange(key, startIndex, endIndex);
			
			Iterator<String> iter		= rankReverseSet.iterator();
			list	= new ArrayList<>(rankReverseSet.size());
			
			String str	= "";
			Double i	= (double) 0;
			
			Optional<OhlcvInfo> ohlcvInfo;
			
			while (iter.hasNext()) {
				str = iter.next();
				ohlcvInfo = ohlcvRepository.findByCode(str);
				
				list.add(new RankResponseInfo(str
						, itemRepository.findByCode(str).getCodeName()
						, ++i
						, ohlcvInfo.get().getClose()
						, ohlcvInfo.get().getClose().subtract(ohlcvInfo.get().getOpen())
						.divide(ohlcvInfo.get().getOpen(), 4, RoundingMode.FLOOR)
						.multiply(new BigDecimal("100"))));
			}
			
		} catch (Exception e) {
			logger.error(e.toString());
			throw new Exception("searchRankList > select > getRankingByCondition" + e);
		}

		return list;
	}

	/**
	 * Desc : 순위 랜덤 변경
	 * @Date    : 2022. 9. 27. 오후 1:23:00
	 * @Author  : Jihoon Wi
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateRandomRank() throws Exception{
		
		List<ItemInfo> listItemInfo	= itemRepository.findAll();
		Optional<InvestAgentVolInfo> investAgentVolInfo;
		Optional<OhlcvInfo> ohlcvInfo;
		
    	for(int i = 0; i < listItemInfo.size(); i++) {
    		
    		ohlcvInfo = ohlcvRepository.findByCode(listItemInfo.get(i).getCode());
    		
    		/**
        	 * 캔들정보 update
        	 * 0 = code
        	 * 1 = windowSize	(default = 1min)
        	 * 2 = timestamp
        	 * 3 = open
        	 * 4 = close
        	 */
    		if(i%2 == 0) {
				ohlcvRepository.save(new OhlcvInfo(listItemInfo.get(i).getCode()
					    						, BigDecimal.ONE
					    						, LocalDateTime.now()
					    						, ohlcvInfo.get().getOpen()
					    						, ohlcvInfo.get().getClose().subtract(new BigDecimal(Integer.toString(new Random().nextInt(1000))))
					    						));
    		}else {
				ohlcvRepository.save(new OhlcvInfo(listItemInfo.get(i).getCode()
													, BigDecimal.ONE
													, LocalDateTime.now()
													, ohlcvInfo.get().getOpen()
													, ohlcvInfo.get().getClose().add(new BigDecimal(Integer.toString(new Random().nextInt(1000))))
						    						));
    		}
    		
    		ohlcvInfo = ohlcvRepository.findByCode(listItemInfo.get(i).getCode());
    		
    		/**
        	 * 거래주체별거래량 update
        	 * 0 = code
        	 * 1 = windowSize 		(default = 1min)
        	 * 2 = timestamp
        	 * 3 = foreighVolume 	(default = Random().nextInt(10000))
        	 * 4 = istttVolume 		(default = Random().nextInt(10000))
        	 * 5 = indiVolume 		(default = Random().nextInt(10000))
        	 * 6 = see 				(default = Random().nextInt(10000))
        	 */
			investAgentVolRepository.save(new InvestAgentVolInfo(listItemInfo.get(i).getCode()
					    										, BigDecimal.ONE
					    										, LocalDateTime.now()
				    											, new BigDecimal(Integer.toString(new Random().nextInt(1000)))
				    											, new BigDecimal(Integer.toString(new Random().nextInt(1000)))
				    											, new BigDecimal(Integer.toString(new Random().nextInt(1000)))
				    											, new BigDecimal(Integer.toString(new Random().nextInt(1000)))
																));
    		
    		investAgentVolInfo = investAgentVolRepository.findByCode(listItemInfo.get(i).getCode());
    		
    		//많이 본
    		zSetOps.add("viewALot", listItemInfo.get(i).getCode()
    							  , investAgentVolInfo.get().getSee().doubleValue());
    		//많이 오른, 많이 내린
    		zSetOps.add("volumeOfLot", listItemInfo.get(i).getCode()
    								 , ohlcvInfo.get().getClose()
    								 	.subtract(ohlcvInfo.get().getOpen())
										.divide(ohlcvInfo.get().getOpen(), 4, RoundingMode.FLOOR)
										.multiply(new BigDecimal("100")).doubleValue());
    		//거래량 많은
    		zSetOps.add("volumeHigh", listItemInfo.get(i).getCode()
    								, investAgentVolInfo.get().getForeighVolume()
									.add(investAgentVolInfo.get().getIstttVolume())
									.add(investAgentVolInfo.get().getIndiVolume())
									.doubleValue());
    	}
	}
	
}
