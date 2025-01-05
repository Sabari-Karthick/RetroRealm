package com.Batman.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommonMapper {
	
	private final ModelMapper mapper;
	
	public <T, S> S convertToEntity(T data, Class<S> type) {
        return mapper.map(data, type);
    }

    public <T, S> S convertToResponse(T data, Class<S> type) {
        return mapper.map(data, type);
    }

   


}
