package com.batman.elastic;

import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ElasticSearchRepository<T> implements IElasticSearchRepository<T> {

	@Override
	public List<T> findAll() {
		log.info("Entering ElasticSearchRepository find All ...");
		
		return Collections.emptyList();
	}

}
