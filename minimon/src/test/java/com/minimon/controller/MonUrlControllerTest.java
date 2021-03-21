package com.minimon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimon.entity.MonUrl;
import com.minimon.enums.ResponseEnum;
import com.minimon.service.MonUrlService;
import com.minimon.vo.MonUrlCheckVO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private MonUrlService monUrlService;


    private MonUrl getDefaultMonUrl(){
        return MonUrl.builder()
                .url("https://www.naver.com")
                .title("네이버")
                .errorLoadTime(3000)
                .timeout(5)
                .loadTime(2000)
                .build();
    }

    @Test
    public void getUrl() throws Exception {
        MonUrl monUrl = monUrlService.save(getDefaultMonUrl());

        mockMvc.perform(get("/monUrl/"+monUrl.getSeq())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code", is(ResponseEnum.SUCCESS.getCode())))
                .andExpect(jsonPath("$.data.url", is(monUrl.getUrl())))
                .andDo(print());
    }

    @Test
    public void getUrlList() throws Exception {
        monUrlService.save(getDefaultMonUrl());

        mockMvc.perform(get("/monUrl")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code", is(ResponseEnum.SUCCESS.getCode())))
                .andExpect(jsonPath("$.data.totalElements", not(0)))
                .andDo(print());
    }

    @Test
    public void create() throws Exception {
        mockMvc.perform(post("/monUrl")
                .content(objectMapper.writeValueAsString(getDefaultMonUrl()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code", is(ResponseEnum.SUCCESS.getCode())))
                .andDo(print());
    }

    @Test
    public void check() throws Exception {
        mockMvc.perform(post("/monUrl/check")
                .content(objectMapper.writeValueAsString(MonUrlCheckVO.builder().url("https://www.naver.com").timeout(5).build()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code", is(ResponseEnum.SUCCESS.getCode())))
                .andDo(print());
    }
}