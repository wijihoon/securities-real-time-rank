## kakaopay securities real-time rank
---
## 🏃‍♂️ 목차
- [개발 환경](#개발-환경)
- [ER 다이어그램](#ER-다이어그램)
- [API 명세](#API-명세)
- [필수 제약사항](#필수-제약사항)
- [해결방법](#해결방법)

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

## 🏃‍♂️ 개발 프레임워크

- 개발 프레임워크 구성은 아래와 같으며, Maven 스크립트에 의해서 자동으로 로드 및 사용하도록 되어있음

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

## 🏃‍♂️테이블 구성

<img width="924" alt="kakaopay_server_ERD" src="https://user-images.githubusercontent.com/76634761/192251282-60522fe2-dc9a-4757-89f4-74f079692ec4.jpg">

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

## 🏃‍♂️ API 명세

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
| `000` | 예상치 못한 오류가 발생하였습니다.         |	500 |
| `101`    | 서버 내부에서 처리 중에 에러가 발생했습니다.           |	400 |
| `102`    | 파라미터 확인부탁드립니다. |400|
| `103`    | 서비스 점검 중입니다. 공지사항을 확인해주세요.           |400|
| `104`    | 헤더 정보 확인부탁드립니다.           |400|

#### Response

```json
HTTP/1.1 400 Bad Request  
{
  "code": 101,
  "msg":"서버 내부에서 처리 중에 에러가 발생했습니다."
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
    "dropALot": [
        {
            "code": "010140",
            "codeName": "삼성중공업",
            "rank": 1.0,
            "price": 5063.00,
            "percent": -15.4800
        },
        {
            "code": "003410",
            "codeName": "쌍용C&E",
            "rank": 2.0,
            "price": 6218.00,
            "percent": -11.5600
        },
        {
            "code": "006800",
            "codeName": "미래에셋증권",
            "rank": 3.0,
            "price": 6169.00,
            "percent": -8.3400
        },
        {
            "code": "024110",
            "codeName": "기업은행",
            "rank": 4.0,
            "price": 9006.00,
            "percent": -7.0600
        },
        {
            "code": "034220",
            "codeName": "LG디스플레이",
            "rank": 5.0,
            "price": 15524.00,
            "percent": -4.4700
        }
    ],
    "riseALot": [
        {
            "code": "008560",
            "codeName": "메리츠증권",
            "rank": 1.0,
            "price": 5614.00,
            "percent": 13.8700
        },
        {
            "code": "028670",
            "codeName": "팬오션",
            "rank": 2.0,
            "price": 6179.00,
            "percent": 11.9300
        },
        {
            "code": "018880",
            "codeName": "한온시스템",
            "rank": 3.0,
            "price": 11192.00,
            "percent": 5.5800
        },
        {
            "code": "316140",
            "codeName": "우리금융지주",
            "rank": 4.0,
            "price": 12723.00,
            "percent": 4.2800
        },
        {
            "code": "015760",
            "codeName": "한국전력",
            "rank": 5.0,
            "price": 22295.00,
            "percent": 3.4500
        }
    ],
    "viewALot": [
        {
            "code": "007070",
            "codeName": "GS리테일",
            "rank": 1.0,
            "price": 24854.00,
            "percent": -2.9200
        },
        {
            "code": "259960",
            "codeName": "크래프톤",
            "rank": 2.0,
            "price": 257137.00,
            "percent": -0.3400
        },
        {
            "code": "017670",
            "codeName": "SK텔레콤",
            "rank": 3.0,
            "price": 51944.00,
            "percent": 1.4500
        },
        {
            "code": "096770",
            "codeName": "SK이노베이션",
            "rank": 4.0,
            "price": 209936.00,
            "percent": -0.2700
        },
        {
            "code": "047810",
            "codeName": "한국항공우주",
            "rank": 5.0,
            "price": 56536.00,
            "percent": 0.7700
        }
    ],
    "volumeHigh": [
        {
            "code": "055550",
            "codeName": "신한지주",
            "rank": 1.0,
            "price": 36084.00,
            "percent": 1.6400
        },
        {
            "code": "066570",
            "codeName": "LG전자",
            "rank": 2.0,
            "price": 102508.00,
            "percent": 0.4900
        },
        {
            "code": "047810",
            "codeName": "한국항공우주",
            "rank": 3.0,
            "price": 56536.00,
            "percent": 0.7700
        },
        {
            "code": "105560",
            "codeName": "KB금융",
            "rank": 4.0,
            "price": 50214.00,
            "percent": -0.9600
        },
        {
            "code": "139480",
            "codeName": "이마트",
            "rank": 5.0,
            "price": 105463.00,
            "percent": 0.4400
        }
    ]
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
    "riseALot": [
        {
            "code": "008560",
            "codeName": "메리츠증권",
            "rank": 1.0,
            "price": 5614.00,
            "percent": 13.8700
        },
        {
            "code": "028670",
            "codeName": "팬오션",
            "rank": 2.0,
            "price": 6179.00,
            "percent": 11.9300
        },
        {
            "code": "018880",
            "codeName": "한온시스템",
            "rank": 3.0,
            "price": 11192.00,
            "percent": 5.5800
        },
        {
            "code": "316140",
            "codeName": "우리금융지주",
            "rank": 4.0,
            "price": 12723.00,
            "percent": 4.2800
        },
        {
            "code": "015760",
            "codeName": "한국전력",
            "rank": 5.0,
            "price": 22295.00,
            "percent": 3.4500
        },
        {
            "code": "026960",
            "codeName": "동서",
            "rank": 6.0,
            "price": 26514.00,
            "percent": 3.3600
        },
        {
            "code": "371460",
            "codeName": "TIGER 차이나전기차SOLACTIVE",
            "rank": 7.0,
            "price": 17138.00,
            "percent": 3.2400
        },
        {
            "code": "138040",
            "codeName": "메리츠금융지주",
            "rank": 8.0,
            "price": 30696.00,
            "percent": 2.1400
        },
        {
            "code": "032640",
            "codeName": "LG유플러스",
            "rank": 9.0,
            "price": 12569.00,
            "percent": 1.7700
        },
        {
            "code": "006360",
            "codeName": "GS건설",
            "rank": 10.0,
            "price": 32058.00,
            "percent": 1.7700
        },
        {
            "code": "055550",
            "codeName": "신한지주",
            "rank": 11.0,
            "price": 36084.00,
            "percent": 1.6400
        },
        {
            "code": "005830",
            "codeName": "DB손해보험",
            "rank": 12.0,
            "price": 63345.00,
            "percent": 1.5100
        },
        {
            "code": "004990",
            "codeName": "롯데지주",
            "rank": 13.0,
            "price": 39468.00,
            "percent": 1.4600
        },
        {
            "code": "017670",
            "codeName": "SK텔레콤",
            "rank": 14.0,
            "price": 51944.00,
            "percent": 1.4500
        },
        {
            "code": "036460",
            "codeName": "한국가스공사",
            "rank": 15.0,
            "price": 42288.00,
            "percent": 1.4100
        },
        {
            "code": "086790",
            "codeName": "하나금융지주",
            "rank": 16.0,
            "price": 39037.00,
            "percent": 1.3900
        },
        {
            "code": "030200",
            "codeName": "KT",
            "rank": 17.0,
            "price": 38521.00,
            "percent": 1.3700
        },
        {
            "code": "000060",
            "codeName": "메리츠화재",
            "rank": 18.0,
            "price": 38649.00,
            "percent": 1.3000
        },
        {
            "code": "008770",
            "codeName": "호텔신라",
            "rank": 19.0,
            "price": 73136.00,
            "percent": 1.2900
        },
        {
            "code": "047050",
            "codeName": "포스코인터내셔널",
            "rank": 20.0,
            "price": 25445.00,
            "percent": 1.1700
        }
    ]
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
    
    "code": 201,
    "message": "success"
}
```

## 🏃‍♂️ 핵심 문제 해결 전략
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

### 주제별 랭킹 조회 API
- 요구사항에는 없는 내용이지만 현재 상용 뿌리기 서비스에는 있는 뿌릴 인원수 최대값을 채팅방 사용자 수 - 1(본인) 보다 크지 않도록 구현. 불필요하게 뿌릴 인원수를 많이 설정하여 요청자 지갑으로 환불하는 로직을 호출해야하기 때문에 서비스 과부하 가능성 있기 때문(프론트에서 막아도 됨)
- 뿌리기 토큰을 랜덤 3자리로 생성할 때 random() 대신randomAlphanumeric() 으로 생성하여 인코딩 오류 방지, 또한 DB 데이터와 중복 체크 필수, 또한 다른 곳에서도 사용할 확장성 고려하여 공통 함수로 분리
- 중복될 경우나 내부 오류시 지정해놓은 횟수만큼 반복 생성하는데, 현재는 3회로 지정하였지만 추후 뿌릴 인원(count)에 맞춰서 유동적으로 가져가는 것도 고민중.

### 순위 랜덤 변경 API
- 돈이 오고가는 거래이므로 로직 중에 오류나면 생성한 레코드 롤백하는 @Transaction 어노테이션 사용
- 뿌린 금액이 DB에 오름차순으로 저장되어 있기 때문에 뿌린 금액 받기 기능 호출시 한번 더 랜덤 정렬 시행하여 '랜덤'기능 충실

### 데이터 모델링 관련
- KP_TB_RAIN_MONEY의 FK를 KP_TB_ROOM_USER의 복합키로 할지, 각각의 PK로 할지 고민하다가 복합키로 결정하였으나 조회문이 복잡해지는 단점 생.
- TOKEN 필드의 데이터크기를 3자리로 잡았는데, 추후 확장성을 고려해 더 넉넉하게 잡았어도 좋았겠다는 생각. 서비스가 확장되면 인원이 늘어나고 3자리수로 유니크한 문자가 한계가 있기 때문
- PK Java에서는 Long인데 데이터 타입 Unsigned int로 할지, bigint로 할지 고민했다가 Java에서는 기본적으로 unsigned type을 지원하지 않아서 방법은 있겠으나 시간을 뺏길 것 같아서 bigint 사용

