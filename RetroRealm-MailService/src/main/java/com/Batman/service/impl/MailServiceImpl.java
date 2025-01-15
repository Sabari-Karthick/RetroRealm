package com.Batman.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.Batman.constants.KafkaConstants;
import com.Batman.dto.OrderDetailsDto;
import com.Batman.mail.EmailDetails;
import com.Batman.service.IMailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("Mail Service")
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements IMailService{

	private final JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String fromEmail;
	
	@Override
	public void sendMail(EmailDetails emailDetails) {
		log.info("Entering Send Mail...");
		javaMailSender.send(javaMailSender.createMimeMessage());
	}
	
	@KafkaListener(topics = KafkaConstants.TOPIC,groupId = KafkaConstants.GROUP_ID)
	public void orderPlacedEventListener(OrderDetailsDto orderDetailsDto) {
		log.info("Entering Kafka Listener ...");
		log.info("Order Id :: {}",orderDetailsDto.getOrderId());
	}

}
