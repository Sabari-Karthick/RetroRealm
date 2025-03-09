package com.batman.dto.request;

import lombok.Data;

@Data
public class PaginationRequest {

	private int start;
	private int size;
	private boolean asc;
	private String property;
	
}
