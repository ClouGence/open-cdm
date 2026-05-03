package com.clougence.clouddm.sdk.execute.session.rdb;

import java.sql.Connection;

/**
 * @version : 2013-10-30
 * @author mode (zyc@hasor.net)
 */
public enum RdbIsolation {

    /** @see java.sql.Connection */
    DEFAULT("DEFAULT", Connection.TRANSACTION_NONE),

    /** @see java.sql.Connection#TRANSACTION_READ_UNCOMMITTED */
    READ_UNCOMMITTED("READ_UNCOMMITTED", Connection.TRANSACTION_READ_UNCOMMITTED),

    /** @see java.sql.Connection#TRANSACTION_READ_COMMITTED */
    READ_COMMITTED("READ_COMMITTED", Connection.TRANSACTION_READ_COMMITTED),

    /** @see java.sql.Connection#TRANSACTION_REPEATABLE_READ */
    REPEATABLE_READ("REPEATABLE_READ", Connection.TRANSACTION_REPEATABLE_READ),

    /** @see java.sql.Connection#TRANSACTION_SERIALIZABLE */
    SERIALIZABLE("SERIALIZABLE", Connection.TRANSACTION_SERIALIZABLE);

    private final String name;
    private final int    value;

    RdbIsolation(String name, int value){
        this.name = name;
        this.value = value;
    }

    public static RdbIsolation valueOfCode(final int value) {
        switch (value) {
            case Connection.TRANSACTION_NONE:
                return RdbIsolation.DEFAULT;
            case Connection.TRANSACTION_READ_UNCOMMITTED:
                return RdbIsolation.READ_UNCOMMITTED;
            case Connection.TRANSACTION_READ_COMMITTED:
                return RdbIsolation.READ_COMMITTED;
            case Connection.TRANSACTION_REPEATABLE_READ:
                return RdbIsolation.REPEATABLE_READ;
            case Connection.TRANSACTION_SERIALIZABLE:
                return RdbIsolation.SERIALIZABLE;
        }
        throw new IllegalStateException(String.format("Connection ISOLATION error level %s.", value));
    }

    public static RdbIsolation valueOfCode(String value) {
        if (value == null) {
            return DEFAULT;
        }
        switch (value.toUpperCase()) {
            case "READ_UNCOMMITTED":
                return READ_UNCOMMITTED;
            case "READ_COMMITTED":
                return READ_COMMITTED;
            case "REPEATABLE_READ":
                return REPEATABLE_READ;
            case "SERIALIZABLE":
                return SERIALIZABLE;
            case "DEFAULT":
            default:
                return DEFAULT;
        }
    }

    public String getName() { return this.name; }

    public int getValue() { return this.value; }
}
