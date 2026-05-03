package com.clougence.rdp.component.dskvconfig.model;

import java.util.HashMap;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.rdp.constant.I18nDsConfigMsgKeys;
import com.clougence.rdp.constant.KvConfValType;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeltaLakeExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "httpsEnabled", defaultValue = "false", descKey = I18nDsConfigMsgKeys.DS_CONFIG_DELTALAKE_CATALOG_URI_HTTPS_ENABLED, valueAdvance = "true / false", readOnly = false, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean             httpsEnabled;

    @DsConfigDef(name = "warehouse", descKey = I18nDsConfigMsgKeys.DS_CONFIG_DELTALAKE_WAREHOUSE, valueAdvance = "s3://warehouse/test, file:///warehouse", readOnly = false)
    private String              warehouse;

    @DsConfigDef(name = "metastoreType", defaultValue = "filesystem", descKey = I18nDsConfigMsgKeys.DS_CONFIG_DELTALAKE_METASTORE_TYPE, valueAdvance = "filesystem", readOnly = false)
    private String              metastoreType;

    @DsConfigDef(name = "catalogProps", descKey = I18nDsConfigMsgKeys.DS_CONFIG_DELTALAKE_CATALOG_PROPS, valueAdvance = "eg: {\"fs.s3a.access.key\":\"YOUR_ACCESS_KEY\",\"fs.s3a.secret.key\":\"YOUR_SECRET_KEY\",\"fs.s3a.path.style.access\":\"true\"}", readOnly = false)
    private String              catalogProps;

    @DsConfigDef(name = "deleteStrategy", defaultValue = "FORCE_REWRITE", descKey = I18nDsConfigMsgKeys.DS_CONFIG_DELTALAKE_DELETE_STRATEGY, readOnly = false, valueAdvance = "AUTO / FORCE_DV / FORCE_REWRITE")
    private String              deleteStrategy;

    @DsConfigDef(name = "dvStorage", descKey = I18nDsConfigMsgKeys.DS_CONFIG_DELTALAKE_DV_STORAGE, readOnly = false, valueAdvance = "PATH_DV_MARKER / INLINE_DV_MARKER / UUID_DV_MARKER")
    private String              dvStorage;

    @DsConfigDef(name = "dvPrefix", descKey = I18nDsConfigMsgKeys.DS_CONFIG_DELTALAKE_DV_PREFIX, readOnly = false, valueAdvance = "eg: _deletion_vector")
    private String              dvPrefix;

    /**
     * will put oll the catalog props into this map
     */
    private Map<String, String> catalogPropsMap;

    public void deserialize() {
        Map<String, String> attributes = new HashMap<>();

        if (StringUtils.isNotBlank(catalogProps)) {
            try {
                Map<String, String> props = new ObjectMapper().readValue(catalogProps, new TypeReference<Map<String, String>>() {
                });
                attributes.putAll(props);
            } catch (Exception e) {
                throw new RuntimeException("Process DeltaLake extra config error.msg:" + ExceptionUtils.getRootCauseMessage(e), e);
            }
        }

        this.catalogPropsMap = attributes;
    }
}
