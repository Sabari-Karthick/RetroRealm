package com.batman.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class FilterGroup<T extends  FilterComponent> {
    private List<T> conditions;
    private LogicalOperator logicalOperator;
}