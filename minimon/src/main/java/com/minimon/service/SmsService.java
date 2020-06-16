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
                .append("YND monitoring Result Message")
                .append("\nTitle : "+tblMonResult.getTitle()+"  ")
                .append("\nType : "+tblMonResult.getType()+" ")
                .append("\nSEQ : "+tblMonResult.getMon_seq()+" ")
                .append("\nDate : "+tblMonResult.getRegDate()+" ")
                .append("\nResult : "+tblMonResult.getResult()+" ")
                .append("\nLoad Time : "+tblMonResult.getLoadTime()+"Ms ")
                .append("\nResponse : \n"+tblMonResult.getResponse().replaceAll(",", ",\n")+" ")
                .toString();

        SendingHttp sendingHttp = new SendingHttp();
        sendingHttp.sendingMassage("https://www.yanadoo.co.kr/minimon/sms/send", text, to);
        logger.info("SEND SMS : "+to+"  Body : "+text);
    }
}
