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
 * @author bucketli 2023/10/24 21:08:47
 */
public enum SecurityType {

    /** secret_file_url */
    CA_CERTIFICATE("SECURITY_TYPE_CA_CERTIFICATE"),

    /** krb5.conf (security file) , xxxx.keytab (secret file) */
    KERBEROS("SECURITY_TYPE_KERBEROS"),

    /** xxx.jks (security file) */
    USER_PASSWD_WITH_TLS("SECURITY_TYPE_USER_PASSWD_WITH_TLS"),

    USER_PASSWD_WITH_SCRAM("SECURITY_TYPE_USER_PASSWD_WITH_SCRAM"),

    USER_PASSWD_WITH_KEYSTORE("SECURITY_TYPE_USER_PASSWD_WITH_KEYSTORE"),

    USER_PASSWD("SECURITY_TYPE_USER_PASSWD"),

    ONLY_USER("SECURITY_TYPE_ONLY_USER"),

    ONLY_PASSWD("SECURITY_TYPE_ONLY_PASSWD"),

    API_KEY("SECURITY_TYPE_API_KEY"),

    AK_SK("SECURITY_TYPE_AK_SK"),

    NONE("SECURITY_TYPE_NONE");

    private final String i18nKey;

    SecurityType(String i18nKey){
        this.i18nKey = i18nKey;
    }

    public String getI18nKey() { return i18nKey; }

    public static SecurityType valueOfName(String name) {
        for (SecurityType type : values()) {
            if (type.name().equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("SecurityType Unsupported name:" + name);
    }
}
