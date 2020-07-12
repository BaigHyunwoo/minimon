package com.minimon.service;

import com.minimon.common.SendingHttp;
import com.minimon.entity.MonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsService {

    public void sendSimpleMessage(String to, MonResult monResult) throws Exception {
        String text = new StringBuffer()
                .append("\n" + monResult.getRegDate() + " ")
                .append("\n" + monResult.getType() + " : " + monResult.getTitle() + " ")
                .append("\nRESULT : " + monResult.getResult() + " ")
                .toString();

        SendingHttp sendingHttp = new SendingHttp();
        sendingHttp.sendingMassage("https://www.yanadoo.co.kr/minimon/sms/send", text, to);
        log.info("SEND SMS : " + to + "  Body : " + text);
    }
}
