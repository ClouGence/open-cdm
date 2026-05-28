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
package com.clougence.clouddm.init.constant;

public final class InitSeedConstants {

    public static final String ADMIN_UID                  = "9999999999999999";
    public static final String DEFAULT_PRIMARY_EMAIL      = "admin@cdmgr.com";
    public static final String DEFAULT_PRIMARY_PHONE      = "12345678900";
    public static final String DEFAULT_PRIMARY_PASSWORD   = "123456";
    public static final String DEFAULT_PRIMARY_ACCESS_KEY = "ak0a2c62tdo1ap2416655mpyx0v36l359p1v5rn782caw8t0qkk1s94b80lfs90";
    public static final String DEFAULT_PRIMARY_SECRET_KEY = "sk6206iy4pb0eydz9hg97jo3tu5d80j97e91bbql65167u8wb75x4ej6e4v4aa4";
    public static final String DEFAULT_CLUSTER_NAME       = "cluster1aw2byj490";
    public static final String DEFAULT_CLUSTER_DESC       = "Default Cluster";
    public static final String DEFAULT_REGION             = "customer";
    public static final String DEFAULT_WORKER_NAME        = "workers8c4qs80l26";
    public static final String DEFAULT_WORKER_WSN         = "wsn582nm54ca045p014288w6e919ec6294m430h427619v64g0pyqzcjb5040q3f";
    public static final String DEFAULT_WORKER_IP          = "172.31.239.4";
    public static final String DEFAULT_CONSOLE_IP         = "172.31.239.3";
    public static final String DEFAULT_EXTERNAL_IP        = "183.134.161.226";
    public static final String RUNTIME_ADMIN_EMAIL_KEY    = "clougence.init.admin.email";
    public static final String RUNTIME_ADMIN_PASSWORD_KEY = "clougence.init.admin.password";

    private InitSeedConstants(){
    }

    public static String resolveAdminEmail() {
        return defaultIfBlank(System.getProperty(RUNTIME_ADMIN_EMAIL_KEY), DEFAULT_PRIMARY_EMAIL);
    }

    public static String resolveAdminPassword() {
        return defaultIfBlank(System.getProperty(RUNTIME_ADMIN_PASSWORD_KEY), DEFAULT_PRIMARY_PASSWORD);
    }

    public static String escapeSqlLiteral(String value) {
        return value == null ? "" : value.replace("'", "''");
    }

    private static String defaultIfBlank(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value;
    }
}
