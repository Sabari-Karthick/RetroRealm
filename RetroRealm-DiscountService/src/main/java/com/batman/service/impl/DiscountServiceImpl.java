package com.batman.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.batman.dto.discount.DiscountRequest;
import com.batman.dto.events.DiscountPlacedEvent;
import com.batman.entity.Discount;
import com.batman.exception.wrapper.DiscountAlreadyExistException;
import com.batman.exception.wrapper.FailedToUpdateGameServiceException;
import com.batman.exception.wrapper.InputFieldException;
import com.batman.feignclients.GameFeignClient;
import com.batman.mapper.CommonMapper;
import com.batman.repository.IDiscountRepository;
import com.batman.service.IDiscountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class DiscountServiceImpl implements IDiscountService {

	private final IDiscountRepository discountRepository;

	private final CommonMapper mapper;

	private final GameFeignClient gameFeignClient;
	
	private final KafkaTemplate<String, DiscountPlacedEvent> kafkaTemplate;
 
	@Override
	public Discount createDiscount(DiscountRequest discountRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InputFieldException(bindingResult.getFieldError().getDefaultMessage());
		}
		List<Discount> list = discountRepository.findDiscountsOfGames(discountRequest.getGameIds());
		if (list.size() > 0) {
			throw new DiscountAlreadyExistException(
					"Already A Discount Exists For one of this Ids ::" + discountRequest.getGameIds());
		}
		Discount discount = mapper.convertToEntity(discountRequest, Discount.class);
		discount.setIsExpired(false);
		Discount savedDiscount = discountRepository.save(discount);
		log.info("Discount Added ...");
		log.info("Sending Update Request to Game Service ....");
		
		/**
		 * This should be updated to kafka instead of manual calling
		 * 
		 * 
		 */
		
		
		
		ResponseEntity<?> response = gameFeignClient.addDiscountToGames(discountRequest.getGameIds(),
				discountRequest.getDiscountValue());
		if (response.getStatusCode().is2xxSuccessful()) {
			log.info("SuccessFull Response Received....");
			return savedDiscount;
		} else {
			log.error("Failed to Update Game Discount....");
			throw new FailedToUpdateGameServiceException(
					response.getStatusCode() + " Response Arrived From Game Service");
		}
	}

	@Override
	public List<Discount> getDiscountByGameIds(Set<Integer> gameIds) {
		log.info("Discount Service Fetching Game Discounts For :::" + gameIds);
		return discountRepository.findDiscountsOfGames(gameIds);

	}

}
