package com.batman.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.batman.criteria.FilterComponent;
import com.batman.criteria.QueryCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@Slf4j
public class BaseHelper {

    private BaseHelper() {
    }

    public static List<FilterComponent> getQueryConditionsFromFilters(Map<String, Object> filters) {
        log.info("Entering BaseHelper getQueryConditionsFromFilters ...");
		List<FilterComponent> queryConditions = new ArrayList<>();
		if(CollectionUtils.isEmpty(filters)){
			log.info("No Filters Provided...");
		}else{
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

}
