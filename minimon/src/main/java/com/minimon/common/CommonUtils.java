package com.minimon.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtils {

	/**
	 * 
	 *  UTC 형태의 데이터를 GMT 밀리세컨즈로 변환
	 *  
	 *  
	 *  @author 백현우
	 *  
	 *  
	 *  @param 	input 가공 데이터
	 *  
	 *  @return 가공 결과 반환
	 *  
	 */
	@SuppressWarnings("deprecation")
	static long convertUTCtoGMT(Double input) {
	    String format = "yyyy/MM/dd HH:mm:ss.SSS";
	    
	    SimpleDateFormat sdf = new SimpleDateFormat(format);
	    
	    // GMT 시간
	    String retime = sdf.format(input*1000);
	    
	    // SPLIT time, mills
	    String[] str = retime.split("\\.");
	    
	    // get Date of split time exclude mills
	    Date date = new Date(str[0]);
	    
	    // convert mills
	    long mills = date.getTime();
	    
	    // include mills
	    mills = mills+Long.parseLong(str[1]);
	    
	    return mills;
	}
	


	/**
	 * 
	 *  DateTime Formatter
	 *  
	 *  
	 *  @author 백현우
	 *  
	 *  
	 *  @param 	input 가공 데이터
	 *  
	 *  @return 가공 결과 반환
	 *  
	 */
	static String datetimeFormat(Date input) {
	    String format = "yyyy/MM/dd HH:mm:ss.SSS";
	    
	    SimpleDateFormat sdf = new SimpleDateFormat(format);
	    
	    
	    return sdf.format(input);
	}
	
	
	/**
	 * 
	 * 	JSON STRING TO MAP
	 * 
	 */
	public static Map<String, String> jsonStringToMap(String inputString) throws Exception{
    	ObjectMapper mapper = new ObjectMapper();
        Map<String, String> result = null;
        
		try {
			
			result = mapper.readValue(inputString, new TypeReference<Map<String, String>>(){});
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
 
        return result;
    }
	

	/**
	 * 
	 * 	PERCENT 적용 데이터 반환
	 * 
	 * 	@param 	type 1 : +
	 * 			type 2 : -
	 * 
	 */
	public static double getPerData(double data, int per, int type) {
		if(type == 1) {
			return data+(data/100*per);
		}
		else {
			return data-(data/100*per);
		}
		
	}
	


	/**
	 * 
	 * 	GET BYTE OF STRING
	 * 
	 */
	public static long getByteLength(String str) {

		long strLength = 0;
		char tempChar[] = new char[str.length()];
		for (int i = 0; i < tempChar.length; i++) {

			tempChar[i] = str.charAt(i);
			if (tempChar[i] < 128) {
				strLength++;
			} else {
				strLength += 2;
			}
		}
		return strLength;
	}
	
}
