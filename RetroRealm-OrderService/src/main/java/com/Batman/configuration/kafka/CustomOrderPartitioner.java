package com.Batman.configuration.kafka;

import java.util.Map;
import java.util.Objects;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.internals.StickyPartitionCache;
import org.apache.kafka.common.Cluster;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomOrderPartitioner implements Partitioner {

	private final StickyPartitionCache stickyPartitionCache = new StickyPartitionCache();

	private static final int EVENT_PARTITION = 0;

	@Override
	public void configure(Map<String, ?> configs) {
		// No Implementation
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		log.info("Entering CustomOrderPartitioner partition ....");
		if (!Objects.isNull(keyBytes) && key instanceof String stringKey && stringKey.contains("FREE_EVENT")) {
			log.info("Event Order, Routing to Event Partition :: {}",EVENT_PARTITION);
			return EVENT_PARTITION;
		}
		log.info("Not a Event Partition Using Sticky Partition");
		return stickyPartitionCache.partition(topic, cluster);
	}

	@Override
	public void close() {
          // No Implementation
	}

}
