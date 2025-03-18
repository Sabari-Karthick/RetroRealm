package com.Batman.service;

import com.Batman.entity.Payment;
import com.Batman.enums.PaymentType;

public interface IPaymentService {
     Payment pay(Double amount,PaymentType  paymentType,String orderId);
}
