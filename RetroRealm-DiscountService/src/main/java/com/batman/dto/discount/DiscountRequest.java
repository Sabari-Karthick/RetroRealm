package com.batman.dto.discount;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import com.batman.enums.DiscountType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DiscountRequest {

	private DiscountType discountType;

	@NotEmpty(message = "Game Ids cannot be Empty")
	private Set<Integer> gameIds;
	
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	@FutureOrPresent
	private LocalDate fromDate;

	@JsonSerialize(using = LocalTimeSerializer.class)
	@JsonFormat(pattern = "HH:mm:ss")
	@Future
	private LocalTime fromTime;

	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	@FutureOrPresent
	private LocalDate toDate;

	@JsonSerialize(using = LocalTimeSerializer.class)
	@JsonFormat(pattern = "HH:mm:ss")
	@Future
	private LocalTime toTime;

	@NotNull(message = "Discount Value must be provided")
	private Double discountValue;
}
