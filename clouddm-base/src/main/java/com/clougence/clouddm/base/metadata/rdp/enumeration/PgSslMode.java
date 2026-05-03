package com.clougence.clouddm.base.metadata.rdp.enumeration;

import java.security.InvalidParameterException;

/**
 * @author bucketli 2023/10/25 11:27:58
 */
public enum PgSslMode {

    /**
     * Do not use encrypted connections.
     */
    DISABLE("disable"),
    /**
     * Start with non-encrypted connection, then try encrypted one.
     */
    ALLOW("allow"),
    /**
     * Start with encrypted connection, fallback to non-encrypted (default).
     */
    PREFER("prefer"),
    /**
     * Ensure connection is encrypted.
     */
    REQUIRE("require"),
    /**
     * Ensure connection is encrypted, and client trusts server certificate.
     */
    VERIFY_CA("verify-ca"),
    /**
     * Ensure connection is encrypted, client trusts server certificate, and server hostname matches
     * the one listed in the server certificate.
     */
    VERIFY_FULL("verify-full"),;

    public static final PgSslMode[] VALUES = values();

    public final String           value;

    PgSslMode(String value){
        this.value = value;
    }

    public static PgSslMode valueOfCode(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        for (PgSslMode pgSslMode : values()) {
            if (pgSslMode.getValue().equals(value)) {
                return pgSslMode;
            }
        }
        throw new InvalidParameterException("PgSslMode unsupported value:" + value);
    }

    public static PgSslMode nameOf(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        for (PgSslMode pgSslMode : values()) {
            if (pgSslMode.name().equalsIgnoreCase(name)) {
                return pgSslMode;
            }
        }
        throw new InvalidParameterException("PgSslMode unsupported name:" + name);
    }

    public String getValue() { return value; }
}
