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
class TransactionControllerTest {

    @Autowired
    private WebApplicationContext ctx;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CSVReaderService csvReaderService;
    @Autowired
    AccountTransactionService accountTransactionService;
    
    @BeforeEach
    void setUp() {
    	this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
		                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
		                .alwaysDo(print())
		                .build();
    }

    @Test
    @DisplayName("모든 주제 순위 랜덤 변경 API 테스트(정상)")
    void update_institutions() {
    	mockMvc.perform(post("/v1/random"))
		        .andDo(print())
		        .andExpect(status().isOk())
		        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andDo(print())
		        .andReturn();
    }
    
    @Test
    @DisplayName("모든 주제 순위를 랜덤하게 변경한다.(실패)")
    void addAccountsfail() throws Exception {
    	
    	mockMvc.perform(post("/v1/random"))
		        .andDo(print()).andExpect(status().is5xxServerError())
		        .andExpect(handler().handlerType(AccountController.class))
		        .andExpect(handler().methodName("add"))
		        .andExpect(jsonPath("$.success", is(false)))
		        .andExpect(jsonPath("$.error").exists());
    }
    
    @Test
    @DisplayName("모든 주제 Top5 조회 API 테스트(정상)")
    void search_all_top5_topics() {
    	mockMvc.perform(get("/v1/rank"))
		        .andDo(print())
		        .andExpect(status().isOk())
		        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andDo(print())
		        .andReturn();
    }
    
    @Test
    @DisplayName("많이 본 주식 Top20 조회 API 테스트(정상)")
    void search_view_top20_topics() {
    	mockMvc.perform(get("/api/rank/0"))
		    	.andDo(print())
		    	.andExpect(status().isOk())
		    	.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		        .andDo(print())
		    	.andReturn();
    }
    
    @Test
    @DisplayName("많이 본 주식 Top100 조회 API 테스트(정상)")
    void search_view_top100_topics() {
    	mockMvc.perform(get("/v1/rank/0"))
		    	.param("paging", 100))
		    	.andDo(print())
		    	.andExpect(status().isOk())
		    	.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		        .andDo(print())
		    	.andReturn();
    }
    
    @Test
    @DisplayName("많이 오른 주식 Top20 조회 API 테스트(정상)")
    void search_rise_top20_topics() {
    	mockMvc.perform(get("/v1/rank/1"))
		    	.andDo(print())
		    	.andExpect(status().isOk())
		    	.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		        .andDo(print())
		    	.andReturn();
    }
    
    @Test
    @DisplayName("많이 오른 주식 Top100 조회 API 테스트(정상)")
    void search_rise_top100_topics() {
    	mockMvc.perform(get("/v1/rank/1"))
		    	.param("paging", 100))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		        .andDo(print())
				.andReturn();
    }
    
    @Test
    @DisplayName("많이 내린 주식 Top20 조회 API 테스트(정상)")
    void search_drop_top20_topics() {
    	mockMvc.perform(get("/v1/rank/2"))
		    	.andDo(print())
		    	.andExpect(status().isOk())
		    	.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		        .andDo(print())
		    	.andReturn();
    }
    
    @Test
    @DisplayName("많이 내린 주식 Top100 조회 API 테스트(정상)")
    void search_drop_top100_topics() {
    	mockMvc.perform(get("/v1/rank/2"))
		    	.param("paging", 100))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		        .andDo(print())
				.andReturn();
    }
    
    @Test
    @DisplayName("많이 보유한 주식 Top20 조회 API 테스트(정상)")
    void search_volume_top20_topics() {
    	mockMvc.perform(get("/v1/rank/3"))
		    	.andDo(print())
		    	.andExpect(status().isOk())
		    	.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		        .andDo(print())
		    	.andReturn();
    }
    
    @Test
    @DisplayName("많이 보유한 주식 Top100 조회 API 테스트(정상)")
    void search_volume_top100_topics() {
    	mockMvc.perform(get("/v1/rank/3"))
		    	.param("paging", 100))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		        .andDo(print())
				.andReturn();
    }

}
