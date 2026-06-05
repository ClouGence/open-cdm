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
package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.clouddm.console.web.global.i18n.I18nDsConfigMsgKeys;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class HiveExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "hdfsPrincipal", descKey = I18nDsConfigMsgKeys.DS_CONFIG_HIVE_HDFS_KERBEROS_PRINCIPAL, readOnly = false)
    private String hdfsPrincipal;

    @DsConfigDef(name = "defaultFS", descKey = I18nDsConfigMsgKeys.DS_CONFIG_HIVE_DEFAULT_FS, valueAdvance = "host:port", readOnly = false)
    private String defaultFS;

    @DsConfigDef(name = "fsDir", descKey = I18nDsConfigMsgKeys.DS_CONFIG_HIVE_FS_DIR, defaultValue = "/user/hive/warehouse", valueAdvance = "e.g.,/user/hive/warehouse,same as hive.metastore.warehouse.dir", readOnly = false)
    private String fsDir;

    @DsConfigDef(name = "hadoopUser", descKey = I18nDsConfigMsgKeys.DS_CONFIG_HUDI_HIVE_HADOOP_USER, defaultValue = "hadoop", valueAdvance = "hadoop or other", readOnly = false)
    private String hadoopUser;

}
