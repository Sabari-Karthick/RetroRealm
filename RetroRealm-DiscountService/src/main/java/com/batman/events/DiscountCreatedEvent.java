package com.batman.events;

import org.springframework.context.ApplicationEvent;

import com.batman.entity.Discount;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscountCreatedEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private transient Discount discount;

	public DiscountCreatedEvent(Object source, Discount discount) {
		super(source);
		this.discount = discount;
	}

}
