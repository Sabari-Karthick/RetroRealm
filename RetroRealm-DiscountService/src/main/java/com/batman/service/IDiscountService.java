package com.batman.service;

import java.util.List;
import java.util.Set;

import org.springframework.validation.BindingResult;

import com.batman.dto.discount.DiscountRequest;
import com.batman.entity.Discount;

public interface IDiscountService {
    Discount createDiscount(DiscountRequest discountRequest,BindingResult bindingResult);
    List<Discount> getDiscountByGameIds(Set<String> gameIds);
    Discount activateDiscount(Integer discountId);
    Discount deActivateDiscount(Integer discountId);
    Discount expireDiscount(Integer discountId);
}

