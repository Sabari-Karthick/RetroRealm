package com.batman.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryCondition implements FilterComponent{
	
	private String field;
	private Object value;
	private QueryOperator queryOperator = QueryOperator.EQUAL;

}
