/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
