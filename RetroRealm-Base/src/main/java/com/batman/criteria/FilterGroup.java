package com.batman.criteria;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

/**
 * FilterGroup represents a group of filter conditions combined with a logical operator.
 * It allows for creating complex filtering criteria by grouping multiple conditions together.
 */

@Getter
@Setter
public class FilterGroup implements FilterComponent {
    private QueryCondition queryCondition1;
    private QueryCondition queryCondition2;
    private LogicalOperator logicalOperator = LogicalOperator.AND;

    /**
     * Creates a new FilterGroup with the specified query conditions and logical operator.
     *
     * @param queryCondition1 The first query condition. Must not be null.
     * @param queryCondition2 The second query condition. Must not be null.
     * @param logicalOperator The logical operator to combine the conditions. Must not be null.
     * @throws IllegalArgumentException if any of the parameters are null.
     */
    public FilterGroup(QueryCondition queryCondition1, QueryCondition queryCondition2, LogicalOperator logicalOperator) {
        Assert.notNull(queryCondition1, "queryCondition1 cannot be null");
        Assert.notNull(queryCondition2, "queryCondition2 cannot be null");
        Assert.notNull(logicalOperator, "logicalOperator cannot be null");
        this.queryCondition1 = queryCondition1;
        this.queryCondition2 = queryCondition2;
        this.logicalOperator = logicalOperator;
    }


    /**
     * Creates a new FilterGroup with the specified query conditions and default logical operator as AND.
     *
     * @param queryCondition1 The first query condition. Must not be null.
     * @param queryCondition2 The second query condition. Must not be null.
     * @throws IllegalArgumentException if any of the parameters are null.
     */

    public FilterGroup(QueryCondition queryCondition1, QueryCondition queryCondition2) {
        Assert.notNull(queryCondition1, "queryCondition1 cannot be null");
        Assert.notNull(queryCondition2, "queryCondition2 cannot be null");
        this.queryCondition1 = queryCondition1;
        this.queryCondition2 = queryCondition2;
    }

}