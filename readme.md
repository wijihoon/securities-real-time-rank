## securities real-time rank
---
## 목차
- [개발 환경](#개발-환경)
- [ER 다이어그램](#ER-다이어그램)
- [API 명세](#API-명세)
- [핵심 문제 해결 전략](#핵심-문제-해결-전략)

---

## 개발 환경
* JDK 11
* Spring Boot 2.5.3
* STS-4-4.16.0.RELEASE
* JPA
* H2
* Redis
* Maven
* Junit

---

## 개발 프레임워크
개발 프레임워크 구성은 아래와 같으며, Maven 스크립트에 의해서 자동으로 로드 및 사용하도록 되어있음

### 1. Database
- h2database : 2.1.214
- redis      : 2.7.4

### 2. Spring Boot & Spring
Restful 서버 구성 및 DB 엔티티 관리에 사용
spring-integration-jdbc, spring-boot-integration은 분산 락을 활용하여, 특정 카드 또는 결제 ID의 동시 접근을 막는데 사용

- spring-boot-starter-test : 2.7.4.RELEASE
- spring-boot-data-jpa: 2.7.4.RELEASE

### 3. Junit
함수 및 메소드의 모듈이 의도한 대로 작동하는지 검증하기 위해 사용자

- junit-jupiter-engine : 5.8.2 
- assertj-core : 3.22.0
- mockito-junit-jupiter : 4.5.1

### 4. 그 외 

* Lombok : 1.18.24<br/>
개발 편의를 위해 사용, Annotation Processor를 활용한 자동 Getter, Setter 기능 및 Builder 생성 시에 사용.<br/>
IDE 에서 사용해야 하는 경우에 Project setting 내에 Enable annotation processing 옵션을 켜고 사용 해야 함.

- opencsv : 3.7 <br/>
Sample Csv 파일을 "csvReader"를 통해 읽어 효율적인 데이터 핸들링을 위함

---

## ♂️테이블 구성

<img width="924" alt="kakaopay_server_ERD" src="https://user-images.githubusercontent.com/76634761/192529288-bfbc7a78-fb67-466c-b3b3-f777b5171457.png">

### 1. item
- Sample Data내 존재하는 주식 상품 정보 저장
- 원장성 테이블로 주식 랭킹 프로세스에 주체가 되는 테이블 개발내 invest_agent_vol, ohlcv의 자식 테이블을 가지게 된다.
- 추후 이력 테이블 필요

| 컬럼ID | 컬럼명 | type | len | PK |
| -------- | ----|----|---|---------- |
| code |주식 코드| char | 32 |1|
| code_name |주식 코드명| char  | 128| |

### 2. invest_agent_vol
- 거래성 테이블로 등록만 가능한 테이블 저장한 데이터를 정정하거나 취소하는 등의 행위는 Insert를 통해 상태 관리 해야함. 따라서 별도의 이력 테이블은 존재하지 않는다.
- 파티션 테이블로 거래 일시 기준으로 파티션을 나눠 관리

| 컬럼ID | 컬럼명 | type | len | PK |
| -----|--- | --------|---|---------- |
| timestamp |거래 일시| datatime |  |1|
| code |주식 코드| char  | 32| 2|
| window_size |주기| int  | | |
| foreigh_volume |외국인 보유량| int  | | |
| isttt_volume |기관 보유량| int  | | |
| indi_volume |개인 보유량| int  | | |
| see |본 횟수| int  | | |

### 3. ohlcv
- 거래성 테이블로 등록만 가능한 테이블 저장한 데이터를 정정하거나 취소하는 등의 행위는 Insert를 통해 상태 관리 해야함. 따라서 별도의 이력 테이블은 존재하지 않는다.
- 파티션 테이블로 거래 일시 기준으로 파티션을 나눠 관리

| 컬럼ID | 컬럼명 | type | len | PK |
| ------|-- | --------|---|---------- |
| timestamp |거래 일시| datatime |  |1|
| code |주식 코드| char  | 32| 2|
| window_size |주기| int  | | |
| open |오픈가격| int  | | |
| close |종료가격| int  | | |

---

## API 명세

### HTTP 상태 코드

| 상태코드 | 싱태 | 설명 |
| -------- | -----------|---------- |
| `200` |성공| **정상 응답**         |
| `201` |성공| **정상적으로 생성**         |
| `400`  | 실패 | 잘못된 요청           |
| `404`   | 실패 | 리소스를 찾을 수 없음 |
| `500`   | 실패 | 시스템 에러           |

### 에러 코드

| code | 설명  | HTTP상태코드|
| -------- | -----------|---------- |
| `-1` | 처리 중에 에러가 발생했습니다.         |	400 |
| `-770`    | 순위 조회 시 주제를 선택해주세요.           |	400 |
| `-771`    | 순위는 최대 100건만 조회가 가능합니다. |400|
| `-772`    | 순위 랜덤 변경 중 오류가 발생했습니다.           |400|

#### Response

```json
HTTP/1.1 400 Bad Request  
{
    "response": null,
    "suceess": false,
    "error": {
        "code": -770,
        "message": "순위 조회 시 주제를 선택해주세요."
    }
}
```

###  모든 주제 랭킹 조회 API
- 이 API는 모든 주제 Top5를 조회하는 API입니다.
- 요청이 성공하면 응답은 바디에 JSON 객체로  `많이 본`, `많이 오른`, `많이 내린`, `많이 보유한` 정보를 포함합니다.

---

#### Response

| Name |  Type  | Descrption |
| ---- | ---- | ----------- |
| viewALot | ViewALot[] | 많이 본 상세 |
| riseALot | RiseALot[] | 많이 오른 상세 |
| dropALot | DropALot[] | 많이 내린 상세 |
| volumeHigh | VolumeHigh[]  | 많이 보유한 상세 |

#### ViewALot(JSON), RiseALot(JSON), DropALot(JSON), VolumeHigh(JSON)

| Name |  Type  | Descrption |
| ---- | ---- | ----------- |
| code | String	 | 상품코드 |
| codeNm | String	 | 상품코드명|
| rank | BigDecimal | 순위 |
| price | BigDecimal  | 가격 |
| percent | Double  | 백분율 |

#### Sample
#### Request

```json
curl -v -X GET "http://localhost:8080/v1/stock/rank"
```

#### Response

```json
HTTP/1.1 200 OK
Content-type: application/json;charset=UTF-8
{
    "response": {
        "dropALot": [
            {
                "code": "024110",
                "codeName": "기업은행",
                "rank": 1.0,
                "price": 8929.00,
                "percent": -7.8600
            },
            {
                "code": "010140",
                "codeName": "삼성중공업",
                "rank": 2.0,
                "price": 5674.00,
                "percent": -5.2800
            },
            {
                "code": "272210",
                "codeName": "한화시스템",
                "rank": 3.0,
                "price": 14507.00,
                "percent": -5.1900
            },
            {
                "code": "030000",
                "codeName": "제일기획",
                "rank": 4.0,
                "price": 21517.00,
                "percent": -3.7300
            },
            {
                "code": "003410",
                "codeName": "쌍용C&E",
                "rank": 5.0,
                "price": 6771.00,
                "percent": -3.6900
            }
        ],
        "riseALot": [
            {
                "code": "008560",
                "codeName": "메리츠증권",
                "rank": 1.0,
                "price": 5309.00,
                "percent": 7.6800
            },
            {
                "code": "018880",
                "codeName": "한온시스템",
                "rank": 2.0,
                "price": 11390.00,
                "percent": 7.4500
            },
            {
                "code": "028670",
                "codeName": "팬오션",
                "rank": 3.0,
                "price": 5876.00,
                "percent": 6.4400
            },
            {
                "code": "015760",
                "codeName": "한국전력",
                "rank": 4.0,
                "price": 22485.00,
                "percent": 4.3300
            },
            {
                "code": "088980",
                "codeName": "맥쿼리인프라",
                "rank": 5.0,
                "price": 13331.00,
                "percent": 3.7400
            }
        ],
        "viewALot": [
            {
                "code": "010140",
                "codeName": "삼성중공업",
                "rank": 1.0,
                "price": 5674.00,
                "percent": -5.2800
            },
            {
                "code": "007070",
                "codeName": "GS리테일",
                "rank": 2.0,
                "price": 24730.00,
                "percent": -3.4000
            },
            {
                "code": "023530",
                "codeName": "롯데쇼핑",
                "rank": 3.0,
                "price": 101034.00,
                "percent": -0.4600
            },
            {
                "code": "086280",
                "codeName": "현대글로비스",
                "rank": 4.0,
                "price": 177546.00,
                "percent": -0.5400
            },
            {
                "code": "066570",
                "codeName": "LG전자",
                "rank": 5.0,
                "price": 102187.00,
                "percent": 0.1800
            }
        ],
        "volumeHigh": [
            {
                "code": "078930",
                "codeName": "GS",
                "rank": 1.0,
                "price": 43781.00,
                "percent": -0.9500
            },
            {
                "code": "377300",
                "codeName": "카카오페이",
                "rank": 2.0,
                "price": 68629.00,
                "percent": 0.3300
            },
            {
                "code": "036570",
                "codeName": "엔씨소프트",
                "rank": 3.0,
                "price": 381680.00,
                "percent": -0.2200
            },
            {
                "code": "003410",
                "codeName": "쌍용C&E",
                "rank": 4.0,
                "price": 6771.00,
                "percent": -3.6900
            },
            {
                "code": "034220",
                "codeName": "LG디스플레이",
                "rank": 5.0,
                "price": 15685.00,
                "percent": -3.4800
            }
        ]
    },
    "suceess": true,
    "error": null
}
```

###  주제별 랭킹 조회 API
- 이 API는 주제별 최대 Top100을 조회하는 API입니다.
- 요청이 성공하면 응답은 바디에 JSON 객체로 `많이 본`, `많이 오른`, `많이 내린`, `많이 보유한` 중 하나의 상세 정보를 포함합니다.
---

#### Request

| Name |  Type  | Descrption |
| ---- | ---- | ----------- |
| id | int | 0 = 많이 본, 1 = 많이 오른, 2 = 많이 내린, 3 = 많이 보유한 |
| paging | int | default = 20, max = 100 |

#### Response

| Name |  Type  | Descrption |
| ---- | ---- | ----------- |
| viewALot | ViewALot[] | 많이 본 상세 |
| riseALot | RiseALot[] | 많이 오른 상세 |
| dropALot | DropALot[] | 많이 내린 상세 |
| volumeHigh | VolumeHigh[]  | 많이 보유한 상세 |

#### ViewALot(JSON), RiseALot(JSON), DropALot(JSON), VolumeHigh(JSON)

| Name |  Type  | Descrption |
| ---- | ---- | ----------- |
| code | String	 | 상품코드 |
| codeNm | String	 | 상품코드명 |
| rank | BigDecimal | 순위 |
| price | BigDecimal  | 가격 |
| percent | Double  | 백분율 |

#### Sample
#### Request

```json
curl -v -X GET "http://localhost:8080/v1/stock/rank/{id}"
```

#### Response

```json
HTTP/1.1 200 OK
Content-type: application/json;charset=UTF-8
{
    "response": {
        "riseALot": [
            {
                "code": "008560",
                "codeName": "메리츠증권",
                "rank": 1.0,
                "price": 5309.00,
                "percent": 7.6800
            },
            {
                "code": "018880",
                "codeName": "한온시스템",
                "rank": 2.0,
                "price": 11390.00,
                "percent": 7.4500
            },
            {
                "code": "028670",
                "codeName": "팬오션",
                "rank": 3.0,
                "price": 5876.00,
                "percent": 6.4400
            },
            {
                "code": "015760",
                "codeName": "한국전력",
                "rank": 4.0,
                "price": 22485.00,
                "percent": 4.3300
            },
            {
                "code": "088980",
                "codeName": "맥쿼리인프라",
                "rank": 5.0,
                "price": 13331.00,
                "percent": 3.7400
            },
            {
                "code": "316140",
                "codeName": "우리금융지주",
                "rank": 6.0,
                "price": 12636.00,
                "percent": 3.5700
            },
            {
                "code": "000060",
                "codeName": "메리츠화재",
                "rank": 7.0,
                "price": 39066.00,
                "percent": 2.4000
            },
            {
                "code": "138040",
                "codeName": "메리츠금융지주",
                "rank": 8.0,
                "price": 30682.00,
                "percent": 2.1000
            },
            {
                "code": "371460",
                "codeName": "TIGER 차이나전기차SOLACTIVE",
                "rank": 9.0,
                "price": 16941.00,
                "percent": 2.0500
            },
            {
                "code": "006360",
                "codeName": "GS건설",
                "rank": 10.0,
                "price": 32128.00,
                "percent": 1.9900
            },
            {
                "code": "047050",
                "codeName": "포스코인터내셔널",
                "rank": 11.0,
                "price": 25628.00,
                "percent": 1.9000
            },
            {
                "code": "032640",
                "codeName": "LG유플러스",
                "rank": 12.0,
                "price": 12574.00,
                "percent": 1.8100
            },
            {
                "code": "000100",
                "codeName": "유한양행",
                "rank": 13.0,
                "price": 57727.00,
                "percent": 1.6300
            },
            {
                "code": "071050",
                "codeName": "한국금융지주",
                "rank": 14.0,
                "price": 62092.00,
                "percent": 1.4500
            },
            {
                "code": "021240",
                "codeName": "코웨이",
                "rank": 15.0,
                "price": 65946.00,
                "percent": 1.4500
            },
            {
                "code": "251270",
                "codeName": "넷마블",
                "rank": 16.0,
                "price": 68239.00,
                "percent": 1.3900
            },
            {
                "code": "026960",
                "codeName": "동서",
                "rank": 17.0,
                "price": 26001.00,
                "percent": 1.3600
            },
            {
                "code": "012450",
                "codeName": "한화에어로스페이스",
                "rank": 18.0,
                "price": 72186.00,
                "percent": 1.1000
            },
            {
                "code": "005830",
                "codeName": "DB손해보험",
                "rank": 19.0,
                "price": 63053.00,
                "percent": 1.0400
            },
            {
                "code": "052690",
                "codeName": "한전기술",
                "rank": 20.0,
                "price": 75661.00,
                "percent": 1.0100
            }
        ]
    },
    "suceess": true,
    "error": null
}
```

###  순위 랜덤 변경 API
- 이 API는 모든 주제를 랜덤하게 변경하는 API입니다.
- 요청이 성공하면 응답은 바디에 JSON 객체로 `많이 본`, `많이 오른`, `많이 내린`, `많이 보유한` 정보를 랜덤하게 변경합니다.

---

#### Response

| Name |  Type  | Descrption |
| ---- | ---- | ----------- |
| code | String | 코드 |
| message | String | 메시지 |

#### Sample
#### Request

```json
curl -v -X POST "http://localhost:8080/v1/stock/random"
```

#### Response

```json
HTTP/1.1 200 OK
Content-type: application/json;charset=UTF-8
{
    "response": null,
    "suceess": false,
    "error": {
        "code": 201,
        "message": "success"
    }
}
```
---

## 단위 테스트
단위 테스트는 "/kakaopaysec/src/test/java/com/kakaopaysec/controller" 폴더 아래에 있는 파일들을 실행 

<img width="924" alt="kakaopay_server_junit" src="https://user-images.githubusercontent.com/76634761/192525118-29f38dd1-5bc0-45f5-b06a-d667867e20f9.png">

---

## 핵심 문제 해결 전략
---

### Application Loading 시 데이터 적재
- `CommandLineRunner` 인터페이스를 `@Bean` 어노테이션을 활용하여 익명 클래스 선언
- Application이 정상적으로 구동되면 `H2 Database`내에 Sample Data를 적재하는 로직 구현

```java
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
                	
                }
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
```

### 실시간 주식 Ranking 서비스 구축
- 실시간으로 변경되는 데이터로 인해 발생하는 서능을 고려하여 인-메모리 `H2 Database`를 통해 가볍고, 관리가 편한 `RDBMS` 선택
- 중복값의 제어와  O(log(N)) Select, 실시간 데이터 추가 및 변경을 고려한 Redis Sorted Set 자료구조 사용

```java
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	private ZSetOperations<String, String> zSetOps;
	
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
```

### 대용량 데이터 처리를 위한 테이블 구조
- `invest_agent_vol`, `ohlcv` 거래성 테이블의 대용량 처리를 위해 Partition Range 생성

```mysql
create table `invest_agent_vol`
(timestamp datatime not null,
code char(32) not null,
window_size int,
foreigh_volume int,
isttt_volume int,
indi_volume int,
see int,
primary key (timestamp, code)
) engine=innodb default charset=utf8mb4
partition by range(date_format(DATETIME,'%Y%m%d%H')) (
partition p0 values less than(2022092709) engine=innodb,
partition p1 values less than(2022092710) engine=innodb,
partition p2 values less than(2022092711) engine=innodb,
partition p3 values less than(2022092712) engine=innodb,
partition p999 values less than maxvalue engine=innodb);
```

