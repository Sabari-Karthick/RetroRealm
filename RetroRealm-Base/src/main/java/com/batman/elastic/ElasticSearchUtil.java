package com.batman.elastic;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.util.ObjectBuilder;
import com.batman.constants.DefaultBaseConstants;
import com.batman.criteria.*;
import com.batman.exception.InternalException;
import com.batman.model.BaseModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
public final class ElasticSearchUtil {

    private ElasticSearchUtil() {
    }

    public static String getIndexName(Class<? extends BaseModel> clazz) {
        try {
            BaseModel baseModel = clazz.getConstructor().newInstance();
            return baseModel.getIndexName();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            log.error("Elastic Search Exception in getIndexName :: {}", e.getMessage());
            throw new InternalException(e.getMessage());
        }
    }

    public static String getTypeForModel(Class<? extends BaseModel> clazz) {
        try {
            BaseModel baseModel = clazz.getConstructor().newInstance();
            return baseModel.getType();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            log.error("Elastic Search Exception in getTypeForModel :: {}", e.getMessage());
            throw new InternalException(e.getMessage());
        }
    }

    public static SearchRequest buildSearchRequest(List<FilterComponent> filterComponents, int start, int size, Class<? extends BaseModel> clazz) {
        log.info("Entering ElasticSearchUtil buildSearchRequest ...");
        String indexName = getIndexName(clazz);
        String type = getTypeForModel(clazz);
        if (StringUtils.isNotBlank(type)) {
            filterComponents.add(new QueryCondition(DefaultBaseConstants.TYPE, type));
        }

        List<EsFilterGroup> queries = convertFilterComponentsToEsFilterGroups(filterComponents);

        SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder().size(size).from(start).index(indexName);
        if(!CollectionUtils.isEmpty(queries)){
            searchRequestBuilder.query(buildQuery(queries));
        }
        log.info("Exiting ElasticSearchUtil buildSearchRequest ...");
        return searchRequestBuilder.build();

    }

    private static Function<Query.Builder, ObjectBuilder<Query>> buildQuery(List<EsFilterGroup> esFilterGroups) {
        List<Query> allQueries = esFilterGroups.stream()
                .flatMap(group -> group.getQueries().stream())
                .toList();
        return q -> {
            BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
            if (!allQueries.isEmpty()) {
                boolQueryBuilder.must(allQueries);
            }
            return q.bool(boolQueryBuilder.build());
        };
    }


    private static List<EsFilterGroup> convertFilterComponentsToEsFilterGroups(List<FilterComponent> filterComponents) {
        log.info("Entering ElasticSearchUtil convertFilterComponentsToEsFilterGroups ...");
        List<EsFilterGroup> esFilterGroups = new ArrayList<>();
        if (CollectionUtils.isEmpty(filterComponents)) {
            log.debug("No Filter Components Found ... Returning Empty List");
        } else {
            for (FilterComponent filterComponent : filterComponents) {
                EsFilterGroup esFilterGroup = new EsFilterGroup();
                if (filterComponent instanceof QueryCondition queryCondition) {
                    esFilterGroup = getEsFilterGroupFromQueryCondition(queryCondition);
                } else if (filterComponent instanceof FilterGroup filterGroup) {
                    esFilterGroup = getEsFilterGroupFromFilterGroup(filterGroup);
                }
                esFilterGroups.add(esFilterGroup);
            }
        }
        log.info("Exiting ElasticSearchUtil convertFilterComponentsToEsFilterGroups ...");
        return esFilterGroups;
    }

    private static EsFilterGroup getEsFilterGroupFromFilterGroup(FilterGroup filterGroup) {
        log.info("Entering ElasticSearchUtil getEsFilterGroupFromFilterGroup ...");
        List<Query> queries = new ArrayList<>();
        QueryCondition queryCondition1 = filterGroup.getQueryCondition1();
        QueryCondition queryCondition2 = filterGroup.getQueryCondition2();
        LogicalOperator logicalOperator = filterGroup.getLogicalOperator();
        Query esQueryForQueryCondition = getESQueryForQueryCondition(queryCondition1);
        Query esQueryForQueryCondition2 = getESQueryForQueryCondition(queryCondition2);
        Query esFilterGroupFromQueryCondition = getEsFilterGroupFromQueryConditions(esQueryForQueryCondition, esQueryForQueryCondition2, logicalOperator);
        queries.add(esFilterGroupFromQueryCondition);
        EsFilterGroup esFilterGroup = new EsFilterGroup();
        esFilterGroup.setQueries(queries);
        log.info("Exiting ElasticSearchUtil getEsFilterGroupFromFilterGroup ...");
        return esFilterGroup;
    }

    private static Query getEsFilterGroupFromQueryConditions(Query esQueryForQueryCondition, Query esQueryForQueryCondition2, LogicalOperator logicalOperator) {
        log.info("Entering ElasticSearchUtil getEsFilterGroupFromQueryConditions ...");
        QueryVariant queryVariant = switch (logicalOperator) {
            case AND -> BoolQuery.of(b -> b.must(esQueryForQueryCondition, esQueryForQueryCondition2));
            case OR -> BoolQuery.of(b -> b.should(esQueryForQueryCondition, esQueryForQueryCondition2));
            default -> throw new InternalException("Invalid Logical Operator");
        };
        log.info("Exiting ElasticSearchUtil getEsFilterGroupFromQueryConditions ...");
        return queryVariant._toQuery();
    }


    private static EsFilterGroup getEsFilterGroupFromQueryCondition(QueryCondition queryCondition) {
        log.info("Entering ElasticSearchUtil getEsFilterGroupFromQueryCondition ...");
        EsFilterGroup esFilterGroup = new EsFilterGroup();
        List<Query> queryList = new ArrayList<>();
        Query query = getESQueryForQueryCondition(queryCondition);
        queryList.add(query);
        esFilterGroup.setQueries(queryList);
        log.info("Exiting ElasticSearchUtil getEsFilterGroupFromQueryCondition ...");
        return esFilterGroup;
    }


    private static Query getESQueryForQueryCondition(@NonNull QueryCondition queryCondition) {
        log.info("Entering ElasticSearchUtil getESQueryForQueryCondition ...");
        String field = queryCondition.getField();
        Object value = queryCondition.getValue();
        QueryOperator queryOperator = queryCondition.getQueryOperator();
        log.debug("Field :: {} and Value :: {} and Query Operator :: {}", field, value, queryOperator);
        QueryVariant queryVariant = switch (queryOperator) {
            case EQUAL -> MatchQuery.of(m -> m.field(field).query(FieldValue.of(value)));
            case NOT_EQUAL ->
                    BoolQuery.of(b -> b.mustNot(q -> q.match(m -> m.field(field).query(FieldValue.of(value)))));
            case GREATER_THAN -> RangeQuery.of(r -> r.untyped(u -> u.field(field).gt(FieldValue.of(value).anyValue())));
            case LESS_THAN -> RangeQuery.of(r -> r.untyped(u -> u.field(field).lt(FieldValue.of(value).anyValue())));
            case GREATER_THAN_OR_EQUAL ->
                    RangeQuery.of(r -> r.untyped(u -> u.field(field).gte(FieldValue.of(value).anyValue())));
            case LESS_THAN_OR_EQUAL ->
                    RangeQuery.of(r -> r.untyped(u -> u.field(field).lte(FieldValue.of(value).anyValue())));
            case CONTAINS -> WildcardQuery.of(w -> w.field(field).wildcard("*" + value + "*"));
        };

        log.info("Exiting ElasticSearchUtil getESQueryForQueryCondition ...");
        return queryVariant._toQuery();
    }


}
