package com.Batman.configuration.kafka;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.Batman.enums.OrderType;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.internals.StickyPartitionCache;
import org.apache.kafka.common.Cluster;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.PartitionInfo;

@Slf4j
public class CustomOrderPartitioner implements Partitioner {

	private final StickyPartitionCache stickyPartitionCache = new StickyPartitionCache();

	@Override
	public void configure(Map<String, ?> configs) {
		// No Implementation
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		log.info("Entering CustomOrderPartitioner partition ....");
		int lastPartition = getLastPartition(topic, cluster);
		if (!Objects.isNull(keyBytes) && key instanceof String stringKey && stringKey.contains(OrderType.EVENT.getType())) {
			log.info("Event Order, Routing to Event Partition :: {}",lastPartition);
			return lastPartition;
		}
		log.info("Not a Event Partition Using Sticky Partition");
		return stickyPartitionCache.partition(topic, cluster);
	}


	private int getLastPartition(String topic, Cluster cluster) {
		List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
		if (partitions == null || partitions.isEmpty()) {
			log.error("No partitions found for topic: {}", topic);
			throw new RuntimeException("No partitions found for topic: " + topic);
		}
		int lastPartition = partitions.get(partitions.size() - 1).partition();
		log.info("Last partition for topic {} is {}", topic, lastPartition);
		return lastPartition;
	}

	@Override
	public void close() {
          // No Implementation
	}

}
