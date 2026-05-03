package com.clougence.clouddm.sdk.security.auth;

// is sql kind for
public enum SecQueryKind {

    // for SQL
    CREATE,
    ALTER,
    DROP,
    QUERY,
    DML,
    CALL,
    EXPLAIN,

    // for nosql
    READ,
    WRITE,

    // for others
    ADMIN,
    OTHER
}
