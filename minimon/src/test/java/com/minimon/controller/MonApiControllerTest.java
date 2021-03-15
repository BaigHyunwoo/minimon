package com.minimon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimon.common.CommonResponseMeta;
import com.minimon.entity.MonApi;
import com.minimon.entity.MonUrl;
import com.minimon.enums.ResponseEnum;
import com.minimon.vo.MonApiCheckVO;
import com.minimon.vo.MonUrlCheckVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class MonApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void create() throws Exception {
        String content = objectMapper.writeValueAsString(MonApi
                .builder()
                .title("API 모니터링 목록 조회")
                .method(HttpMethod.GET)
                .url("http://localhost:8080/monApi")
                .timeout(5)
                .loadTime(2000)
                .errorLoadTime(3000)
                .build());

        mockMvc.perform(post("/monApi")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code", is(ResponseEnum.SUCCESS.getCode())))
                .andDo(print());
    }

    @Test
    public void check() throws Exception {
        String content = objectMapper.writeValueAsString(MonApiCheckVO.builder().method(HttpMethod.GET.toString()).url("https://www.naver.com").data("1"));

        mockMvc.perform(post("/monApi/check")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}