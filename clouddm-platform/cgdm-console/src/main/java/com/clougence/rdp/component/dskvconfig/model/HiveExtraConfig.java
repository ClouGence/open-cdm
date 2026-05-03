package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.rdp.constant.I18nDsConfigMsgKeys;
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
