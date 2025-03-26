package com.batman.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class QueryCondition implements FilterComponent{
	
	private String field;
	private Object value;
	private QueryOperator queryOperator = QueryOperator.EQUAL;

	public QueryCondition(String field, Object value){
		this.field = field;
		this.value = value;
	}

	public QueryCondition(String field, Object value, QueryOperator queryOperator){
		this.field = field;
		this.value = value;
		this.queryOperator = queryOperator;
	}

}
