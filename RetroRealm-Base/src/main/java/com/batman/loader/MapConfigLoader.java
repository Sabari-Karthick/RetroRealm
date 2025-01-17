package com.batman.loader;

import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.batman.exception.InternalException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class MapConfigLoader<K, V> implements IConfigLoader<K, V> {

	private Map<K, V> configMap;

	protected boolean isLoading = false;

	@Override
	public final void load() {
		log.info("Entering load Configuration Map ...");
		synchronized (this) { 
			try {
				configMap = loadMap();
			} catch (Exception e) {
				log.error("Error While Load Configuration ...");
				throw new InternalException("ERROR_LOADING_CONFIGURATION", e);
			}
			isLoading = false;
		}
	}

	@Override
	public V get(K key) {
		if (isLoading) {
			try {
				Thread.sleep(100);
				return get(key);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.error("Interrupted Exception While Fetching Config for Key :: {}", key);
			} catch (Exception e) {
				log.error("Error While Fetching Config for Key :: {}", key);
				return get(key);
			}
		}
		if (CollectionUtils.isEmpty(configMap)) {
			load();
		}

		return configMap.get(key);
	}

	public abstract Map<K, V> loadMap();

}
