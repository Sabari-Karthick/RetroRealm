package com.Batman.dto;

import lombok.Data;

import java.util.Map;

@Data
public class PageableRequest {
	int pageNumber = 0;
	int pageSize = 5;
	boolean asc = true;
	String sortField = "gamePrice";
	Map<String,Object> filters;
}
