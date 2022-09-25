package com.kakaopaysec.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.kakaopaysec.service.TransactionService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private WebApplicationContext ctx;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    TransactionService transactionService;

    @Test
    void maxSumAmt() throws Exception {
        mockMvc.perform(get("/year/maxSumAmt"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void yearNotTrx() throws Exception {
        mockMvc.perform(get("/year/notTrx"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void yearBranSumAmt() throws Exception {
        mockMvc.perform(get("/year/branSumAmt"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void branTransfer1() throws Exception {
        mockMvc.perform(get("/bran/transfer?brName=분당점"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    void branTransfer2() throws Exception {
        mockMvc.perform(get("/bran/transfer?brName=판교점"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }

}