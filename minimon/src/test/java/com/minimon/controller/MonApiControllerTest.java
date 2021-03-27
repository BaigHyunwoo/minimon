package com.minimon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimon.entity.MonApi;
import com.minimon.enums.ResponseEnum;
import com.minimon.service.MonApiService;
import com.minimon.vo.MonApiCheckVO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Autowired
    private MonApiService monApiService;


    private MonApi getDefaultMonApi() {
        return MonApi.builder()
                .title("API 모니터링 목록 조회")
                .method(HttpMethod.GET)
                .url("http://localhost:8080/monApi")
                .timeout(5)
                .loadTime(2000)
                .errorLoadTime(3000)
                .build();
    }

    @Test
    public void getApi() throws Exception {
        MonApi monApi = monApiService.save(getDefaultMonApi());

        mockMvc.perform(get("/monApi/" + monApi.getSeq())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code", is(ResponseEnum.SUCCESS.getCode())))
                .andExpect(jsonPath("$.data.url", is(monApi.getUrl())))
                .andDo(print());
    }

    @Test
    public void getApiList() throws Exception {
        monApiService.save(getDefaultMonApi());

        mockMvc.perform(get("/monApi")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code", is(ResponseEnum.SUCCESS.getCode())))
                .andExpect(jsonPath("$.data.totalElements", not(0)))
                .andDo(print());
    }

    @Test
    public void create() throws Exception {
        mockMvc.perform(post("/monApi")
                .content(objectMapper.writeValueAsString(getDefaultMonApi()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code", is(ResponseEnum.SUCCESS.getCode())))
                .andDo(print());
    }

    @Test
    public void check() throws Exception {
        mockMvc.perform(post("/monApi/check")
                .content(objectMapper.writeValueAsString(MonApiCheckVO.builder().method(HttpMethod.GET.name()).url("https://www.naver.com").build()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code", is(ResponseEnum.SUCCESS.getCode())))
                .andDo(print());
    }
}