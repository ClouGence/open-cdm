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
package com.clougence.clouddm.ds.oracle.i18n;

import com.clougence.clouddm.base.metadata.ds.ConfigI18nKey;
import com.clougence.utils.i18n.I18nResource;

@I18nResource("/META-INF/clougence/i18n/ora-config")
public interface OraConfigI18nKeys extends ConfigI18nKey {

    String CONFIG_ORACLE_CONNECT_TYPE_LABEL                         = "CONFIG_ORACLE_CONNECT_TYPE_LABEL";
    String CONFIG_ORACLE_CONNECT_TYPE_DESC                          = "CONFIG_ORACLE_CONNECT_TYPE_DESC";
    String CONFIG_ORACLE_SID_LABEL                                  = "CONFIG_ORACLE_SID_LABEL";
    String CONFIG_ORACLE_SID_DESC                                   = "CONFIG_ORACLE_SID_DESC";
    String CONFIG_ORACLE_SERVICE_LABEL                              = "CONFIG_ORACLE_SERVICE_LABEL";
    String CONFIG_ORACLE_SERVICE_DESC                               = "CONFIG_ORACLE_SERVICE_DESC";
    String CONFIG_ORACLE_PDB_LABEL                                  = "CONFIG_ORACLE_PDB_LABEL";
    String CONFIG_ORACLE_PDB_DESC                                   = "CONFIG_ORACLE_PDB_DESC";
    String CONFIG_ORACLE_TNS_LABEL                                  = "CONFIG_ORACLE_TNS_LABEL";
    String CONFIG_ORACLE_TNS_ADMIN_LABEL                            = "CONFIG_ORACLE_TNS_ADMIN_LABEL";
    String CONFIG_ORACLE_TNS_ADMIN_DESC                             = "CONFIG_ORACLE_TNS_ADMIN_DESC";
    String CONFIG_ORACLE_TNS_NAME_LABEL                             = "CONFIG_ORACLE_TNS_NAME_LABEL";
    String CONFIG_ORACLE_TNS_NAME_DESC                              = "CONFIG_ORACLE_TNS_NAME_DESC";
    String CONFIG_ORACLE_EXCLUDE_ORA_MAINTAINED_SCHEMAS_LABEL       = "CONFIG_ORACLE_EXCLUDE_ORA_MAINTAINED_SCHEMAS_LABEL";
    String CONFIG_ORACLE_EXCLUDE_ORA_MAINTAINED_SCHEMAS_DESC        = "CONFIG_ORACLE_EXCLUDE_ORA_MAINTAINED_SCHEMAS_DESC";
}
