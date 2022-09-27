package com.kakaopaysec.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * @author wijihoon
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private WebApplicationContext ctx;
    @Autowired
    private MockMvc mockMvc;
    
    @BeforeEach
    void setUp() {
    	this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
		                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
		                .alwaysDo(print())
		                .build();
    }

    @Test
    @DisplayName("모든 주제 순위 랜덤 변경 API 테스트(정상)")
    void update_institutions() throws Exception {
    	mockMvc.perform(post("/v1/stock/random"))
		        .andDo(print())
		        .andExpect(status().isOk())
		        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
		        .andReturn();
    }
    
    @Test
    @DisplayName("모든 주제 순위를 랜덤하게 변경한다.(실패)")
    void addAccountsfail() throws Exception {
    	
    	mockMvc.perform(post("/v1/stock/random"))
		        .andDo(print()).andExpect(status().is5xxServerError())
		        .andExpect(handler().handlerType(TransactionController.class))
		        .andExpect(handler().methodName("add"))
		        .andExpect(jsonPath("$.success", is(false)))
		        .andExpect(jsonPath("$.error").exists());
    }
    
    @Test
    @DisplayName("모든 주제 Top5 조회 API 테스트(정상)")
    void search_all_top5_topics() throws Exception {
    	mockMvc.perform(get("/v1/stock/rank"))
		        .andDo(print())
		        .andExpect(status().isOk())
		        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
		        .andReturn();
    }
    
    @Test
    @DisplayName("많이 본 주식 Top20 조회 API 테스트(정상)")
    void search_view_top20_topics() throws Exception {
    	mockMvc.perform(get("/v1/stock/rank/0"))
		    	.andDo(print())
		    	.andExpect(status().isOk())
		    	.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		        .andDo(print())
		    	.andReturn();
    }
    
    @Test
    @DisplayName("많이 본 주식 Top100 조회 API 테스트(정상)")
    void search_view_top100_topics() throws Exception {
    	mockMvc.perform(get("/v1/stock/rank/0?paging=100"))
		    	.andDo(print())
		    	.andExpect(status().isOk())
		    	.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		        .andDo(print())
		    	.andReturn();
    }
    
    @Test
    @DisplayName("많이 오른 주식 Top20 조회 API 테스트(정상)")
    void search_rise_top20_topics() throws Exception {
    	mockMvc.perform(get("/v1/stock/rank/1"))
		    	.andDo(print())
		    	.andExpect(status().isOk())
		    	.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		        .andDo(print())
		    	.andReturn();
    }
    
    @Test
    @DisplayName("많이 오른 주식 Top100 조회 API 테스트(정상)")
    void search_rise_top100_topics() throws Exception {
    	mockMvc.perform(get("/v1/stock/rank/1?paging=100"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		        .andDo(print())
				.andReturn();
    }
    
    @Test
    @DisplayName("많이 내린 주식 Top20 조회 API 테스트(정상)")
    void search_drop_top20_topics() throws Exception {
    	mockMvc.perform(get("/v1/stock/rank/2"))
		    	.andDo(print())
		    	.andExpect(status().isOk())
		    	.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		        .andDo(print())
		    	.andReturn();
    }
    
    @Test
    @DisplayName("많이 내린 주식 Top100 조회 API 테스트(정상)")
    void search_drop_top100_topics() throws Exception {
    	mockMvc.perform(get("/v1/stock/rank/2?paging=100"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		        .andDo(print())
				.andReturn();
    }
    
    @Test
    @DisplayName("많이 보유한 주식 Top20 조회 API 테스트(정상)")
    void search_volume_top20_topics() throws Exception {
    	mockMvc.perform(get("/v1/stock/rank/3"))
		    	.andDo(print())
		    	.andExpect(status().isOk())
		    	.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		        .andDo(print())
		    	.andReturn();
    }
    
    @Test
    @DisplayName("많이 보유한 주식 Top100 조회 API 테스트(정상)")
    void search_volume_top100_topics() throws Exception {
    	mockMvc.perform(get("/v1/stock/rank/3?paging=100"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		        .andDo(print())
				.andReturn();
    }

}
