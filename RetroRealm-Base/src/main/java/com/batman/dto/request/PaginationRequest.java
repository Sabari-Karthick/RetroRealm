package com.batman.dto.request;

import java.util.Map;

import lombok.Data;

@Data
public class PaginationRequest {

	private int start;
	private int size;
	private boolean asc;
	private String property;
	private Map<String, Object> filters;
	
}
