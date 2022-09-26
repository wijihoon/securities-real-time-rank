# 카카오페이증권 사전과제

## 프로젝트 관리
* 작성자 : 위지훈
* 작성일자 : 2022-09-26
* 작성내용 : 
- 실시간 순위 정보를 고객에서 제공하는 서비스
- “많이 본”, “많이 오른”, “많이 내린”, “거래량 많은” 각 주제로 메인화면과 상세화면으로 구성
- 서비스의 상세한 설명은 카카오페이앱 주식 서비스의 발견 탭 의 [실시간 순위] 서비스를 참고

## 목차
- [개발 환경](#개발-환경)
- [빌드 및 실행하기](#빌드-및-실행하기)
- [기능 요구사항](#기능-요구사항)
- [필수 제약사항](#필수-제약사항)
- [해결방법](#해결방법)

## 개발 환경
- 기본환경
  - IDE : STS-4-4.16.0.RELEASE
  - GIT
- Sever
  - JAVA11
  - Spring Boot 2.5.3
  - JPA
  - H2
  - Redis
  - Maven
  - Junit
  
## 빌드 및 실행하기
### 터미널 환경
```
 - 프로젝트 zip 다운 후 압축해제
 - intellij > File > New > Project from Existing Sources... 선택
 - external model > gradle 선택
 - KakaopaysecApplication 실행
```

## 기능 요구사항
- 서비스 제공에 필요한 RESTful API 를 구현합니다.
- 실시간 데이터의 변동을 위해 데이터가 랜덤으로 변경되는 API를 추가로 개발해 주시기 바랍니다.
- Application이 로딩될 때 기본 데이터가 DB에 적재하도록 합니다. (주식 종목에 대한 정보는 첨부된 데이터를 참고하시면 됩니다.)
- 데이터 테이블 구조는 효율적인 방식으로 스스로 설계해 주시면 됩니다.
- 각 기능 및 제약사항에 대한 단위테스트를 작성합니다.
  
## API 설명
- 모든 주제의 상위 5건 조회 API
  - 주제별 상위 5건 데이터를 한번에 가져옵니다.
- 주제별 조회 API (주제는 4가지로 분류)
  - “많이 본”
  - “많이 오른”
  - “많이 내린”
  - “거래량 많은”
- 요청은 페이징(기본 20건) 처리하여 조회합니다.
- 최대 100개까지만 데이터를 제공합니다.
- 순위를 랜덤하게 변경할 수 있는 API 
  - 주제 전체의 순위가 랜덤하게 변경될 수 있도록 합니다.

## 필수 제약사항
- 설계 내용과 이유, 핵심 문제해결 전략 및 분석한 내용을 작성하여 “readme.md” 파일에 첨부 해주세요.
- 개발은 SpringBoot 와 JAVA를 사용해서 구현하시기 바랍니다.
- API의 HTTP Method들은 자유롭게 선택하시면 됩니다. (GET, POST, DELETE, PUT, PATCH)
- 에러응답, 에러코드는 자유롭게 정의해주세요.
- 단위 테스트를 작성하세요.

## 해결방법
### 1. 데이터 파일('.csv')을 데이터베이스에 저장하는 API 개발
- Request
```
http://localhost:8080/db/csv
```

- CSV 파일을 데이터베이스에 저장하는 API개발
  - data.sql을 이용하여 실행 시 sql를 실행하도록 구현했지만 요구사항과 맞지 않다고 판단하여 수정.
  - CsvController을 이용한 업로드보다 CSVReaderService을 추가 개발하여 /resources/data 경로의 csv파일을 데이터베이스에 저장하도록 수정

### 2. 2018년, 2019년 각 연도별 합계 금액이 가장 많은 고객을 추출하는 API 개발.(단, 취소여부가 ‘Y’ 거래는 취소된 거래임, 합계 금액은 거래금액에서 수수료를 차감한 금액임)
- Request
```
http://localhost:8080/year/maxSumAmt
```

- Response
```json
[
  {
    "year": "2018",
    "name": "테드",
    "accNo": "11111114",
    "sumAmt": 28992000
  },
  {
    "year": "2019",
    "name": "에이스",
    "accNo": "11111112",
    "sumAmt": 40998400
  }
]
```  

- `JpaRepository`에서 `@Query` 어노테이션을 통해 연도별 고객금액과 연도별 합계 금액 결과를 가져와 두 리스트를 비교하여 최대 값을 보여주도록 개발

### 3. 2018년 또는 2019년에 거래가 없는 고객을 추출하는 API 개발.
(취소여부가 ‘Y’ 거래는 취소된 거래임)

- Request
```
http://localhost:8080/year/notTrx
```

- Response
```json
[
  {
    "year": "2018",
    "name": "사라",
    "acctNo": "11111115"
  },
  {
    "year": "2018",
    "name": "에이스",
    "acctNo": "11111121"
  },
  {
    "year": "2019",
    "name": "테드",
    "acctNo": "11111114"
  },
  {
    "year": "2019",
    "name": "에이스",
    "acctNo": "11111121"
  }
]
```

- `JpaRepository`에서 `@Query` 어노테이션을 이용하여 계좌정보, 거래내역을 LEFT JOIN 하여 거래금액이 null인 고객추출
- 취소거래도 거래라고 볼 수 있어 거래가 있는 고객으로 보는게 맞다고 생각하여 포함

### 4. 연도별 관리점별 거래금액 합계를 구하고 합계금액이 큰 순서로 출력하는 API 개발.( 취소여부가 ‘Y’ 거래는 취소된 거래임)

- Request
```
http://localhost:8080/year/branSumAmt
```

- Response
```json
[
  {
  "year": "2018",
  "dataList":[
    {
    "brNm": "분당점",
    "brNo": "B",
    "trxAmt": "38484000"
    },
    {
    "brNm": "판교점",
    "brNo": "A",
    "trxAmt": "20505700"
    },
    {
    "brNm": "강남점",
    "brNo": "C",
    "trxAmt": "20232867"
    },
    {
    "brNm": "잠실점",
    "brNo": "D",
    "trxAmt": "14000000"
    }
  ]
},
{
  "year": "2019",
  "dataList":[
    {
    "brNm": "판교점",
    "brNo": "A",
    "trxAmt": "66795100"
    },
    {
    "brNm": "분당점",
    "brNo": "B",
    "trxAmt": "45396700"
    },
    {
    "brNm": "강남점",
    "brNo": "C",
    "trxAmt": "19500000"
    },
    {
    "brNm": "잠실점",
    "brNo": "D",
    "trxAmt": "6000000"
    }
  ]
},
{
  "year": "2020",
  "dataList":[
    {
    "brNm": "을지로점",
    "brNo": "E",
    "trxAmt": "1000000"
    }
    ]
  }
]
```

- `JpaRepository`에서 `@Query` 어노테이션을 이용하여 연도별 지점거래내역 추출
- 자료출력 형태를 위해 연도를 키로 지점정보를 데이터로 Map<String, List> 저장 후 출력형태에 맞게 리스트맵에 넣어 출력

### 5. 분당점과 판교점을 통폐합하여 판교점으로 관리점 이관을 하였습니다. 지점명을 입력하면 해당지점의 거래금액 합계를 출력하는 API 개발( 취소여부가 ‘Y’ 거래는 취소된 거래임,)

- Request
```
http://localhost:8080/bran/transfer?brName=분당점
```
- Response
```json

{
  "code": "404",
  "메세지": "br code not found error"
}
```
- Request
```
http://localhost:8080/bran/transfer?brName=판교점
```
- Response
```json
{
  "brName": "판교점",
  "brCode": "A",
  "sumAmt": 174601600
}
```

- `JpaRepository`에서 `@Query` 어노테이션을 이용하여 지점별 합계금액 추출
- 테이블로 관리를 하지 않아, 신규 리스트에 이관 지점 정보 저장(이관지점이 없을 경우 현재 지점명과 동일하게 세팅)
- 지점별 합계금액 조회 시 이관 지점 리스트를 조회하여 리스트 갯수가 0일 경우 404, br code not found error로 Exception 발생, 1이 초과할 경우 반복문을 통한 합계금액 +
