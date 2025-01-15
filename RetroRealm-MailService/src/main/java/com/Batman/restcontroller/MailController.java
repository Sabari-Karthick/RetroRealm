package com.Batman.restcontroller;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Batman.enums.Entity;
import com.Batman.service.IMailService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/notify")
@AllArgsConstructor
@Slf4j
public class MailController {
	
	private final IMailService mailService;
	
	@PostMapping("/{id}/{entityName}")
	public ResponseEntity<?> sendNotification(@PathVariable(value = "id")Integer id,@PathVariable("entityName") Entity entityName){
		log.info("Mail Sending Request Arrived For {} with Id :: {} ",entityName,id);
		mailService.sendMail(null);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
