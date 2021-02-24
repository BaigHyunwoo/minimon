package com.minimon.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class CommonRestTemplate {
    private final Charset charset = Charset.forName("UTF-8");

    /**
     * makeHeader for json
     */
    private HttpHeaders makeHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    /**
     * makeRestTemplate
     */
    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(charset));
        return restTemplate;
    }

    /**
     * HTTP.GET
     */
    private ResponseEntity get(RestTemplate restTemplate, String url) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(makeHeader()), String.class);
    }

    /**
     * HTTP.POST
     */
    private ResponseEntity post(RestTemplate restTemplate, String url, Object param) {
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(param, makeHeader()), String.class);
    }

    /**
     * HTTP.PUT
     */
    private ResponseEntity put(RestTemplate restTemplate, String url, Object param) {
        return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(param, makeHeader()), String.class);
    }

    /**
     * HTTP.DELETE
     */
    private ResponseEntity delete(RestTemplate restTemplate, String url, Object param) {
        return restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(param, makeHeader()), String.class);
    }

    /**
     * send request by method
     */
    private String call(HttpMethod method, String url, Object param) {
        String result = null;
        RestTemplate restTemplate = getRestTemplate();
        ResponseEntity response = null;

        switch (method) {
            case GET:
                response = get(restTemplate, url);
                break;

            case POST:
                response = post(restTemplate, url, param);
                break;

            case PUT:
                response = put(restTemplate, url, param);
                break;

            case DELETE:
                response = delete(restTemplate, url, param);
                break;
        }

        if (response != null && response.getStatusCode() == HttpStatus.OK) {

            result = response.getBody().toString();

        } else {
            log.info("ERROR  " + method + " : " + url);
        }
        return result;
    }

    /**
     * send request without params
     */
    public String callApi(HttpMethod method, String url) {
        return call(method, url, null);
    }

    /**
     * send request
     */
    public String callApi(HttpMethod method, String url, Object param) {
        return call(method, url, param);
    }

    /**
     * convert String to Map
     */
    public Map convertToMap(String data) {
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
    public String createUrlWithParams(HashMap<String, Object> param, String url) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
        param.entrySet().forEach(entry -> {
            uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue());
        });
        return uriComponentsBuilder.build(false).toUriString();
    }
}
