## KAKAOPAY RAIN MONEY API

---

## 🏃‍♂️ 목차
- [개발 환경](#개발-환경)
- [ER 다이어그램](#ER-다이어그램)
- [API 명세](#API-명세)
- [필수 제약사항](#필수-제약사항)
- [해결방법](#해결방법)

---

## 🏃‍♂️ 개발 환경
* JDK 11
* Spring Boot 2.5.3
* STS-4-4.16.0.RELEASE
* JPA
* H2
* Redis
* Maven
* Junit

---

## 🏃‍♂️ ER 다이어그램
<img width="924" alt="kakaopay_server_ERD" src="https://user-images.githubusercontent.com/76634761/192251282-60522fe2-dc9a-4757-89f4-74f079692ec4.jpg">

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
- 이 API는 카카오페이증권 모든 주제 Top5를 조회하는 API입니다.
- 주문 조회 API를 사용해 개별 주문의 상세 정보를 조회합니다. 앱 어드민 키를 헤더에 담아 POST로 요청합니다. 아래 두 가지 예제 중 어느 방법을 사용해도 결과는 같습니다. 요청이 성공하면 응답은 바디에 JSON 객체로 주문 상세 정보를 포함합니다.

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
curl -v -X GET "http://localhost:8080/api/rank"
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
            "price": 5064.00,
            "procent": -15.4600
        },
        {
            "code": "006800",
            "codeName": "미래에셋증권",
            "rank": 2.0,
            "price": 6205.00,
            "procent": -7.8100
        },
        {
            "code": "003410",
            "codeName": "쌍용C&E",
            "rank": 3.0,
            "price": 6571.00,
            "procent": -6.5300
        },
        {
            "code": "034220",
            "codeName": "LG디스플레이",
            "rank": 4.0,
            "price": 15497.00,
            "procent": -4.6400
        },
        {
            "code": "272210",
            "codeName": "한화시스템",
            "rank": 5.0,
            "price": 14607.00,
            "procent": -4.5300
        }
    ],
    "riseALot": [
        {
            "code": "028670",
            "codeName": "팬오션",
            "rank": 1.0,
            "price": 6340.00,
            "procent": 14.8500
        },
        {
            "code": "005940",
            "codeName": "NH투자증권",
            "rank": 2.0,
            "price": 10965.00,
            "procent": 9.1000
        },
        {
            "code": "008560",
            "codeName": "메리츠증권",
            "rank": 3.0,
            "price": 5344.00,
            "procent": 8.3900
        },
        {
            "code": "018880",
            "codeName": "한온시스템",
            "rank": 4.0,
            "price": 11346.00,
            "procent": 7.0300
        },
        {
            "code": "371460",
            "codeName": "TIGER 차이나전기차SOLACTIVE",
            "rank": 5.0,
            "price": 17557.00,
            "procent": 5.7600
        }
    ],
    "viewALot": [
        {
            "code": "241560",
            "codeName": "두산밥캣",
            "rank": 1.0,
            "price": 32772.00,
            "procent": -1.2900
        },
        {
            "code": "011780",
            "codeName": "금호석유",
            "rank": 2.0,
            "price": 130615.00,
            "procent": -0.6800
        },
        {
            "code": "026960",
            "codeName": "동서",
            "rank": 3.0,
            "price": 26059.00,
            "procent": 1.5900
        },
        {
            "code": "112610",
            "codeName": "씨에스윈드",
            "rank": 4.0,
            "price": 65426.00,
            "procent": 0.8100
        },
        {
            "code": "088980",
            "codeName": "맥쿼리인프라",
            "rank": 5.0,
            "price": 13147.00,
            "procent": 2.3100
        }
    ],
    "volumeHigh": [
        {
            "code": "004020",
            "codeName": "현대제철",
            "rank": 1.0,
            "price": 32640.00,
            "procent": -1.8400
        },
        {
            "code": "402340",
            "codeName": "SK스퀘어",
            "rank": 2.0,
            "price": 43503.00,
            "procent": -0.2300
        },
        {
            "code": "361610",
            "codeName": "SK아이이테크놀로지",
            "rank": 3.0,
            "price": 94968.00,
            "procent": 0.2800
        },
        {
            "code": "026960",
            "codeName": "동서",
            "rank": 4.0,
            "price": 26059.00,
            "procent": 1.5900
        },
        {
            "code": "180640",
            "codeName": "한진칼",
            "rank": 5.0,
            "price": 61977.00,
            "procent": 0.2800
        }
    ]
}
```

###  주제별 랭킹 조회 API
- 이 API는 카카오페이증권 주제별 최대 Top100을 조회하는 API입니다.
- 주문 조회 API를 사용해 개별 주문의 상세 정보를 조회합니다. 앱 어드민 키를 헤더에 담아 POST로 요청합니다. 아래 두 가지 예제 중 어느 방법을 사용해도 결과는 같습니다. 요청이 성공하면 응답은 바디에 JSON 객체로 주문 상세 정보를 포함합니다.

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
curl -v -X GET "http://localhost:8080/api/rank/{id}"
```

#### Response

```json
HTTP/1.1 200 OK
Content-type: application/json;charset=UTF-8
{
    "riseALot": [
        {
            "code": "028670",
            "codeName": "팬오션",
            "rank": 1.0,
            "price": 6340.00,
            "procent": 14.8500
        },
        {
            "code": "005940",
            "codeName": "NH투자증권",
            "rank": 2.0,
            "price": 10965.00,
            "procent": 9.1000
        },
        {
            "code": "008560",
            "codeName": "메리츠증권",
            "rank": 3.0,
            "price": 5344.00,
            "procent": 8.3900
        },
        {
            "code": "018880",
            "codeName": "한온시스템",
            "rank": 4.0,
            "price": 11346.00,
            "procent": 7.0300
        },
        {
            "code": "371460",
            "codeName": "TIGER 차이나전기차SOLACTIVE",
            "rank": 5.0,
            "price": 17557.00,
            "procent": 5.7600
        },
        {
            "code": "047050",
            "codeName": "포스코인터내셔널",
            "rank": 6.0,
            "price": 26140.00,
            "procent": 3.9300
        },
        {
            "code": "006360",
            "codeName": "GS건설",
            "rank": 7.0,
            "price": 32422.00,
            "procent": 2.9200
        },
        {
            "code": "004990",
            "codeName": "롯데지주",
            "rank": 8.0,
            "price": 39898.00,
            "procent": 2.5600
        },
        {
            "code": "088980",
            "codeName": "맥쿼리인프라",
            "rank": 9.0,
            "price": 13147.00,
            "procent": 2.3100
        },
        {
            "code": "055550",
            "codeName": "신한지주",
            "rank": 10.0,
            "price": 36323.00,
            "procent": 2.3100
        },
        {
            "code": "036460",
            "codeName": "한국가스공사",
            "rank": 11.0,
            "price": 42623.00,
            "procent": 2.2100
        },
        {
            "code": "032640",
            "codeName": "LG유플러스",
            "rank": 12.0,
            "price": 12624.00,
            "procent": 2.2100
        },
        {
            "code": "000060",
            "codeName": "메리츠화재",
            "rank": 13.0,
            "price": 38980.00,
            "procent": 2.1700
        },
        {
            "code": "015760",
            "codeName": "한국전력",
            "rank": 14.0,
            "price": 22001.00,
            "procent": 2.0900
        },
        {
            "code": "086790",
            "codeName": "하나금융지주",
            "rank": 15.0,
            "price": 39301.00,
            "procent": 2.0800
        },
        {
            "code": "017670",
            "codeName": "SK텔레콤",
            "rank": 16.0,
            "price": 52192.00,
            "procent": 1.9300
        },
        {
            "code": "000100",
            "codeName": "유한양행",
            "rank": 17.0,
            "price": 57742.00,
            "procent": 1.6500
        },
        {
            "code": "026960",
            "codeName": "동서",
            "rank": 18.0,
            "price": 26059.00,
            "procent": 1.5900
        },
        {
            "code": "316140",
            "codeName": "우리금융지주",
            "rank": 19.0,
            "price": 12372.00,
            "procent": 1.4000
        },
        {
            "code": "071050",
            "codeName": "한국금융지주",
            "rank": 20.0,
            "price": 61977.00,
            "procent": 1.2600
        }
    ]
}
```

###  순위 랜덤 변경 API
- 이 API는 카카오페이증권 모든 주제를 랜덤하게 변경하는 API입니다.
- 주문 조회 API를 사용해 개별 주문의 상세 정보를 조회합니다. 앱 어드민 키를 헤더에 담아 POST로 요청합니다. 아래 두 가지 예제 중 어느 방법을 사용해도 결과는 같습니다. 요청이 성공하면 응답은 바디에 JSON 객체로 주문 상세 정보를 포함합니다.

---

#### Response

| Name |  Type  | Descrption |
| ---- | ---- | ----------- |
| code | String | 코드 |
| message | String | 메시지 |

#### Sample
#### Request

```json
curl -v -X POST "http://localhost:8080/api/randomRank"
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

