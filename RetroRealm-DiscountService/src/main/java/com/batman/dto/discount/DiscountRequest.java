package com.batman.dto.discount;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import com.batman.enums.DiscountType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DiscountRequest {

	private DiscountType discountType;

	@NotEmpty(message = "Game Ids cannot be Empty")
	private Set<String> gameIds;
	
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	@FutureOrPresent(message = "Discount From Date is not a future Value")
	@NotNull(message = "Discount From Date cannot be null or empty")
	private LocalDate fromDate;

	@NotNull(message = "Discount From Time cannot be null or empty")
	@JsonSerialize(using = LocalTimeSerializer.class)
	@JsonFormat(pattern = "HH:mm:ss")
//	@FutureTime(message = "Discount From Time is not a future Value")
	private LocalTime fromTime;

	@NotNull(message = "Discount To Date cannot be null or empty")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	@FutureOrPresent(message = "Discount To Date is not a future Value")
	private LocalDate toDate;

	@NotNull(message = "Discount To Time cannot be null or empty")
	@JsonSerialize(using = LocalTimeSerializer.class)
	@JsonFormat(pattern = "HH:mm:ss")
//	@FutureTime(message = "Discount To Time is not a future Value")
	private LocalTime toTime;

	@NotNull(message = "Discount Value must be provided")
	private Double discountValue;
}
