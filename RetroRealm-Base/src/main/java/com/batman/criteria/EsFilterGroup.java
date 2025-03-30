package com.batman.criteria;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.Data;

import java.util.List;

@Data
public class EsFilterGroup implements FilterComponent{

    private List<Query> queries;
}
