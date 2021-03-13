package com.minimon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimon.entity.MonUrl;
import com.minimon.vo.MonUrlCheckVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class MonUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void create() throws Exception {
        String content = objectMapper.writeValueAsString(MonUrl.builder()
                .url("https://www.naver.com")
                .title("네이버")
                .errorLoadTime(3000)
                .timeout(5)
                .loadTime(2000)
                .build());

        mockMvc.perform(post("/monUrl")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code", is(200)))
                .andDo(print());
    }

    @Test
    public void check() throws Exception {
        String content = objectMapper.writeValueAsString(new MonUrlCheckVO("https://www.naver.com", 5));

        mockMvc.perform(post("/monUrl/check")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}