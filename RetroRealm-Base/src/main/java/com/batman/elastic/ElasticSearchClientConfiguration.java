package com.batman.elastic;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.stereotype.Component;

import com.batman.exception.wrapper.InternalException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import nl.altindag.ssl.SSLFactory;

@Component
@Slf4j
@Getter
@RequiredArgsConstructor
public class ElasticSearchClientConfiguration {

	private RestClient restClient;
	private ElasticsearchClient elasticsearchClient;
	private final ObjectMapper mapper;

	@PostConstruct
	private void initSearchClient() {
		log.info("Initializing Elasticsearch Client...");

		try {

			String host = "localhost";
			int port = 9200;
			String scheme = "http";

			String user = "elastic";
			String pwd = "1234";

			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, pwd));

			RestClientBuilder builder = RestClient.builder(new HttpHost(host, port, scheme));
//                .setHttpClientConfigCallback(httpClientBuilder -> {
//                    SSLFactory sslFactory = SSLFactory.builder()
//                        .withUnsafeTrustMaterial()
//                        .withUnsafeHostnameVerifier()
//                        .build();
//                    return httpClientBuilder
//                        .setSSLContext(sslFactory.getSslContext())
//                        .setSSLHostnameVerifier(sslFactory.getHostnameVerifier())
//                        .disableAuthCaching()
//                        .setDefaultCredentialsProvider(credentialsProvider);
//                });

			restClient = builder.build();
			ElasticsearchTransport transport = new RestClientTransport(restClient, new CustomJacksonJsonpMapper(mapper));
			elasticsearchClient = new ElasticsearchClient(transport);

			log.info("Elasticsearch Client initialized successfully.");
		} catch (Exception e) {
			log.error("Failed to initialize Elasticsearch Client {}", ExceptionUtils.getStackTrace(e));
			throw new InternalException("Error initializing Elasticsearch Client", e);
		}
	}

	@PreDestroy
	public void closeClient() {
		try {
			if (restClient != null) {
				restClient.close();
				log.info("Elasticsearch Client closed.");
			}
		} catch (Exception e) {
			log.error("Error closing Elasticsearch Client {}", ExceptionUtils.getStackTrace(e));
		}
	}

}
