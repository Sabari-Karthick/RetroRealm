package com.batman.service.impl;


import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.batman.constants.KafkaConstants;
import com.batman.dto.discount.DiscountRequest;
import com.batman.dto.events.DiscountPlacedEvent;
import com.batman.entity.Discount;
import com.batman.enums.DiscountStatus;
import com.batman.events.DiscountCreatedEvent;
import com.batman.exception.wrapper.DiscountAlreadyExistException;
import com.batman.exception.wrapper.FailedToUpdateGameServiceException;
import com.batman.exception.wrapper.InputFieldException;
import com.batman.exception.wrapper.NoDiscountAvailableException;
import com.batman.mapper.CommonMapper;
import com.batman.repository.IDiscountRepository;
import com.batman.service.IDiscountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class DiscountService implements IDiscountService {

	private final IDiscountRepository discountRepository;

	private final CommonMapper mapper;

	private final ApplicationEventPublisher applicationEventPublisher;

	private final KafkaTemplate<String, DiscountPlacedEvent> kafkaTemplate;

	@Override
	public Discount createDiscount(DiscountRequest discountRequest, BindingResult bindingResult) {
		log.info("Entering Create Discount ...");
		if (bindingResult.hasErrors()) {
		    log.error("Input Field Invalid Error ...");
		    FieldError fieldError = bindingResult.getFieldError();
		    String errorMessage = (fieldError != null) ? fieldError.getDefaultMessage() : "Invalid input field";
		    throw new InputFieldException(errorMessage);
		}

		List<Discount> list = discountRepository.findActiveDiscountsOfGames(discountRequest.getGameIds(),false);
		if (!list.isEmpty()) {
			throw new DiscountAlreadyExistException(
					"Already A Discount Exists For one of this Ids ::" + discountRequest.getGameIds());
		}
		Discount discount = mapper.convertToEntity(discountRequest, Discount.class);
		discount.setIsExpired(false);
		discount.setDiscountStatus(DiscountStatus.SCHEDULED);
		Discount savedDiscount = discountRepository.save(discount);
		log.info("Discount Added with ID :: {} ...", savedDiscount.getDiscountId());
		log.info("Publishing Discount Created Event ... ");

		DiscountCreatedEvent discountCreatedEvent = new DiscountCreatedEvent(this, savedDiscount);
		applicationEventPublisher.publishEvent(discountCreatedEvent);
		log.info("Leaving Create Discount ...");

		return savedDiscount;
	}

	@Override
	public List<Discount> getDiscountByGameIds(Set<Integer> gameIds) {
		log.info("Discount Service Fetching Game Discounts For :::" + gameIds);
		return discountRepository.findDiscountsOfGames(gameIds);

	}

	@Override
	public Discount activateDiscount(Integer discountId) {

		log.info("Sending Discount Added Event ... ");
		Discount discount = discountRepository.findById(discountId)
				.orElseThrow(() -> new NoDiscountAvailableException("DISCOUNT_NOT_FOUND"));
		discount.setDiscountStatus(DiscountStatus.ACTIVE);
		DiscountPlacedEvent discountPlacedEvent = DiscountPlacedEvent.builder().gameIds(discount.getGameIds())
				.discountValue(discount.getDiscountValue()).build();
		CompletableFuture<SendResult<String, DiscountPlacedEvent>> future = kafkaTemplate.send(KafkaConstants.TOPIC,
				discountPlacedEvent);
		future.handle((result, throwable) -> {
			if (throwable == null) {
				log.info("SuccessFull Response Received....");
				return result;
			} else {
				log.error("Failed to Update Game Discount....");
				throw new CompletionException(new FailedToUpdateGameServiceException(
						throwable.getMessage() + " Response Arrived From Game Service"));
			}
		});
		return discountRepository.save(discount) ;
	}

	@Override
	public Discount deActivateDiscount(Integer discountId) {
		
		log.info("Sending Remove Discount  Event ... ");
		Discount discount = discountRepository.findById(discountId)
				.orElseThrow(() -> new NoDiscountAvailableException("DISCOUNT_NOT_FOUND"));
		discount.setDiscountStatus(DiscountStatus.EXPIRED);
		discount.setIsExpired(true);
		DiscountPlacedEvent discountRemoveEvent = DiscountPlacedEvent.builder().gameIds(discount.getGameIds())
				.discountValue(0.0).build();
		CompletableFuture<SendResult<String, DiscountPlacedEvent>> future = kafkaTemplate.send(KafkaConstants.TOPIC,
				discountRemoveEvent);
		future.handle((result, throwable) -> {
			if (throwable == null) {
				log.info("SuccessFull Response Received....");
				return result;
			} else {
				log.error("Failed to Update Game Discount....");
				throw new CompletionException(new FailedToUpdateGameServiceException(
						throwable.getMessage() + " Response Arrived From Game Service"));
			}
		});
		return discountRepository.save(discount) ;
	}

	@Override
	public Discount expireDiscount(Integer discountId) {
		log.info("Entering expireDiscount ... ");
		Discount discount = discountRepository.findById(discountId)
				.orElseThrow(() -> new NoDiscountAvailableException("DISCOUNT_NOT_FOUND"));
		discount.setIsExpired(true);
		discount.setDiscountStatus(DiscountStatus.EXPIRED);
		log.info("Leaving expireDiscount ... ");
		return discountRepository.save(discount);
	}

}
