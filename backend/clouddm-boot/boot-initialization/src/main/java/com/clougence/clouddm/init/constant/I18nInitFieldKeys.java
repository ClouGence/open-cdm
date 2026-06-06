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

import com.clougence.utils.i18n.I18nResource;

@I18nResource("/i18n/init-fields")
public enum I18nInitFieldKeys {

    INIT_FIELD_JDBC_URL_LABEL,
    INIT_FIELD_JDBC_URL_DESC,
    INIT_FIELD_DB_USERNAME_LABEL,
    INIT_FIELD_DB_USERNAME_DESC,
    INIT_FIELD_DB_PASSWORD_LABEL,
    INIT_FIELD_DB_PASSWORD_DESC,
    INIT_FIELD_JWT_SECRET_LABEL,
    INIT_FIELD_JWT_SECRET_DESC,
    INIT_FIELD_ADMIN_EMAIL_LABEL,
    INIT_FIELD_ADMIN_EMAIL_DESC,
    INIT_FIELD_ADMIN_PASSWORD_LABEL,
    INIT_FIELD_ADMIN_PASSWORD_DESC,
    INIT_FIELD_SERVER_PORT_LABEL,
    INIT_FIELD_SERVER_PORT_DESC,
    INIT_FIELD_RSOCKET_DNS_LABEL,
    INIT_FIELD_RSOCKET_DNS_DESC,
    INIT_FIELD_RSOCKET_PORT_LABEL,
    INIT_FIELD_RSOCKET_PORT_DESC,
    INIT_TEST_DB_SUCCESS,
    INIT_TEST_DB_CONNECTION_FAILED,
    INIT_TEST_DB_CHARSET_INVALID,
    INIT_MYSQL_DRIVER_REQUIRED,
    INIT_MYSQL_DRIVER_PREPARING,
    INIT_MYSQL_DRIVER_READY,
    INIT_MYSQL_DRIVER_UNAVAILABLE,
    INIT_MYSQL_DRIVER_PREPARE_FAILED,
    INIT_MYSQL_DRIVER_PREPARE_STARTED,
    INIT_MYSQL_DRIVER_FILE_DOWNLOAD_COMPLETE,
    INIT_MYSQL_DRIVER_FILE_DOWNLOADING,
    INIT_MYSQL_DRIVER_FILE_DEFAULT_NAME,
    INIT_TEST_DB_REBUILD_PROMPT,
    INIT_TEST_DB_USE_EXISTING_WARNING,
    INIT_TEST_DB_REBUILD_WARNING,
    INIT_TEST_DB_REBUILD_CONFIRM_LABEL,
}
