package com.Batman.configuration.kafka;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoadBalancingPartitioner implements Partitioner {

    // Stores approximate load per partition (can be enhanced using monitoring)
    private static final ConcurrentHashMap<Integer, Integer> partitionLoadMap = new ConcurrentHashMap<>();

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, 
                         byte[] valueBytes, Cluster cluster) {
        int numPartitions = cluster.partitionCountForTopic(topic);

        // Initialize load if not already set
        for (int i = 0; i < numPartitions; i++) {
            partitionLoadMap.putIfAbsent(i, 0);
        }

        // Find the partition with the least load
        int minLoadPartition = partitionLoadMap.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .get()
                .getKey();

        // Simulate increasing load (In reality, use a monitoring tool like Prometheus)
        partitionLoadMap.put(minLoadPartition, partitionLoadMap.get(minLoadPartition) + 1);

        return minLoadPartition;
    }

    @Override
    public void close() {}

    @Override
    public void configure(Map<String, ?> configs) {}
}
