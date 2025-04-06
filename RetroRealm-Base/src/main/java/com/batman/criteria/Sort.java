package com.batman.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.query.SortDirection;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Sort {
    private String field;
    private SortDirection direction;
}
