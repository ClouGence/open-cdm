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
package com.clougence.clouddm.dsfamily.postgres.analysis;

public interface PgSecDomainOptionKeys {

    String OPT_CATALOG_OWNER             = "owner";
    String OPT_CATALOG_TABLESPACE        = "tableSpace";
    String OPT_CATALOG_RCV               = "refreshCollationVersion"; //refreshCollationVersion
    String OPT_CATALOG_RESET             = "reset";
    String OPT_CATALOG_ALLOW_CONNECTIONS = "allowConnections";
    String OPT_CATALOG_CONNECTION_LIMIT  = "connectionLimit";
    String OPT_CATALOG_IS_TEMPLATE       = "isTemplate";
    String OPT_CATALOG_CONF_SET          = "configSet";
    String OPT_CATALOG_CONF_NAME         = "configName";
    String OPT_CATALOG_CONF_VALUE        = "configValue";

    String OPT_JOIN_NATURAL              = "naturalJoin";
}
