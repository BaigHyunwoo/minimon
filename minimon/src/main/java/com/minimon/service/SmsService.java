package com.minimon.service;

import com.minimon.common.SendingHttp;
import com.minimon.entity.TblMonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private Logger logger = LoggerFactory.getLogger(SmsService.class);

    public void sendSimpleMessage(String to, TblMonResult tblMonResult) throws Exception{
        String text = new StringBuffer()
                .append("\n"+tblMonResult.getRegDate()+" ")
                .append("\n"+tblMonResult.getType()+" : "+tblMonResult.getTitle()+" ")
                .append("\nERR : "+tblMonResult.getResult()+" ")
                .toString();

        SendingHttp sendingHttp = new SendingHttp();
        sendingHttp.sendingMassage("https://www.yanadoo.co.kr/minimon/sms/send", text, to);
        logger.info("SEND SMS : "+to+"  Body : "+text);
    }
}
