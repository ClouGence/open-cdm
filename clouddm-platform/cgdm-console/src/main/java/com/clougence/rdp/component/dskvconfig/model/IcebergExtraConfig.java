package com.clougence.rdp.component.dskvconfig.model;

import java.util.HashMap;
import java.util.Map;

import com.clougence.clouddm.base.metadata.rdp.enumeration.IcebergPropsKey;
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
public class IcebergExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "httpsEnabled", defaultValue = "false", descKey = I18nDsConfigMsgKeys.DS_CONFIG_ICEBERG_CATALOG_URI_HTTPS_ENABLED, valueAdvance = "true / false", readOnly = false, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean             httpsEnabled;

    @DsConfigDef(name = "catalogName", descKey = I18nDsConfigMsgKeys.DS_CONFIG_ICEBERG_CATALOG_NAME, readOnly = false)
    private String              catalogName;

    @DsConfigDef(name = "catalogType", descKey = I18nDsConfigMsgKeys.DS_CONFIG_ICEBERG_CATALOG_TYPE, valueAdvance = "GLUE / NESSIE / REST ...", readOnly = false)
    private String              catalogType;

    @DsConfigDef(name = "catalogWarehouse", descKey = I18nDsConfigMsgKeys.DS_CONFIG_ICEBERG_CATALOG_WAREHOUSE, valueAdvance = "s3://warehouse", readOnly = false)
    private String              catalogWarehouse;

    @DsConfigDef(name = "catalogProps", descKey = I18nDsConfigMsgKeys.DS_CONFIG_ICEBERG_CATALOG_PROPS, valueAdvance = "eg: {\"s3.endpoint\":\"http://localhost:9000\"}", readOnly = false)
    private String              catalogProps;

    /**
     * will put oll the catalog props into this map
     */
    private Map<String, String> catalogPropsMap;

    public void deserialize() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(IcebergPropsKey.CATALOG_TYPE, this.getCatalogType());
        attributes.put(IcebergPropsKey.WAREHOUSE_LOCATION, this.catalogWarehouse);

        if (StringUtils.isNotBlank(catalogProps)) {
            try {
                Map<String, String> props = new ObjectMapper().readValue(catalogProps, new TypeReference<Map<String, String>>() {
                });
                attributes.putAll(props);
            } catch (Exception e) {
                throw new RuntimeException("Process iceberg extra config config error.msg:" + ExceptionUtils.getRootCauseMessage(e), e);
            }
        }

        this.catalogPropsMap = attributes;
    }
}
