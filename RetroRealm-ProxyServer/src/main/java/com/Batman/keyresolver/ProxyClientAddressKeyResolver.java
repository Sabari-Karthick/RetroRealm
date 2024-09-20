package com.Batman.keyresolver;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.support.ipresolver.XForwardedRemoteAddressResolver;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
/**
 * 
 * This Class is Used to extract the IP Address of the request Sender
 * 
 * 
 * 
 */
@Primary
@Component(value = "proxyKeyResolver")
@Slf4j
public class ProxyClientAddressKeyResolver implements KeyResolver {
	
	
	@Override
	public Mono<String> resolve(ServerWebExchange exchange) {
		log.info("Entering Proxy Key Resolver");
		XForwardedRemoteAddressResolver addressResolver = XForwardedRemoteAddressResolver.maxTrustedIndex(2);
		InetSocketAddress inetSocketAddress = addressResolver.resolve(exchange);
		InetAddress inetAddress = inetSocketAddress.getAddress();
		String hostAddress = inetAddress.getHostAddress();
		log.info("Host Address {}",hostAddress);
		log.info("Leaving Proxy Key Resolver");
		return Mono.just(hostAddress);
	}

}
