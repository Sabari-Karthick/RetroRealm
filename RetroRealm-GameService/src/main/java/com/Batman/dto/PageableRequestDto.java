package com.Batman.dto;

import lombok.Data;

@Data
public class PageableRequestDto {
	Integer pageNumber = 0;
	Integer pageSize = 5;
	Boolean asc = true;
	String property = "gamePrice";
}
