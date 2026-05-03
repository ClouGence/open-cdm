package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

public enum RdbQueryMode {

    NORMAL,
    UNION,
    JOIN,
    WITH,
    WITH_BODY,
    SUB_WHERE,
    SUB_SELECT,
    SUB_SET,
    SUB_FROM,
    SUB_CALL,
    //TEMP,
    INSERT,
    CREATE,

}
