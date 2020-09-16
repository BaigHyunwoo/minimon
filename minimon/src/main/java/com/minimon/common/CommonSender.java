package com.minimon.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommonSender {
    public Map<String, Object> sendingMassage(String url, String data) throws Exception {

        Map<String, Object> returnMap = new HashMap<String, Object>();
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);

        params.add(new BasicNameValuePair("data", URLEncoder.encode(data, "UTF-8")));

        httppost.setEntity(new UrlEncodedFormEntity(params));
        HttpResponse res = httpclient.execute(httppost);
        HttpEntity resEntity = res.getEntity();

        if (resEntity != null) {
            String json = EntityUtils.toString(resEntity);
            String decodeStr = URLDecoder.decode(json, "UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(decodeStr, new TypeReference<Map<String, Object>>() {
            });
            returnMap = map;
        }

        return returnMap;

    }
}
