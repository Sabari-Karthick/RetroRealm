package com.batman.service.impl;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.batman.dto.discount.DiscountRequest;
import com.batman.entity.Discount;
import com.batman.exception.wrapper.InputFieldException;
import com.batman.exception.wrapper.NoDiscountAvailableException;
import com.batman.mapper.CommonMapper;
import com.batman.repository.IDiscountRepository;
import com.batman.service.IDiscountService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
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
	public Discount getDiscountByGameIds(Set<Integer> gameIds) {
		return discountRepository.findByGameIds(gameIds).orElseThrow(()->  new NoDiscountAvailableException("NO_DISCOUNT_AVAILABLE")); 
		
	}

}
