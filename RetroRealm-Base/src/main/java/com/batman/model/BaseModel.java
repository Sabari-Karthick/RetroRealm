package com.batman.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class BaseModel {

	@Transient
	private String indexName;

	@Transient
	private String type;

	@JsonIgnore
	public abstract Object getPrimaryKeyValue();

}
