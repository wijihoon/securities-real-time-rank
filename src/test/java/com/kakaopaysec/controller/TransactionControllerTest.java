/**
 * 
 */
package com.kakaopaysec.controller;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import com.kakaopaysec.exception.ExceptionMessage;
import com.kakaopaysec.service.RankRedisSevice;
import com.kakaopaysec.service.TransactionService;

/**
 * @author wijihoon
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest extends ControllerTestTemplate {

	@Test
	@DisplayName("클라이언트로부터 POST 요청을 통해 CSV 파일을 정상적으로 업로드한다.")
	void upload_csv_file() {
	    requestCsvFileUpload(CSV_FILE_NAME)
	            .expectStatus()
	            .isOk()
	            .expectHeader()
	            .contentType(MediaType.APPLICATION_JSON)
	            .expectBody()
	            .jsonPath("$.fileName").isEqualTo("사전과제3.csv")
		        .jsonPath("$.contentType").isEqualTo("text/csv")
		        .jsonPath("$.size").isEqualTo(8887)
	    ;
	}
	
    @BeforeEach
    void setUp() {
        requestCsvFileUpload(CSV_FILE_NAME);
    }

    @Test
    @DisplayName("기관 조회 요청을 통해 전체 기관의 이름과 코드를 조회한다.")
    void read_institutions() {
        webTestClient.get().uri("/institutions")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$..name").value(hasItem("국민은행"))
                .jsonPath("$..code").value(hasItem("bank01"))
                ;
    }
    
    @Test
    @DisplayName("각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 조회한다.")
    void show_institution_and_year_of_max_fund() {
        webTestClient.get().uri("/funds/years/maximum")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.year").isEqualTo(2014)
                .jsonPath("$.bank").isEqualTo("주택도시기금")
        ;
    }

    @Test
    @DisplayName("전체 기간에서 입력한 은행 중 지원 금액 평균이 가장 클 때와 작을 때 연도와 지원 금액을 조회한다.")
    void show_year_and_amount_of_max_min_average_fund_by_bank() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/funds/years/average/maximum-minimum")
                        .queryParam("bank", "국민은행").build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.[0].year").isEqualTo(2006)
                .jsonPath("$.[0].amount").isEqualTo(484)
                .jsonPath("$.[1].year").isEqualTo(2016)
                .jsonPath("$.[1].amount").isEqualTo(5115)
        ;
    }

    @Test
    @DisplayName("2018년 특정 달과 은행을 입력했을 때 예측값을 조회한다.")
    void show_predict_fund() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/funds/predict")
                        .queryParam("bank", "국민은행")
                        .queryParam("month", 2)
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.bank").isEqualTo("bank01")
                .jsonPath("$.year").isEqualTo(2018)
                .jsonPath("$.month").isEqualTo(2)
                .jsonPath("$.amount").isEqualTo(4817)
        ;
    }

    @Test
    @DisplayName("찾을 수 없는 기관을 쿼리 스트링에 입력한 경우 응답에 에러 메시지가 담긴다.")
    void show_error_does_not_found_bank() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/funds/predict")
                        .queryParam("bank", "카카오페이")
                        .queryParam("month", 2)
                        .build())
                .exchange()
                .expectStatus()
                .is5xxServerError()
                .expectBody()
                .jsonPath("$.message").isEqualTo("찾을 수 없는 기관입니다.")
        ;
    }
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
//	@BeforeEach
//	void setUp() throws Exception {
//	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.kakaopaysec.controller.TransactionController#searchRankList(java.lang.Integer, java.lang.Integer)}.
	 */
	@Test
	final void testSearchRankList() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.kakaopaysec.controller.TransactionController#updateRandomRank()}.
	 */
	@Test
	final void testUpdateRandomRank() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.kakaopaysec.controller.TransactionController#TransactionController(ExceptionMessage, TransactionService, RankRedisSevice)}.
	 */
	@Test
	final void testTransactionController() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#Object()}.
	 */
	@Test
	final void testObject() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#getClass()}.
	 */
	@Test
	final void testGetClass() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#hashCode()}.
	 */
	@Test
	final void testHashCode() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#equals(java.lang.Object)}.
	 */
	@Test
	final void testEquals() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#clone()}.
	 */
	@Test
	final void testClone() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#toString()}.
	 */
	@Test
	final void testToString() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#notify()}.
	 */
	@Test
	final void testNotify() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#notifyAll()}.
	 */
	@Test
	final void testNotifyAll() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#wait()}.
	 */
	@Test
	final void testWait() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#wait(long)}.
	 */
	@Test
	final void testWaitLong() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#wait(long, int)}.
	 */
	@Test
	final void testWaitLongInt() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link java.lang.Object#finalize()}.
	 */
	@Test
	final void testFinalize() {
		fail("Not yet implemented"); // TODO
	}
	
//	@Autowired
//	MockMvc mockMvc;
//
//	@Autowired
//	protected ObjectMapper objectMapper;
//
//	@Autowired
//	RoomUserRepository roomUserRepo;
//	@Autowired
//	RainMoneyRepository rainMoneyRepo;
//	@Autowired
//	RainMoneyDetailRepository rainMoneyDetailRepo;
//	@Autowired
//	UserRepository userRepo;
//
//	public static final String ROOMID = "X-ROOM-ID";
//	public static final String USERID = "X-USER-ID";
//
//	public void setup() throws Exception {
//		mockMvc = MockMvcBuilders.standaloneSetup(RainMoneyController.class).build();
//	}
//
//	/**
//	 * 뿌리기 API 테스트(정상)
//	 */
//	@Test
//	void rainMoneyTest() throws Exception {
//
//		// given
//		RequestDTO requestrainMoney = new RequestDTO();
//		requestrainMoney.count = 3;
//		requestrainMoney.total_amount = 300;
//
//		// 결과값 JSON 형식으로 노출해줌
//		Gson gson = new Gson();
//		String resJson = gson.toJson(requestrainMoney);
//
//		// When
//		mockMvc.perform(
//				post("/kakaopay/rainMoney")
//						.contentType(MediaType.APPLICATION_JSON)
//						.accept(MediaType.APPLICATION_JSON)
//						.header(ROOMID, "AAA")
//						.header(USERID, "1").content(objectMapper.writeValueAsString(resJson))
//		).andDo(print());
//
//		// Then
//		// TODO:
//		// return 201 Created
//	}
//
//	/**
//	 * 뿌리기 API 테스트(비정상)
//	 * 뿌릴 인원이 채팅방 인원수보다 클 경우
//	 */
//	@Test
//	void rainMoneyErrTest() throws Exception {
//
//		// given
//		RequestDTO requestrainMoney = new RequestDTO();
//		requestrainMoney.count = 300;
//		requestrainMoney.total_amount = 500;
//
//		// 결과값 JSON 형식으로 노출해줌
//		Gson gson = new Gson();
//		String resJson = gson.toJson(requestrainMoney);
//
//		// When
//		mockMvc.perform(
//				post("/kakaopay/rainMoney")
//						.contentType(MediaType.APPLICATION_JSON)
//						.accept(MediaType.APPLICATION_JSON)
//						.header(ROOMID, "AAA")
//						.header(USERID, "1").content(objectMapper.writeValueAsString(resJson))
//		).andDo(print());
//
//		// Then
//		// TODO:
//		// return E0108 Error
//
//	}
//
//	/**
//	 * 뿌린 금액 받기 API 테스트(정상)
//	 */
//	@Test
//	void receiveRainMoneyTest() throws Exception {
//		// given
//		ReceiveRequestDTO receiverainMoney = new ReceiveRequestDTO();
//		receiverainMoney.token = rainMoneyRepo.findAll().get(0).getToken();
//
//		// 결과값 JSON 형식으로 노출해줌
//		Gson gson = new Gson();
//		String resJson = gson.toJson(receiverainMoney);
//
//		// When
//		mockMvc.perform(
//				post("/kakaopay/receive")
//						.contentType(MediaType.APPLICATION_JSON)
//						.accept(MediaType.APPLICATION_JSON)
//						.header(ROOMID, "AAA")
//						.header(USERID, "2").content(objectMapper.writeValueAsString(resJson))
//		).andDo(print());
//
//		// Then
//		// TODO:
//		// return 201 Created
//	}
//	/**
//	 * 뿌린 금액 받기 API 테스트(비정상)
//	 * 1. 10분 유효 시간 초과
//	 * 2. 본인이 뿌린 금액 받기 요청
//	 * 3. 채팅방 권한 없는 사용자
//	 * 4. 중복 받기 요청
//	 */
//	@Test
//	void receiveRainMoneyErrTest() throws Exception {
//		// given
//		ReceiveRequestDTO receiverainMoney = new ReceiveRequestDTO();
//		receiverainMoney.token = rainMoneyRepo.findAll().get(0).getToken();
//
//		// 결과값 JSON 형식으로 노출해줌
//		Gson gson = new Gson();
//		String resJson = gson.toJson(receiverainMoney);
//
//		// When
//		mockMvc.perform(
//				post("/kakaopay/receive")
//						.contentType(MediaType.APPLICATION_JSON)
//						.accept(MediaType.APPLICATION_JSON)
//						.header(ROOMID, "AAA")
//						.header(USERID, "2").content(objectMapper.writeValueAsString(resJson))
//		).andDo(print());
//
//		// Then
//		// TODO:
//		// return E0107 Error
//		// return E0104 Error
//		// return E0302 Error
//		// return E0101 Error
//	}
//
//	/*
//	 * 뿌리기 정보 조회 API 테스트(정상)
//	 */
//	@Test
//	void getRainMonerainMoneyDetailTest() throws Exception {
//		// given
//		ReceiveRequestDTO receiverainMoney = new ReceiveRequestDTO();
//		receiverainMoney.token = rainMoneyRepo.findAll().get(0).getToken();
//
//		// 결과값 JSON 형식으로 노출해줌
//		Gson gson = new Gson();
//		String resJson = gson.toJson(receiverainMoney);
//
//		// When
//		mockMvc.perform(
//				get("/kakaopay/rainMoney")
//						.contentType(MediaType.APPLICATION_JSON)
//						.accept(MediaType.APPLICATION_JSON)
//						.header(ROOMID, "AAA")
//						.header(USERID, "1").content(objectMapper.writeValueAsString(resJson))
//		).andDo(print());
//	}
//	/*
//	 * 뿌리기 정보 조회 API 테스트(비정상)
//	 * 1. 발급자 이외의 사용자 조회 요청
//	 * 2. 7일 조희 기간 종료
//	 */
//	@Test
//	void getRainMonerainMoneyDetailErrTest() throws Exception {
//		// given
//		ReceiveRequestDTO receiverainMoney = new ReceiveRequestDTO();
//		receiverainMoney.token = rainMoneyRepo.findAll().get(0).getToken();
//
//		// 결과값 JSON 형식으로 노출해줌
//		Gson gson = new Gson();
//		String resJson = gson.toJson(receiverainMoney);
//
//		// When
//		mockMvc.perform(
//				get("/kakaopay/rainMoney")
//						.contentType(MediaType.APPLICATION_JSON)
//						.accept(MediaType.APPLICATION_JSON)
//						.header(ROOMID, "AAA")
//						.header(USERID, "4").content(objectMapper.writeValueAsString(resJson))
//		).andDo(print());
//
//		// Then
//		// TODO:
//		// return E0301 Error
//		// return E0102 Error
//	}
//}
///** 뿌리기 요청에 사용되는 Body */
//@DataJdbcTest
//class RequestDTO {
//	int total_amount;
//	int count;
//}
//@DataJdbcTest
//class ReceiveRequestDTO{
//	String token;
//}

}
