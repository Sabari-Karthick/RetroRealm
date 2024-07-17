package com.batman.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.batman.dto.discount.DiscountRequest;
import com.batman.entity.Discount;
import com.batman.exception.wrapper.InputFieldException;
import com.batman.mapper.CommonMapper;
import com.batman.repository.IDiscountRepository;
import com.batman.service.IDiscountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class DiscountServiceImpl implements IDiscountService{

	
	private final IDiscountRepository discountRepository;
	
	private final CommonMapper mapper;
	
	
	@Override
	public Discount createDiscount(DiscountRequest discountRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InputFieldException(bindingResult.getFieldError().getDefaultMessage());
		}
		Discount discount = mapper.convertToEntity(discountRequest, Discount.class);
		discount.setIsExpired(false);
		return discountRepository.save(discount);
	}


	@Override
	public List<Discount> getDiscountByGameIds(Set<Integer> gameIds) {
		log.info("Discount Service Fetching Game Discounts For :::" + gameIds);
		return discountRepository.findAllByGameIds(gameIds);
		
	}

}
