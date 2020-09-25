package com.minimon.common;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class CommonSender {

    public void sendingMassage(String url, String data) throws Exception {
        List<NameValuePair> params = new ArrayList<>();
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        params.add(new BasicNameValuePair("data", URLEncoder.encode(data, "UTF-8")));
        httppost.setEntity(new UrlEncodedFormEntity(params));
        httpclient.execute(httppost);
    }
}
