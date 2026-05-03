package com.clougence.clouddm.base.metadata.ds;

import java.sql.JDBCType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class ColMetaData {

    private String   catalog;
    private String   schema;
    private String   table;
    private String   column;
    private int      index;

    private String   columnType;
    private JDBCType jdbcType;
    private int      precision;
}
