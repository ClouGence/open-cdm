package com.clougence.clouddm.base.metadata.rdp.enumeration;

/**
 * @author bucketli 2020/6/19 10:41
 */
public enum SecurityFileType {
    // .e.g., .crt, .cer, .pem
    ca_certificate_file,
    // e.g., .crt, .cer, .pem
    client_certificate_file,
    // e.g., krb5.conf
    kerberos_conf_file,
    // e.g., .jks, .p12, .pfx
    ssl_truststore_file,
    // e.g., .jks, .p12, .pfx
    ssl_keystore_file,
    // e.g., jaas.conf
    jaas_file,
    // e.g., kerberos.keytab
    kerberos_keytab_file,
    // e.g., .keystore
    keystore_file,
    // e.g., .key, .secret, .env, .pk8
    secret_file
}
