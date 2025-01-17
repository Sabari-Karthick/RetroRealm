package com.batman.loader;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class AppConfigurationLoader extends MapConfigLoader<String, Object>{
	
	
	@Value("${is-local:false}")
	private boolean isLocal;
	
	private Map<String, Object> configMap;

	@Override
	public Map<String, Object> loadMap() {
		log.info("Entering loadMap in AppConfigurationLoader ...");
		this.configMap = new HashMap<>();
		log.info("Leaving loadMap in AppConfigurationLoader ...");
		return this.configMap;
	}



}
