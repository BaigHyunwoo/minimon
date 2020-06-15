package com.minimon.service;

import com.minimon.common.SendingHttp;
import com.minimon.entity.TblMonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private Logger logger = LoggerFactory.getLogger(SmsService.class);

    public void sendSimpleMessage(String to, String subject, TblMonResult tblMonResult) throws Exception{
        String text = new StringBuffer()
                .append("<h1>모니터링 결과입니다.</h1>")
                .append("<p> 타이틀 : "+tblMonResult.getTitle()+"</p>")
                .append("<p> 종류 : "+tblMonResult.getType()+"</p>")
                .append("<p> SEQ : "+tblMonResult.getMon_seq()+"</p>")
                .append("<p> 일시 : "+tblMonResult.getRegDate()+"</p>")
                .append("<p> 결과 : "+tblMonResult.getResult()+"</p>")
                .append("<p> 걸린 시간 : "+tblMonResult.getLoadTime()+"Ms</p>")
                .append("<p> 응답 : "+tblMonResult.getResponse()+"</p>")
                .toString();

        SendingHttp sendingHttp = new SendingHttp();
//        sendingHttp.sendingMassage("https://www.yanadoo.co.kr/minimon/sms/send", text, "010-2407-1563");
        sendingHttp.sendingMassage("http://localhost:8080/minimon/sms/send", text, "010-2407-1563");

        logger.info("SEND MAIL : "+to+"  Body : "+text);
    }
}
