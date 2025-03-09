package com.batman.elastic;

import java.util.List;


public  interface IElasticSearchRepository<T> {
        
	List<T> findAll();
}
