package com.minimon.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.minimon.entity.TblMonResult;

@Service
public class EmailService{
	
	@Autowired
    public JavaMailSender emailSender;

	private Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendSimpleMessage(String to, String subject, TblMonResult tblMonResult) throws MessagingException {
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
    	
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setSubject(subject);
        helper.setTo(to);
        helper.setText(text, true);
        //emailSender.send(message);
        
        logger.info("SEND MAIL : "+to+"  Body : "+text);
    }
}
