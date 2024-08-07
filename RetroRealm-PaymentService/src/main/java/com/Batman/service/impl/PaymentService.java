package com.Batman.service.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Service;

import com.Batman.entity.Payment;
import com.Batman.enums.PaymentStatus;
import com.Batman.enums.PaymentType;
import com.Batman.repository.IPaymentRepository;
import com.Batman.service.IPaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service("payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentService implements IPaymentService{
	
	private final IPaymentRepository paymentRepository;  

	@Override
	public Payment pay(Double amount, PaymentType paymentType) {
		ZonedDateTime initiatedTime = ZonedDateTime.now(ZoneId.systemDefault());
		log.info("Payment Request Arrived for amount {} with Type {} initated at {}",amount,paymentType,initiatedTime);
		
		Payment payment = new Payment();
		try {
			//Actual Payment Logic Needs to be implemented
			Thread.sleep(10000);
            payment.setPaymentStatus(PaymentStatus.COMPLETED);			
		} catch (InterruptedException ignore) {
			payment.setPaymentStatus(PaymentStatus.FAILED);			
		}
		ZonedDateTime CompletedTime = ZonedDateTime.now(ZoneId.systemDefault());
		payment.setIntiatedTime(initiatedTime);
		payment.setCompletedTime(CompletedTime);
		payment.setPaymentType(paymentType);
		payment.setAmount(amount);
		return paymentRepository.save(payment);
	}

}
