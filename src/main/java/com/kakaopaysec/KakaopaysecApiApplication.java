package com.kakaopaysec;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

import com.kakaopaysec.entity.InvestAgentVolInfo;
import com.kakaopaysec.entity.ItemInfo;
import com.kakaopaysec.entity.OhlcvInfo;
import com.kakaopaysec.repository.InvestAgentVolRepository;
import com.kakaopaysec.repository.ItemRepository;
import com.kakaopaysec.repository.OhlcvRepository;
import com.opencsv.CSVReader;

@SpringBootApplication
public class KakaopaysecApiApplication {
	
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private InvestAgentVolRepository investAgentVolRepository;
	@Autowired
	private OhlcvRepository ohlcvRepository;
	@Autowired
    private RedisTemplate<String, String> redisTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(KakaopaysecApiApplication.class, args);
	}

    @Bean
    public CommandLineRunner commandLineRunner() {
        return (args) -> {
            try {
            	Optional<InvestAgentVolInfo> investAgentVolInfo;
            	CSVReader csvReader = new CSVReader(new FileReader("/kakaopaysec/src/main/resources/data/SampleData.csv"));
            	String[] strArr;
            	
                while ((strArr = csvReader.readNext()) != null) {
                	
                	/**
                	 * 종목정보 save
                	 * 0 = code
                	 * 1 = code_name
                	 */
                	itemRepository.save(new ItemInfo(strArr[1]
                									, strArr[2]));
                	
                	/**
                	 * 캔들정보 save
                	 * 0 = code
                	 * 1 = windowSize	(default = 1min)
                	 * 2 = timestamp
                	 * 3 = open 		(default = 초기 금액)
                	 * 4 = close 		(default = 초기 금액)
                	 */
                	ohlcvRepository.save(new OhlcvInfo(strArr[1]
            										, BigDecimal.ONE
            										, LocalDateTime.now()
        											, new BigDecimal(strArr[3])
        											, new BigDecimal(strArr[3])));
                	
                	/**
                	 * 거래주체별거래량 save
                	 * 0 = code
                	 * 1 = windowSize 		(default = 1min)
                	 * 2 = timestamp
                	 * 3 = foreighVolume 	(default = Random().nextInt(1000))
                	 * 4 = istttVolume 		(default = Random().nextInt(1000))
                	 * 5 = indiVolume 		(default = Random().nextInt(1000))
                	 * 6 = see 				(default = Random().nextInt(1000))
                	 */
                	
                	investAgentVolRepository.save(new InvestAgentVolInfo(strArr[1]
                														, BigDecimal.ONE
                														, LocalDateTime.now()
            															, new BigDecimal(Integer.toString(new Random().nextInt(1000)))
            															, new BigDecimal(Integer.toString(new Random().nextInt(1000)))
            															, new BigDecimal(Integer.toString(new Random().nextInt(1000)))
            															, new BigDecimal(Integer.toString(new Random().nextInt(1000)))));
                	
                	investAgentVolInfo = investAgentVolRepository.findByCode(strArr[1]);
                	//많이 본
                    redisTemplate.opsForZSet().add("viewALot", strArr[1]
                    										 , investAgentVolInfo.get().getSee().doubleValue());
                    //많이 오른, 많이 내린
                    redisTemplate.opsForZSet().add("volumeOfLot", strArr[1]
                    											, new BigDecimal(strArr[3]).subtract(new BigDecimal(strArr[3])).doubleValue());
                    //거래량 많은
                    redisTemplate.opsForZSet().add("volumeHigh", strArr[1]
                											   , investAgentVolInfo.get().getForeighVolume()
                											   .add(investAgentVolInfo.get().getIstttVolume())
                											   .add(investAgentVolInfo.get().getIndiVolume())
                											   .doubleValue());
                }
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
    
}
