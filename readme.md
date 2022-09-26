## KAKAOPAY RAIN MONEY API

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
<img width="924" alt="kakaopay_server_ERD" src="https://user-images.githubusercontent.com/34532192/97914961-06b7b480-1d94-11eb-84df-ac7ddc6ea094.png">

---

## 🏃‍♂️ API 명세

### 요청 공통

`헤더`

| 항목         | 값 (예)          | 설명           | 필수 여부|
| ------------ | ---------------- | --------------- |--
| Content-Type | application/json | `JSON` 으로 요청 |

### 응답 공통

`HTTP 응답코드`

| 응답코드 | 설명                  |
| -------- | --------------------- |
| `200` | **정상 응답**         |
| `201` | **정상적으로 생성**         |
| `400`    | 잘못된 요청           |
| `404`    | 리소스를 찾을 수 없음 |
| `500`    | 시스템 에러           |

`에러코드 및 메시지`


| 에러코드 | 메시지                  |
| -------- | --------------------- |
| `E0000` | 예상치 못한 오류가 발생하였습니다.         |
| `E0101`    | 서버 내부에서 처리 중에 에러가 발생했습니다.           |
| `E0102`    | 파라미터 확인부탁드립니다. |
| `E0103`    | 서비스 점검 중입니다. 공지사항을 확인해주세요.           |
| `E0104`    | 헤더 정보 확인부탁드립니다.           |

`헤더`

| 항목         | 값               | 설명             |
| ------------ | ---------------- | ---------------- |
| Content-Type | application/json | `JSON` 으로 응답 |

내용

| 이름    |  타입  | 필수 | 설명             |
| ------- | :----: | :---: | ---------------- |
| code    | string |  ○   | 응답 코드     |
| message | string |  ○   | API 별 응답 내용     |
응답 예

```json
{

"cause":  null,
"stackTrace":  [],
"code":  "101",
"message":  "서버 내부에서 처리 중에 에러가 발생했습니다.",
"suppressed":  [],
"localizedMessage":  "서버 내부에서 처리 중에 에러가 발생했습니다."

}
```

###  주제별 랭킹 조회 API
---

#### 요청

| 항목 | 값             |
| ---- | -------------- |
| URL  | `GET` /api/rank/{id} |

`항목`

| 이름       |  타입  | 필수 | 설명                                                         |
| ---------- | :----: | :---: | ------------------------------------------------------------ |
| id     | int |  x   |  default = 모든 주제 상위 5건, 0 = 많이 본, 1 = 많이 오른, 2 = 많이 내린, 3 = 많이 보유한  |
| paging      | int  |  x   | default = 20, max = 100                                          |

요청 예)

```json
{
  "paging": 100
}
```

#### 응답

`응답 내용`

| 이름 |  타입  | 필수 | 설명        |
| ---- | :----: | :---: | ----------- |
| viewALotList.code | String | ○ | 코드 |
| viewALotList.codeName | String | ○ | 코드명 |
| viewALotList.rank | Double | ○ | 순위 |
| viewALotList.price | BigDecimal | ○ | 금액 |
| viewALotList.percent | BigDecimal | ○ | 백분율 |
| riseALotList.code | String | ○ | 코드 |
| riseALotList.codeName | String | ○ | 코드명 |
| riseALotList.rank | Double| ○ | 순위 |
| riseALotList.price | BigDecimal | ○ | 금액 |
| riseALotList.percent | BigDecimal | ○ | 백분율 |
| dropALotList.code | String | ○ | 코드 |
| dropALotList.codeName | String | ○ | 코드명 |
| dropALotList.rank | Double | ○ | 순위 |
| dropALotList.price | BigDecimal | ○ | 금액 |
| dropALotList.percent | BigDecimal | ○ | 백분율 |
| volumeHighList.code | String  | ○ | 코드 |
| volumeHighList.codeName | String  | ○ | 코드명 |
| volumeHighList.rank | Double  | ○ | 순위 |
| volumeHighList.price | BigDecimal | ○ | 금액 |
| volumeHighList.percent | BigDecimal  | ○ | 백분율 |

응답 예시

```json
{
"viewALotList":  [
	{
		"code": 005930,
		"codeName":  삼성전자,
		"rank":  1,
		"price":  61500,
		"percent":  0.00
	},
	{
		"code": 373220,
		"codeName":  LG에너지솔루션,
		"rank":  2,
		"price":  452000,
		"percent":  0.00
	}
]
}
```

### 순위 랜덤 변경 API
---

#### 요청

| 항목 | 값             | 설명 |
| ---- | -------------- | --- |
| URL  | `POST` /api/randomRank|



--

#### 응답

`응답 내용`

| 이름 |  타입  | 필수 | 설명        |
| ---- | :----: | :---: | ----------- |
| code   | String |  ○   | 결과코드 |
| message | String |  ○   | 결과내용 |

`응답 예시`

```
{
"code" : 000,
"message" : success
}
```
핵심 문제 해결 전략
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

### 총평
원래 자바가 비효율적인 것 같아서 조금 싫어했는데, 이번 프로젝트를 계기로 자바도 효율적으로 쓸 수 있는 것들이 엄청 많구나라고 알게됨. boot, JPA/Hibernate, loombok, gradle, IntelliJ 등 을 통해서 이렇게 느꼈는데, 특히 JPA는 너무 좋았지만 파이썬의 ORM과 비교하면 그래도 해줘야할 게 많다. 이전에 썼던 Mybatis가 금융, SI기업에서 많이 쓰는 이유가 안정적이고 원래 쓰던거여서 라고 하던데, 안정화되면 빨리 도입했으면 좋겠다. 이번에 IntelliJ + gradle 조합을 쓰면서 너무 fancy 하다고 생각.
언어에 대한 인식을 바꾸게 해준 너무 좋은 경험.

