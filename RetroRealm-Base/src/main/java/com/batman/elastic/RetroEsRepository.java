package com.batman.elastic;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.batman.criteria.Sort;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.batman.criteria.FilterComponent;
import com.batman.exception.InternalException;
import com.batman.model.BaseModel;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RetroEsRepository<T extends BaseModel> implements IRetroESBaseRepository<T> {

    private final ElasticSearchClientConfiguration elasticSearchClientConfiguration;

    @Override
    public List<T> findAll(List<FilterComponent> filterComponents,Sort sort, int start, int size, Class<T> clazz) {
        log.info("Entering ElasticSearchRepository find All ...");
        SearchRequest searchRequest = ElasticSearchUtil.buildSearchRequest(filterComponents,sort, start, size, clazz);
        try {
            SearchResponse<T> searchResponse = elasticSearchClientConfiguration.getElasticsearchClient()
                    .search(searchRequest, clazz);
            List<Hit<T>> hits = searchResponse.hits().hits();
            log.info("Hit Count :: {}", hits.size());
            return hits.stream().map(Hit::source).toList();
        } catch (ElasticsearchException e) {
            log.error("Elastic Search Exception in RetroEsRepository findAll :: {}", e.getMessage());
            handleElasticSearchException(e);
        } catch (IOException e) {
            log.error("IO-Exception in RetroEsRepository findAll :: {}", e.getMessage());
            throw new InternalException(e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public Page<T> getAllByPage(List<FilterComponent> filterComponents, Sort sort, int start, int size, Class<T> clazz) {
        log.info("Entering ElasticSearchRepository getAppByPage ...");
        List<T> results = findAll(filterComponents,sort, start, size, clazz);
        log.info("Result Count :: {}",results.size());
        Page<T> page = new PageImpl<>(results, PageRequest.of(start, size), results.size());
        log.info("Exiting ElasticSearchRepository getAppByPage ...");
        return page;
    }

    @Override
    public T save(T model) {
        log.info("Entering ElasticSearchRepository save ...");
        String indexName = ElasticSearchUtil.getIndexName(model.getClass());
        indexDocument(indexName, model);
        log.info("Exiting ElasticSearchRepository save ...");
        return model;
    }

    private void indexDocument(String indexName, T model) {
        if (StringUtils.isBlank(indexName)) {
            log.error("Index Name is Empty Can't Index Document");
            return;
        }
        int retryCount = 5;
        while (retryCount > 0) {
            try {
                IndexResponse index = elasticSearchClientConfiguration.getElasticsearchClient()
                        .index(i -> i.index(indexName).document(model).id(String.valueOf(model.getPrimaryKeyValue()))
                                .refresh(Refresh.WaitFor));
                log.info("Index Response :: {}", index.result());
                return;
            } catch (IOException | ElasticsearchException e) {
                log.error("Elastic Search Exception in indexDocument :: {}", e.getMessage());
                retryCount--;
                if (retryCount == 0) {
                    throw new InternalException(e.getMessage());
                }
            }
        }

    }

    @Override
    public List<T> saveBatch(List<T> models) {
        return List.of();
    }



    private void handleElasticSearchException(ElasticsearchException e) {
        if (e.status() == 404) {
            log.error("Index Not Found :: {}", e.getMessage());
            throw new InternalException("Index Not Found");
        } else if (e.status() == 400) {
            log.error("Bad Request :: {}", e.getMessage());
            throw new InternalException("Bad Request");
        } else {
            log.error("Internal Server Error :: {}", e.getMessage());
            throw new InternalException("Internal Server Error");
        }

    }

}
