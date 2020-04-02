package com.minimon.common;

import java.text.SimpleDateFormat;
import java.util.Date;

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
}
