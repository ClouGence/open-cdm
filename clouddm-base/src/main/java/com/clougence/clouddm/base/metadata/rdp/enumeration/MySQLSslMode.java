package com.clougence.clouddm.base.metadata.rdp.enumeration;

/**
 * @author chunlin create time is 2025/7/14
 * Since MySQL 8.0.13, the useSSL connection property has been replaced by the sslMode connection property.
 * https://dev.mysql.com/doc/connectors/en/connector-j-connp-props-security.html
 */
public enum MySQLSslMode {

    /**
     * Do not use encrypted connections.
     * Equivalent to the old {"useSSL=false"} option.
     */
    DISABLED("DISABLED"),
    /**
     * Default mode, attempt to encrypt the connection, if unable to encrypt, use an unencrypted connection.
     * Equivalent to the old {"useSSL=true"，"requireSSL=false"，"verifyServerCertificate=false"} option.
     */
    PREFERRED("PREFERRED"),
    /**
     * Indicates that the connection must be encrypted. If encryption is not possible, the connection will fail.
     * Equivalent to the old {"useSSL=true"，"requireSSL=true"，"verifyServerCertificate=false"} option.
     */
    REQUIRED("REQUIRED"),
    /**
     * Indicates that the connection must be encrypted and the server certificate must be verified for validity using the local CA certificate.
     * Equivalent to the old {"useSSL=true", "verifyServerCertificate=true"} option.
     */
    VERIFY_CA("VERIFY_CA"),
    /**
     * Indicates that the connection must be encrypted and the server certificate must be verified for validity using a local CA certificate,
     * and the host name or IP address of the server certificate must be verified to match the actual connected host name or IP address.
     * There is no equivalent legacy settings for "sslMode=VERIFY_IDENTITY" on Connector/J 8.0.12 and earlier
     */
    VERIFY_IDENTITY("VERIFY_IDENTITY");

    public final String                value;

    public static final MySQLSslMode[] VALUES = values();

    MySQLSslMode(String value){
        this.value = value;
    }

    public static MySQLSslMode valueOfCode(String code) {
        if (code == null || code.isEmpty()) {
            return null;
        }
        for (MySQLSslMode sslMode : VALUES) {
            if (sslMode.getValue().equalsIgnoreCase(code)) {
                return sslMode;
            }
        }
        throw new IllegalArgumentException("MySQLSslMode unsupported value: " + code);
    }

    public static MySQLSslMode nameOf(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        for (MySQLSslMode sslMode : VALUES) {
            if (sslMode.name().equalsIgnoreCase(name)) {
                return sslMode;
            }
        }
        throw new IllegalArgumentException("MySQLSslMode unsupported name: " + name);
    }

    public String getValue() { return value; }
}
