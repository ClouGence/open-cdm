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

import java.util.HashMap;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.clouddm.base.metadata.rdp.enumeration.PaimonPropsKey;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.clouddm.console.web.global.i18n.I18nDsConfigMsgKeys;
import com.clougence.clouddm.platform.dal.model.system.KvConfValType;
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
public class DataLakeFormationExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "httpsEnabled", defaultValue = "false", descKey = I18nDsConfigMsgKeys.DS_CONFIG_PAIMON_CATALOG_URI_HTTPS_ENABLED, valueAdvance = "true / false", readOnly = false, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean             httpsEnabled;

    @DsConfigDef(name = "warehouse", descKey = I18nDsConfigMsgKeys.DS_CONFIG_PAIMON_METASTORE_TYPE, valueAdvance = "my_instance_name", readOnly = false)
    private String              warehouse;

    @DsConfigDef(name = "metastoreType", defaultValue = "rest", descKey = I18nDsConfigMsgKeys.DS_CONFIG_PAIMON_METASTORE_TYPE, valueAdvance = "filesystem / rest")
    private String              metastoreType;

    @DsConfigDef(name = "catalogProps", descKey = I18nDsConfigMsgKeys.DS_CONFIG_PAIMON_CATALOG_PROPS, readOnly = false)
    private String              catalogProps;

    /**
     * will put oll the catalog props into this map
     */
    private Map<String, String> catalogPropsMap;

    public void deserialize() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(PaimonPropsKey.CATALOG_METASTORE, this.metastoreType);
        attributes.put(PaimonPropsKey.CATALOG_WAREHOUSE, this.warehouse);

        if (StringUtils.isNotBlank(catalogProps)) {
            try {
                Map<String, String> props = new ObjectMapper().readValue(catalogProps, new TypeReference<Map<String, String>>() {});
                attributes.putAll(props);
            } catch (Exception e) {
                throw new RuntimeException("Process paimon extra config error.msg:" + ExceptionUtils.getRootCauseMessage(e), e);
            }
        }

        this.catalogPropsMap = attributes;
    }
}
