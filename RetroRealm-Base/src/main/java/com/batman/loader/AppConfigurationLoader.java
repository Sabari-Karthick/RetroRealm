package com.batman.loader;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class AppConfigurationLoader extends MapConfigLoader<String, Object>{
	
	
	private Map<String, Object> configMap;

	@Override
	public Map<String, Object> loadMap() {
		log.info("Entering loadMap in AppConfigurationLoader ...");
		this.configMap = new HashMap<>();
		return this.configMap;
	}



}
