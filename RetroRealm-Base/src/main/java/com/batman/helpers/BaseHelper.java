package com.batman.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.batman.criteria.FilterComponent;
import com.batman.criteria.QueryCondition;
import com.batman.criteria.Sort;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.SortDirection;
import org.springframework.util.CollectionUtils;

@Slf4j
public final class BaseHelper {

    private BaseHelper() {
    }

    public static List<FilterComponent> getQueryConditionsFromFilters(Map<String, Object> filters) {
        log.info("Entering BaseHelper getQueryConditionsFromFilters ...");
        List<FilterComponent> queryConditions = new ArrayList<>();
        if (CollectionUtils.isEmpty(filters)) {
            log.info("No Filters Provided...");
        } else {
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String fieldName = entry.getKey();
                Object value = entry.getValue();
                QueryCondition queryCondition = new QueryCondition(fieldName, value);
                queryConditions.add(queryCondition);
            }
        }
        log.debug("Query Condition Count :: {}", queryConditions.size());
        log.info("Exiting BaseHelper getQueryConditionsFromFilters ...");
        return queryConditions;
    }

    public static Sort buildSort(String sortField, boolean asc) {
        log.info("Entering BaseHelper buildSort ...");
        Sort sort = new Sort();
        sort.setField(sortField);
        sort.setDirection(asc ? SortDirection.ASCENDING : SortDirection.DESCENDING);
        log.info("Exiting BaseHelper buildSort ...");
        return sort;
    }

    public static <T> Set<T> convertToSet(T[] elements) {
        if (elements == null || elements.length == 0) {
            return Collections.emptySet();
        }
        return new HashSet<>(Arrays.asList(elements));
    }



}
