package com.batman.elastic;

import com.batman.criteria.FilterComponent;

import java.util.List;


public interface IRetroESBaseRepository<T> {
        
	List<T> findAll(List<FilterComponent> filterComponents,int start,int size,Class<T> clazz);
	T save(T model);
	List<T> saveBatch(List<T> models);
}
