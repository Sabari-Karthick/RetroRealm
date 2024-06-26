package com.batman.service;

import org.springframework.validation.BindingResult;

import com.batman.dto.discount.DiscountRequest;
import com.batman.entity.Discount;

public interface IDiscountService {
    Discount createDiscount(DiscountRequest discountRequest,BindingResult bindingResult);
}
