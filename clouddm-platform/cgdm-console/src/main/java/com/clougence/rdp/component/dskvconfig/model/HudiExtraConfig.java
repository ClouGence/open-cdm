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
import com.clougence.clouddm.base.metadata.rdp.enumeration.HudiFsType;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.clouddm.console.web.global.i18n.I18nDsConfigMsgKeys;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * @author bucketli 2022/8/10 09:33:48
 */
@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class HudiExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "fsType", defaultValue = "HDFS", descKey = I18nDsConfigMsgKeys.DS_CONFIG_HUDI_FS_TYPE, valueAdvance = "HDFS/OSS/S3...", readOnly = false)
    private HudiFsType fsType;

    @DsConfigDef(name = "defaultFS", descKey = I18nDsConfigMsgKeys.DS_CONFIG_HUDI_DEFAULT_FS, valueAdvance = "host:port", readOnly = false)
    private String     defaultFS;

    @DsConfigDef(name = "fsDir", descKey = I18nDsConfigMsgKeys.DS_CONFIG_HUDI_FS_DIR, valueAdvance = "e.g.,/user/hive/warehouse,same as hive.metastore.warehouse.dir", readOnly = false)
    private String     fsDir;

    @DsConfigDef(name = "hadoopUser", descKey = I18nDsConfigMsgKeys.DS_CONFIG_HUDI_HIVE_HADOOP_USER, valueAdvance = "hadoop or other", readOnly = false)
    private String     hadoopUser;

    @DsConfigDef(name = "hdfsPrincipal", descKey = I18nDsConfigMsgKeys.DS_CONFIG_HUDI_HDFS_KERBEROS_PRINCIPAL, readOnly = false)
    private String     hdfsPrincipal;
}
