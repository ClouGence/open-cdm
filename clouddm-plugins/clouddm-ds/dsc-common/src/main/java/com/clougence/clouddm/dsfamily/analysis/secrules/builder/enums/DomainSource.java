package com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums;

public enum DomainSource {
    // It won't be any other child node
    NONE,

    DELETE,

    // common
    OBJ_NAME,
    CONSTANT,
    VARIABLE,
    COLUMN,
    COLUMN_LIST,

    //
    RENAME,

    // column
    COLUMN_TYPE,

    // select
    SELECT,
    WHERE,
    SELECT_ITEM,
    FROM,
    JOIN,
    TABLE,

    //UPDATE
    SET_VALUE,
    UPDATE,
    UPDATE_COLUMN,

    // insert
    VALUES,
    INSERT_COLUMN,

    // column, constraint
    COLUMN_DEF,
    CONSTRAINT,
    CONSTRAINT_TYPE,

    // alter table
    ALTER_TABLE_ITEM,
    COLUMN_DEFAULT_VALUE,
    COMMENT,

    INDEX,

    // func
    FUNCTION_ARGS,
    FUNCTION,

    CREATE_TABLE,

    KEY_VALUE,
    OPTIONS,

    // postgres
    INHERIT,
    SET_OPT,
    ROLE_SPEC,
    ARRAY

}
