package com.batman.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Data;

@MappedSuperclass
@Data
public class BaseModel {

    @Transient
    private String indexName;

    @Transient
    private String type;

}
