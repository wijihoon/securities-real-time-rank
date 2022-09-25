package com.kakaopaysec.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakaopaysec.entity.InvestAgentVolInfo;
import com.kakaopaysec.entity.ItemInfo;
import com.kakaopaysec.entity.OhlcvInfo;
import com.kakaopaysec.entity.RankResponseInfo;
import com.kakaopaysec.repository.InvestAgentVolRepository;
import com.kakaopaysec.repository.ItemRepository;
import com.kakaopaysec.repository.OhlcvRepository;

@Service
public class RankRedisSevice {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	private ZSetOperations<String, String> zSetOps;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private OhlcvRepository ohlcvRepository;
	@Autowired
	private InvestAgentVolRepository investAgentVolRepository;

	@PostConstruct
	public void init() {
		zSetOps = redisTemplate.opsForZSet();
	}

	public List<RankResponseInfo> getRankingByCondition(String key, int startIndex, int endIndex) {
		
		Set<String> rankReverseSet;
		Optional<OhlcvInfo> ohlcvInfo = null;
		
		if("riseALot".equals(key))		rankReverseSet = zSetOps.reverseRange("volumeOfLot", startIndex, endIndex);
		else if("dropALot".equals(key)) rankReverseSet = zSetOps.range("volumeOfLot", startIndex, endIndex);
		else							rankReverseSet = zSetOps.reverseRange(key, startIndex, endIndex);
		
		Iterator<String> iter		= rankReverseSet.iterator();
		List<RankResponseInfo> list	= new ArrayList<>(rankReverseSet.size());
		
		String str	= "";
		Double i	= (double) 0;

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

		return list;
	}

	@Transactional
	public void updateRandomRank() {
		
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
    								 , ohlcvInfo.get().getClose().doubleValue());
    		//거래량 많은
    		zSetOps.add("volumeHigh", listItemInfo.get(i).getCode()
    								, investAgentVolInfo.get().getForeighVolume()
									.add(investAgentVolInfo.get().getIstttVolume())
									.add(investAgentVolInfo.get().getIndiVolume())
									.doubleValue());
    	}
	}
}
