package com.batman.elastic;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.elastic.clients.json.jackson.JacksonJsonpMapper;

public class CustomJacksonJsonpMapper extends JacksonJsonpMapper{

	public CustomJacksonJsonpMapper(ObjectMapper mapper) {
		super(mapper);
	}

}
