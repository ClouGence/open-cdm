package com.clougence.clouddm.dsfamily.postgres.execute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.postgre.PostgresConstraintType;
import com.clougence.adapter.postgre.PostgresForeignKeyRule;
import com.clougence.adapter.postgre.PostgresForeignMatchOption;

import lombok.Getter;
import lombok.Setter;

/**
 * Postgres 外键
 * @version : 2021-05-17
 * @author 赵永春 (zyc@hasor.net)
 */
@Getter
@Setter
public class PgForeignKey {

    private String                     schema;
    private String                     table;
    private String                 name;
    private PostgresConstraintType constraintType;

    private List<String>               columns          = new ArrayList<>();
    private String                     referenceSchema;
    private String                     referenceTable;
    private Map<String, String>        referenceMapping = new HashMap<>();
    private PostgresForeignKeyRule     updateRule;
    private PostgresForeignKeyRule     deleteRule;
    private PostgresForeignMatchOption matchOption;
}
