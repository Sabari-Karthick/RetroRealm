package com.batman.elastic;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.batman.criteria.FilterComponent;
import com.batman.exception.InternalException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.batman.model.BaseModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RetroEsRepository<T extends BaseModel> implements IRetroESBaseRepository<T> {

    private final ElasticSearchClientConfiguration elasticSearchClientConfiguration;

    @Override
    public List<T> findAll(List<FilterComponent> filterComponents) {
        log.info("Entering ElasticSearchRepository find All ...");

        return Collections.emptyList();
    }

    @Override
    public T save(T model) {
        log.info("Entering ElasticSearchRepository save ...");
        String indexName = getIndexName(model);
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
                IndexResponse index = elasticSearchClientConfiguration.getElasticsearchClient().index(i -> i.index(indexName).document(model).id("").refresh(Refresh.WaitFor));
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

    private String getIndexName(T model) {
        try {
            BaseModel baseModel = model.getClass().getConstructor().newInstance();
            return baseModel.getIndexName();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            log.error("Elastic Search Exception in getIndexName :: {}", e.getMessage());
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public List<T> saveBatch(List<T> models) {
        return List.of();
    }

}
;