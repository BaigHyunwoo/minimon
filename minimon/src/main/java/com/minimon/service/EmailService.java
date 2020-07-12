package com.minimon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
//
//    private final JavaMailSender emailSender;
//
//    public void sendSimpleMessage(String to, String subject, TblMonResult tblMonResult) throws MessagingException {
//        String text = new StringBuffer()
//                .append("<p>" + tblMonResult.getRegDate() + "</p>")
//                .append("<p>" + tblMonResult.getType() + " : " + tblMonResult.getTitle() + "</p>")
//                .append("<p>RESULT : " + tblMonResult.getResult() + "</p>")
//                .toString();
//
//        MimeMessage message = emailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//        helper.setSubject(subject);
//        helper.setTo(to);
//        helper.setText(text, true);
//        emailSender.send(message);
//
//        log.info("SEND MAIL : " + to + "  Body : " + text);
//    }
}
