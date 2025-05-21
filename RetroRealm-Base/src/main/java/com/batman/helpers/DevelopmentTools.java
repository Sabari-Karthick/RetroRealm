package com.batman.helpers;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.batman.exception.wrapper.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public final class DevelopmentTools {

    private DevelopmentTools() {
    }


    /**
     * Creates an Elasticsearch index using the provided client, mapping file, and index name.
     * This method is intended for development/testing purposes.
     *
     * @param esClient  The ElasticsearchClient instance to use for creating the index.
     * @param fileName  The name of the JSON mapping file (without .json extension) located in /ES-Mapping/ classpath folder.
     * @param indexName The name of the index to be created.
     * @return true if the index was successfully created and acknowledged, false otherwise.
     * @throws IllegalArgumentException if fileName is empty.
     * @throws InternalException        if there's an error reading the mapping file or communicating with Elasticsearch.
     */
    public static boolean createIndex(ElasticsearchClient esClient, String fileName, String indexName) {
        log.info("Entering DevelopmentTools createIndex for index: {}", indexName);
        if (esClient == null) {
            log.error("ElasticsearchClient cannot be null.");
            throw new IllegalArgumentException("ELASTICSEARCH_CLIENT_IS_NULL");
        }
        if (StringUtils.isEmpty(fileName)) {
            log.error("File name for index mapping cannot be empty.");
            throw new IllegalArgumentException("FILE_NAME_IS_EMPTY");
        }
        if (StringUtils.isEmpty(indexName)) {
            log.error("Index name cannot be empty.");
            throw new IllegalArgumentException("INDEX_NAME_IS_EMPTY");
        }

        String filePath = "/ES-Mapping/" + fileName + ".json";
        ClassPathResource classPathResource = new ClassPathResource(filePath);

        if (!classPathResource.exists()) {
            log.error("Mapping file not found at path: {}", filePath);
            throw new InternalException("MAPPING_FILE_NOT_FOUND: " + filePath);
        }

        try (InputStream inputStream = classPathResource.getInputStream()) {
            CreateIndexRequest createIndexRequest = CreateIndexRequest.of(i -> i
                    .index(indexName)
                    .withJson(inputStream));

            log.info("Attempting to create index '{}' with mapping from '{}'", indexName, filePath);
            CreateIndexResponse response = esClient.indices().create(createIndexRequest);

            if (response.acknowledged()) {
                log.info("Successfully created and acknowledged index: {}", indexName);
                log.info("Leaving DevelopmentTools createIndex successfully.");
                return true;
            } else {
                log.warn("Index creation for '{}' was not acknowledged.", indexName);
                log.info("Leaving DevelopmentTools createIndex with unacknowledged creation.");
                return false;
            }
        } catch (IOException e) {
            log.error("IOException while reading mapping file {} or creating index {}: {}", filePath, indexName, ExceptionUtils.getStackTrace(e));
            throw new InternalException("Error during index creation: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Exception while creating index {}: {}", indexName, ExceptionUtils.getStackTrace(e));
            throw new InternalException("Error during index creation: " + e.getMessage(), e);
        }
    }

}
