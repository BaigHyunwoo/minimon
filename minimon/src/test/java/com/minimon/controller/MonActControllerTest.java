package com.minimon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimon.entity.MonAct;
import com.minimon.entity.MonUrl;
import com.minimon.enums.ResponseEnum;
import com.minimon.service.MonActService;
import org.apache.http.entity.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class MonActControllerTest {

    @Autowired
    private MonActService monActService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMultipartFile getTestFile() throws IOException {
        Path path = Paths.get("src/main/resources/testFiles/FindTest.java");
        String name = "transactionFile";
        String originalFileName = "FindTest.java";
        String contentType = ContentType.TEXT_PLAIN.getMimeType();
        byte[] content = Files.readAllBytes(path);
        return new MockMultipartFile(name, originalFileName, contentType, content);
    }

    private MonAct getDefaultMonAct() throws IOException {
        return MonAct.builder()
                .title("네이버 검색")
                .errorLoadTime(8000)
                .timeout(10)
                .loadTime(7000)
                .codeDataList(monActService.getTestSource(getTestFile()))
                .build();
    }

    @Test
    public void getAct() throws Exception {
        MonAct monAct = monActService.save(getDefaultMonAct());

        mockMvc.perform(get("/monAct/"+monAct.getSeq())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code", is(ResponseEnum.SUCCESS.getCode())))
                .andExpect(jsonPath("$.data.seq", is(monAct.getSeq())))
                .andDo(print());
    }

    @Test
    public void getActList() throws Exception {
        monActService.save(getDefaultMonAct());

        mockMvc.perform(get("/monAct")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code", is(ResponseEnum.SUCCESS.getCode())))
                .andExpect(jsonPath("$.data.totalElements", not(0)))
                .andDo(print());
    }

    @Test
    public void create() throws Exception {
        mockMvc.perform(post("/monAct")
                .content(objectMapper.writeValueAsString(getDefaultMonAct()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code", is(ResponseEnum.SUCCESS.getCode())))
                .andDo(print());
    }

    @Test
    public void check() throws Exception {
        mockMvc.perform(multipart("/monAct/check")
                .file(getTestFile()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code", is(ResponseEnum.SUCCESS.getCode())))
                .andDo(print());
    }
}