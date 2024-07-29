package com.Batman.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.Batman.enums.Entity;
import com.Batman.service.IMailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("Mail Service")
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements IMailService{

	private final JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private final String fromEmail;
	
	@Override
	public void sendMail(Integer entityID, Entity entityName) {
		log.info("Entering Send Mail...");
		javaMailSender.send(javaMailSender.createMimeMessage());
	}

}
