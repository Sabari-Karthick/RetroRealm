package com.batman.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MappedSuperclass
@Data
public abstract class BaseModel {

	@Transient
	private String indexName;

	@Transient
	private String type;

	@Transient
	@JsonIgnore
	private Map<String, List<String>> flattenFieldsConfig = new HashMap<>();

	@JsonIgnore
	public abstract Object getPrimaryKeyValue();

}
