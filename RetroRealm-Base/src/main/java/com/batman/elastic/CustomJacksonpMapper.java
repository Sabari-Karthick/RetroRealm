package com.batman.elastic;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.elastic.clients.json.jackson.JacksonJsonpMapper;

public class CustomJacksonpMapper extends JacksonJsonpMapper{

	public CustomJacksonpMapper(ObjectMapper mapper) {
		super(mapper);
	}

}
