package com.batman.resolvers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import com.batman.exception.InternalException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiscoveryClinetUrlResolver implements ServiceUrlResolver {

	private final DiscoveryClient discoveryClient;

	@Override
	public Map<String, String> resolveServiceUrls() {
		log.info("Entering Discovery Client Url Resolver ...");
		if (Objects.isNull(discoveryClient)) {
			log.error("No instance of Discovery Client Found ...");
			throw new InternalException("Discovery Client Not Found");
		}
		List<String> services = discoveryClient.getServices();
		log.info("Number of Services Register :: {}", services.size());
		Map<String, String> serviceUrlMap = new HashMap<>();
		for (String service : services) {
			serviceUrlMap.put(service, resolveUrlFromService(service));
		}
		log.info("Leaving Discovery Client Url Resolver ...");
		return serviceUrlMap;
	}

	private String resolveUrlFromService(String serviceName) {
		List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
		if (instances.isEmpty()) {
			log.debug("No Instance of {} is Found ....", serviceName);
			return "";
		}
		ServiceInstance serviceInstance = instances.get(0);
		String scheme = serviceInstance.getScheme();
		String host = serviceInstance.getHost();
		int port = serviceInstance.getPort();
		return scheme + "://" + host + ":" + port;
	}

}
