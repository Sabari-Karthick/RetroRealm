package com.batman.loader;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class MapConfigLoader<K, V> implements IConfigLoader<K, V> {
	
	private Map<K, V> configMap;
	

	@Override
	public final void load() {
        log.info("Entering load Configuration Map ...");
        synchronized (this.configMap) {
			loadMap();
		}
	}

	@Override
	public V get(K key) {
		return null;
	}

	public abstract Map<K, V> loadMap();

}
