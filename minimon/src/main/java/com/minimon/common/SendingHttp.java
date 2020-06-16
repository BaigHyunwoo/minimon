package com.minimon.common;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimon.exceptionHandler.MyException;




/**
 * 
 * 각 서버에게 전송을 담당 
 * 
 * 전송 후 데이터를 반환
 * 
 * 
 * 
 * @author 백현우
 *
 */
public class SendingHttp {

	
	private String className = this.getClass().toString();


	/**
	 *  HTTP CALL GET
	 *
	 *
	 *  @author 백현우
	 *
	 *
	 *  @param	url					전송 URL
	 *  @param	data				전송 JSON DATA
	 *
	 *
	 * 	@throws MyException 모든 Exception을 ExceptionHandler에서 처리 에러코드 11
	 */
	@SuppressWarnings("deprecation")
	public Map<String,Object> sendingMassage(String url, String data) throws Exception {

		Map<String,Object> returnMap = new HashMap<String, Object>();

		try {


			HttpClient httpclient = HttpClients.createDefault();

			HttpPost httppost = new HttpPost(url);

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("data",  URLEncoder.encode(data, "UTF-8") ) );

			httppost.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse res = httpclient.execute(httppost);

			HttpEntity resEntity = res.getEntity();

			if(resEntity != null) {

				String json = EntityUtils.toString(resEntity);

				String decodeStr = URLDecoder.decode(json,"UTF-8");

				ObjectMapper mapper = new ObjectMapper();

				Map<String, Object> map = new HashMap<String, Object>();

				map = mapper.readValue(decodeStr, new TypeReference<Map<String, Object>>(){});

				returnMap = map;

			}

			httpclient.getConnectionManager().shutdown();

		} catch (Exception e) {
			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - " + e.getStackTrace()[0].getMethodName() , className, 11);
		}

		return returnMap;

	}

	/**
	 *  HTTP CALL WITH CELLPHONE
	 *  
	 *  
	 *  @author 백현우
	 *  
	 *  
	 *  @param	url					전송 URL
	 *  @param	data				전송 JSON DATA
	 *  
	 *  
	 * 	@throws MyException 모든 Exception을 ExceptionHandler에서 처리 에러코드 11
	 */
	@SuppressWarnings("deprecation")
	public Map<String,Object> sendingMassage(String url, String data, String cellPhone) throws Exception {
		
		Map<String,Object> returnMap = new HashMap<String, Object>();
		
		try {
			
		    HttpClient httpclient = HttpClients.createDefault();
		    
		    HttpPost httppost = new HttpPost(url);
		    
		    List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("data",  data ) );
			params.add(new BasicNameValuePair("cellPhone",  cellPhone ) );
		    
		    httppost.setEntity(new UrlEncodedFormEntity(params));
		    
		    HttpResponse res = httpclient.execute(httppost);

		    HttpEntity resEntity = res.getEntity();
		    
		    httpclient.getConnectionManager().shutdown();

		} catch (Exception e) {
			e.printStackTrace();
			throw new MyException("CLASS : " + className + " - METHOD : " +  new Object(){}.getClass().getEnclosingMethod().getName()  + " "
					+ "- TYPE = [Function]/  Function - " + e.getStackTrace()[0].getMethodName() , className, 11);
		}

		return returnMap;
		
	}
	
	


}
