# 카카오페이증권 사전과제

## 프로젝트 관리
* 작성자 : 이민재
* 작성일자 : 2020.02.28
* 작성내용 : 카카오페이증권 사전과제 - 특정 고객 거래내역 조회 서비스 개발

## 목차
- [개발 환경](#개발-환경)
- [빌드 및 실행하기](#빌드-및-실행하기)
- [기능 요구사항](#기능-요구사항)
- [필수 제약사항](#필수-제약사항)
- [해결방법](#해결방법)

## 개발 환경
- 기본환경
  - IDE : Intellij IDEA Ultimate
  - GIT
- Sever
  - JAVA8
  - Spring Boot 2.2.4
  - JPA
  - H2
  - Gradle
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
- 2018년, 2019년 각 연도별 합계 금액이 가장 많은 고객을 추출하는 API 개발.(단, 취소여부가 ‘Y’ 거래는 취소된 거래임, 합계 금액은 거래금액에서 수수료를 차감한 금액임)
- 2018년 또는 2019년에 거래가 없는 고객을 추출하는 API 개발.
(취소여부가 ‘Y’ 거래는 취소된 거래임)
- 연도별 관리점별 거래금액 합계를 구하고 합계금액이 큰 순서로 출력하는 API 개발.( 취소여부가 ‘Y’ 거래는 취소된 거래임)
- 분당점과 판교점을 통폐합하여 판교점으로 관리점 이관을 하였습니다. 지점명을 입력하면 해당지점의 거래금액 합계를 출력하는 API 개발( 취소여부가 ‘Y’ 거래는 취소된 거래임,)

  
## 필수 제약사항
* API 기능명세에서 기술된 API를 모두 개발하세요.
* Spring boot 기반의 프레임웍을 사용하세요.
* 단위 테스트(Unit Test) 코드를 개발하여 각 기능을 검증하세요. (필수사항)
* 모든 입/출력은 JSON 형태로 주고 받습니다.
* 단, 각 API에 HTTP Method들(GET|POST|PUT|DEL)은 자유롭게 선택하세요.
* README.md 파일을 추가하여, 개발 프레임웍크, 문제해결 방법, 빌드 및 실행 방법을 기술하세요.

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
