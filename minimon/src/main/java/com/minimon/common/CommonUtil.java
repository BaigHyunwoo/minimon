package com.minimon.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CommonUtil {

    public static double getPerData(double data, int per, int type) {
        if (type == 1) {
            return data + (data / 100 * per);
        } else {
            return data - (data / 100 * per);
        }
    }

    /**
     * convert String to Object
     */
    public static <T> T convertToObject(String data, Class<T> T) {
        try {
            return new ObjectMapper().readValue(data, T);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("ERROR  Convert To Object " + data);
        }
        return null;
    }

    /**
     * convert String to Map
     */
    public static Map convertToMap(String data) {
        try {
            return new ObjectMapper().readValue(data, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("ERROR  Convert To Map " + data);
        }
        return null;
    }

    /**
     * create new url with params
     */
    public static String createUrl(HashMap<String, Object> param, String url) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
        param.entrySet().forEach(entry -> {
            uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue());
        });
        return uriComponentsBuilder.build(false).toUriString();
    }
}
