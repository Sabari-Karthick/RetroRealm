package com.batman.elastic;

import com.batman.criteria.FilterComponent;
import com.batman.criteria.Sort;
import org.springframework.data.domain.Page;

import java.util.List;


public interface IRetroESBaseRepository<T> {
        
	List<T> findAll(List<FilterComponent> filterComponents,Sort sort,int start,int size,Class<T> clazz);
	Page<T> getAllByPage(List<FilterComponent> filterComponents, Sort sort, int start, int size, Class<T> clazz);
	T save(T model);
	List<T> saveBatch(List<T> models);
}
