package com.minimon.service;

import com.minimon.common.CommonSearchSpec;
import com.minimon.entity.MonUrl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class MonUrlServiceTest {

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
    public void save() {
        MonUrl monUrl = getDefaultMonUrl();
        assertEquals(monUrl, monUrlService.save(monUrl));
    }

    @Test
    public void getList() {
        monUrlService.save(getDefaultMonUrl());
        long size = monUrlService.getList(new CommonSearchSpec()).getTotalElements();
        assertNotEquals(0, size);
    }

    @Test
    public void get() {
        MonUrl monUrl = monUrlService.save(getDefaultMonUrl());
        MonUrl selectMonUrl = monUrlService.get(1).get();
        assertEquals(monUrl.getSeq(), selectMonUrl.getSeq());
    }
}