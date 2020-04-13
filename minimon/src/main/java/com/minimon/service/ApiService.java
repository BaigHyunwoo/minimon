package com.minimon.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minimon.common.CommonUtils;
import com.minimon.entity.TblMonApi;
import com.minimon.entity.TblMonApiParam;
import com.minimon.exceptionHandler.MyException;
import com.minimon.repository.TblMonUrlRepository;

@Service
public class ApiService {

	@Autowired
	TblMonUrlRepository tblMonUrlRepository;

	private String className = this.getClass().toString();

	private Logger logger = LoggerFactory.getLogger(ApiService.class);
	
	
	
	/**
	 * 
	 * 	URL 모니터링 검사 실행
	 * 
	 * 
	 * 
	 * 	@exception			핸들러로 처리	CODE 11
	 */
	public Map<String, Object> checkApis(List<TblMonApi> apis) throws Exception {
		Map<String, Object> checkData = new HashMap<String, Object>();
		
		try {
			
			for(TblMonApi api : apis) 	{
				Map<String, Object> logData = executeApi(api);
				checkData.put(""+api.getSeq(), errorCheckApi(api, logData));
			}
			
		}catch(Exception e) {
			e.printStackTrace();

			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - execute", className, 11);
         
		}
		
		return checkData;
	}
	
	

	
	/**
	 * 
	 * 	API 실행
	 * 
	 * 
	 * 
	 * 	@exception			핸들러로 처리	CODE 12
	 */
	public Map<String, Object> executeApi(TblMonApi api) throws Exception {
		Map<String, Object> logData = new HashMap<String, Object>();
		
		try {
			
			/*
			 * 실행
			 */
			logData = httpSending(api);
			
			logger.debug(logData.toString());
			
			
		}catch(Exception e) {
			e.printStackTrace();

			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - execute", className, 12);
         
		}
		
		return logData;
	}
	


	/**
	 * 
	 * 	URL 에러 검사
	 * 
	 * 
	 * 
	 * 	@exception			핸들러로 처리	CODE 13
	 */
	public Map<String, Object> errorCheckApi(TblMonApi api, Map<String, Object> logData) throws Exception {
		Map<String, Object> checkData = new HashMap<String, Object>();
		
		try {

			String result = "SUCCESS";
			int status = Integer.parseInt(""+logData.get("status"));
			double loadTime = Double.parseDouble(""+logData.get("loadTime"));
			double payLoad = Double.parseDouble(""+logData.get("payLoad"));
			String response = ""+logData.get("response");
			
			
			/*
			 * CHECK
			 */
			if(api.getStatus() == status) checkData.put("status", "SUCCESS");
			else {
				checkData.put("status", "ERR");
				result = "ERR";
			}
			
			if(loadTime <= CommonUtils.getPerData(api.getLoadTime(), api.getLoadTimePer(), 1)) checkData.put("loadTime", "SUCCESS");
			else {
				checkData.put("loadTime", "ERR");
				result = "ERR";
			}

			if(CommonUtils.getPerData(api.getPayLoad(), api.getPayLoadPer(), 2) <= payLoad 
					&& payLoad <= CommonUtils.getPerData(api.getPayLoad(), api.getPayLoadPer(), 1)) checkData.put("status", "SUCCESS");
			else {
				checkData.put("payLoad", "ERR");
				result = "ERR";
			}
			
			if(response.equals(api.getResponse()) == true) checkData.put("response", "SUCCESS");
			else {
				checkData.put("response", "ERR");
				result = "ERR";
			}

			
			/*
			 * SET PARAM
			 */
			checkData.put("check_loadTime",loadTime);
			checkData.put("check_payLoad",payLoad);
			checkData.put("check_status",status);
			checkData.put("check_response",response);
			checkData.put("origin_loadTime",api.getLoadTime());
			checkData.put("origin_payLoad",api.getPayLoad());
			checkData.put("origin_status",api.getStatus());
			checkData.put("origin_response", api.getResponse());
			checkData.put("url", api.getUrl());
			checkData.put("method", api.getMethod());
			checkData.put("seq", api.getSeq());
			checkData.put("result", result);
			
		}catch(Exception e) {
			e.printStackTrace();

			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - execute", className, 13);
         
		}
		
		return checkData;
	}



	/**
	 *  HTTP SENDING
	 *  
	 *  @author 백현우
	 *  @param	code				전송 코드
	 *  @param	url					전송 URL
	 *  @param	paramMap			전송 토큰 Params
	 *  @param	jsonMap				전송 JsonData
	 *  
	 * 	@exception			핸들러로 처리	CODE 14
	 */
	@SuppressWarnings("deprecation")
	public Map<String, Object> httpSending(TblMonApi api) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			
		    HttpClient httpclient = HttpClients.createDefault();
		    
		    List<NameValuePair> params = new ArrayList<NameValuePair>();
		    
		    for(TblMonApiParam tblMonApiParam : api.getApiParams()) {
		    	params.add(new BasicNameValuePair(tblMonApiParam.getParam_key(), tblMonApiParam.getParam_value()));
		    }
		    
	        long st = System.currentTimeMillis();
	        HttpResponse response = httpclient.execute(getHttpRequest(api.getMethod(), api.getUrl(), params));
        	long ed = System.currentTimeMillis();

        	result = getApiLogData(st, ed, response);
		    httpclient.getConnectionManager().shutdown();
        	
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - execute", className, 14);
			
		}

		return result;
	}
	


	/**
	 *  GET HTTP LOG DATA
	 *  
	 *  @author 백현우
	 *  
	 * 	@exception			핸들러로 처리	CODE 15
	 */
	public Map<String, Object> getApiLogData(long st, long ed, HttpResponse response) throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		BufferedReader reader = null;
		
	    try {
	    	
			long loadTime = ed-st;
	    	long payLoad = response.getEntity().getContentLength();
	    	int status = response.getStatusLine().getStatusCode();

        	StringBuffer responseData = new StringBuffer();
			if (status >= 200 && status < 300) {
				String inputLine = "";

				reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				
				while ((inputLine = reader.readLine()) != null) {
					responseData.append(inputLine);
		        }
				
		        reader.close();
				
				payLoad = response.getEntity().getContentLength();
				if(payLoad == -1) payLoad = CommonUtils.getByteLength(responseData.toString());
			}
	    	
			result.put("loadTime", loadTime);
			result.put("status", status);
			result.put("payLoad", payLoad);
			result.put("response", responseData.toString());
	        
		} catch (Exception e) {
			e.printStackTrace();
			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - execute", className, 15);
		}
        
        return result;
	}

	

	/**
	 *  GET HTTP REQUEST
	 *  
	 *  @author 백현우
	 *  
	 * 	@exception			핸들러로 처리	CODE 16
	 */
	public HttpUriRequest getHttpRequest(String method, String url, List<NameValuePair> params) throws Exception {
		HttpUriRequest http = null;

	    try {
	    	
	        if(method.equals("GET") == true) {
	
			    HttpGet httpget = new HttpGet(url);
			    
			    http = httpget;
	        }
	        else if(method.equals("POST") == true) {
	
			    HttpPost httppost = new HttpPost(url);
	
				httppost.setEntity(new UrlEncodedFormEntity(params));
			    
			    http = httppost;
	        	
	        }
	        else if(method.equals("PUT") == true) {
	
			    HttpPut httpput = new HttpPut(url);
	
			    httpput.setEntity(new UrlEncodedFormEntity(params));
			    
			    http = httpput;
	        	
	        }
	        else if(method.equals("DELETE") == true) {
	
			    HttpDelete httpdelete = new HttpDelete(url);
			    
			    http = httpdelete;
	        	
	        }
	        else if(method.equals("PATCH") == true) {
	
			    HttpPatch httppatch = new HttpPatch(url);
	
			    httppatch.setEntity(new UrlEncodedFormEntity(params));
			    
			    http = httppatch;
	        	
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - execute", className, 16);
		}
        
        return http;
	}
	
}
