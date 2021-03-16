package com.minimon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimon.enums.ResponseEnum;
import org.apache.http.entity.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class MonActControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void check() throws Exception {
        Path path = Paths.get("src/main/resources/testFiles/DicFindTest.java");
        String name = "transactionFile";
        String originalFileName = "DifFindTest.java";
        String contentType = ContentType.TEXT_PLAIN.getMimeType();
        byte[] content = Files.readAllBytes(path);

        mockMvc.perform(multipart("/monAct/check")
                .file(new MockMultipartFile(name, originalFileName, contentType, content)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code", is(ResponseEnum.SUCCESS.getCode())))
                .andDo(print());
    }
}