package com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums;

public enum CommonAttribute implements Attribute {
    IF_EXISTS,
    IF_NOT_EXISTS,

    ALIAS,

    VALUE,

    // func
    FUNC_ARG_NAME,

    // user and role
    PASSWORD,

    INDEX_TYPE,

    // column
    NOT_NULL,
    COLUMN_ALLOW_NULL,

    // insert
    //    INSERT_IGNORE,
    //    INSERT_UPDATE,
    INSERT_CONFLICT,

    //where
    VALID_WHERE,

    LIMIT,
    UNION,
    COMMENT,

    // drop
    CASCADE,
    RESTRICT,

    //view
    MATERIALIZED,
    DEFAULT,

    IGNORE,
    MULTI_VALUE,

}
