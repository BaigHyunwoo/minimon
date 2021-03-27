package com.minimon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimon.common.CommonSearchSpec;
import com.minimon.entity.MonAct;
import com.minimon.entity.MonApi;
import com.minimon.entity.MonCodeData;
import com.minimon.vo.MonitoringResultVO;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class MonActServiceTest {

    @Autowired
    private MonActService monActService;

    @Autowired
    private ObjectMapper objectMapper;


    private MockMultipartFile getTestFile() {
        Path path = Paths.get("src/main/resources/testFiles/FindTest.java");
        String name = "transactionFile";
        String originalFileName = "FindTest.java";
        String contentType = ContentType.TEXT_PLAIN.getMimeType();
        byte[] content = null;

        try {
            content = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new MockMultipartFile(name, originalFileName, contentType, content);
    }

    private MonAct getDefaultMonAct() {
        return MonAct.builder()
                .title("네이버 검색")
                .errorLoadTime(8000)
                .timeout(10)
                .loadTime(7000)
                .codeDataList(monActService.getTestSource(getTestFile()))
                .build();
    }

    @Test
    void save() {
        MonAct monAct = getDefaultMonAct();
        assertEquals(monAct, monActService.save(monAct));
    }

    @Test
    void getList() {
        monActService.save(getDefaultMonAct());
        long size = monActService.getList(new CommonSearchSpec()).getTotalElements();
        assertNotEquals(0, size);
    }

    @Test
    void get() {
        MonAct monAct = monActService.save(getDefaultMonAct());
        MonAct selectMonAct = monActService.get(monAct.getSeq()).get();
        assertEquals(monAct.getSeq(), selectMonAct.getSeq());
    }

    @Test
    void remove() {
        MonAct monAct = monActService.save(getDefaultMonAct());
        monActService.remove(monAct.getSeq());
        Optional selectMonAct = monActService.get(monAct.getSeq());
        assertNotEquals(true, selectMonAct.isPresent());
    }

    @Test
    void getTestSource() {
        List<MonCodeData> codeDataList = monActService.getTestSource(getTestFile());
        assertNotNull(codeDataList.size());
    }

    @Test
    void executeCodeList() {
        MonitoringResultVO result = monActService.executeCodeList(getTestFile());
        assertEquals(HttpStatus.OK, result.getStatus());
    }
}