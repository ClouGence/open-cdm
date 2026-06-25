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
package com.clougence.clouddm.ds.maxcompute.dsconf;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.UiUtils;
import com.clougence.clouddm.base.metadata.ui.form.value.FieldOptionValueDef;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.maxcompute.i18n.McConfigI18nKeys;
import com.clougence.clouddm.sdk.execute.dsconf.DsConfigSpi;
import com.clougence.drivers.adapter.ConvertUtils;
import com.clougence.utils.StringUtils;

public class McConfigSpi implements DsConfigSpi {

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return McConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        McConfig config = (McConfig) dsConfig;
        Long connectTimeoutMs = ConvertUtils.toLong(defaultConfig.get(McConfig.Fields.connectTimeoutMs), false);
        Integer soTimeoutSec = ConvertUtils.toInteger(defaultConfig.get(McConfig.Fields.soTimeoutSec), false);
        config.setUserName(defaultConfig.get(DataSourceConfig.Fields.userName));
        config.setPassword(defaultConfig.get(DataSourceConfig.Fields.password));
        config.setSdkEndpoint(defaultConfig.get(McConfig.Fields.sdkEndpoint));
        config.setDefaultCatalog(defaultConfig.get(McConfig.Fields.defaultCatalog));
        config.setDefaultSchema(defaultConfig.get(McConfig.Fields.defaultSchema));
        config.setConnectTimeoutMs(connectTimeoutMs == null ? 5000L : connectTimeoutMs);
        config.setSoTimeoutSec(soTimeoutSec == null ? 10 : soTimeoutSec);
        config.setClientTimeZone(defaultConfig.get(McConfig.Fields.clientTimeZone));
        config.setInteractiveMode(ConvertUtils.toBoolean(defaultConfig.get(McConfig.Fields.interactiveMode), false));

        boolean blank = StringUtils.isBlank(defaultConfig.get(McConfig.Fields.schemaStyle));
        config.setSchemaStyle((blank ? Boolean.FALSE : ConvertUtils.toBoolean(defaultConfig.get(McConfig.Fields.schemaStyle), false)));
        return dsConfig;
    }

    @Override
    public void customizeAddPanels(Map<DsConfigGroup, UiPanel> panels) {
        UiPanel general = panels.get(DsConfigGroup.GENERAL);
        if (general == null) {
            return;
        }

        UiPanelField host = general.findField(DataSourceConfig.Fields.host);
        UiPanelField sdkEndpoint = general.findField(McConfig.Fields.sdkEndpoint);
        if (host != null && sdkEndpoint != null) {
            general.removeField(DataSourceConfig.Fields.host);
            general.removeField(McConfig.Fields.sdkEndpoint);
            UiPanelField endpoint = UiPanelField.builder()
                .field("maxComputeEndpoint")
                .type(UiPanelFieldType.MaxComputeEndpoint)
                .titleI18N(McConfigI18nKeys.CONFIG_ADD_DS_MC_ENDPOINT_LABEL)
                .descI18N(McConfigI18nKeys.CONFIG_ADD_DS_MC_ENDPOINT_DESC)
                .defaultValue(UiUtils.strValueDef("cn-hangzhou|public"))
                .options(maxComputeEndpointOptions())
                .build();
            endpoint.addField(hiddenField(DataSourceConfig.Fields.host));
            endpoint.addField(hiddenField(McConfig.Fields.sdkEndpoint));
            general.afterAddField(endpoint, DataSourceConfig.Fields.driverVersion);
        }

        UiPanelField defaultCatalog = general.findField(McConfig.Fields.defaultCatalog);
        if (defaultCatalog != null) {
            defaultCatalog.setTitleI18N(McConfigI18nKeys.CONFIG_MC_PROJECT_LABEL);
            defaultCatalog.setDescI18N("");
        }

        UiPanel options = panels.get(DsConfigGroup.OPTIONS);
        UiPanelField interactiveMode = general.findField(McConfig.Fields.interactiveMode);
        if (interactiveMode != null) {
            general.removeField(McConfig.Fields.interactiveMode);
            interactiveMode.setTitleI18N(McConfigI18nKeys.CONFIG_MC_INTERACTIVE_MODE_LABEL);
            interactiveMode.setDescI18N(McConfigI18nKeys.CONFIG_MC_INTERACTIVE_MODE_DESCRIPTION);
            if (options == null) {
                general.addField(interactiveMode);
            } else {
                options.addField(interactiveMode);
            }
        }

        UiPanelField schemaStyle = general.findField(McConfig.Fields.schemaStyle);
        if (schemaStyle != null) {
            general.removeField(McConfig.Fields.schemaStyle);
            schemaStyle.setTitleI18N(McConfigI18nKeys.CONFIG_MC_SCHEMA_STYLE_LABEL);
            schemaStyle.setDescI18N(McConfigI18nKeys.CONFIG_MC_SCHEMA_STYLE_DESCRIPTION);
            schemaStyle.setDefaultValue(UiUtils.boolValueDef(false));
            UiPanelField defaultSchema = general.findField(McConfig.Fields.defaultSchema);
            if (defaultSchema != null) {
                general.removeField(McConfig.Fields.defaultSchema);
                defaultSchema.setActiveExpr(UiUtils.activeWhenEquals(McConfig.Fields.schemaStyle, "true"));
                defaultSchema.setDescI18N(McConfigI18nKeys.CONFIG_MC_DEFAULT_SCHEMA_DESCRIPTION);
                schemaStyle.addField(defaultSchema);
            }
            general.afterAddField(schemaStyle, McConfig.Fields.defaultCatalog);
        }
    }

    protected UiPanelField hiddenField(String field) {
        return UiPanelField.builder().field(field).type(UiPanelFieldType.Input).hide(true).build();
    }

    protected List<ValueDef> maxComputeEndpointOptions() {
        List<ValueDef> options = new ArrayList<>();
        addMaxComputeEndpointOptions(options, "cn-wulanchabu", "华北 6（乌兰察布）");
        addMaxComputeEndpointOptions(options, "cn-beijing", "华北 2（北京）");
        addMaxComputeEndpointOptions(options, "cn-shanghai", "华东 2（上海）");
        addMaxComputeEndpointOptions(options, "cn-hongkong", "中国香港");
        addMaxComputeEndpointOptions(options, "cn-zhangjiakou", "华北 3（张家口）");
        addMaxComputeEndpointOptions(options, "cn-shenzhen", "华南 1（深圳）");
        addMaxComputeEndpointOptions(options, "ap-northeast-1", "日本（东京）");
        addMaxComputeEndpointOptions(options, "cn-chengdu", "西南 1（成都）");
        addMaxComputeEndpointOptions(options, "ap-southeast-1", "新加坡");
        addMaxComputeEndpointOptions(options, "ap-southeast-3", "马来西亚（吉隆坡）");
        addMaxComputeEndpointOptions(options, "ap-southeast-5", "印度尼西亚（雅加达）");
        addMaxComputeEndpointOptions(options, "cn-hangzhou", "华东 1（杭州）");
        addMaxComputeEndpointOptions(options, "us-east-1", "美国（弗吉尼亚）");
        addMaxComputeEndpointOptions(options, "eu-west-1", "英国（伦敦）");
        addMaxComputeEndpointOptions(options, "us-west-1", "美国（硅谷）");
        addMaxComputeEndpointOptions(options, "eu-central-1", "德国（法兰克福）");
        addMaxComputeEndpointOptions(options, "me-east-1", "阿联酋（迪拜）", "maxcompute.aliyuncs.com");
        addMaxComputeEndpointOptions(options, "me-central-1", "沙特（利雅得）");
        addMaxComputeEndpointOptions(options, "cn-hangzhou-finance", "华东 1 金融云");
        addMaxComputeEndpointOptions(options, "cn-shenzhen-finance-1", "华南 1 金融云");
        addMaxComputeEndpointOptions(options, "cn-beijing-finance-1", "华北 2 金融云");
        addMaxComputeEndpointOptions(options, "cn-shanghai-finance-1", "华东 2 金融云");
        return options;
    }

    protected void addMaxComputeEndpointOptions(List<ValueDef> options, String regionId, String regionName) {
        addMaxComputeEndpointOptions(options, regionId, regionName, "maxcompute." + regionId + ".aliyuncs.com");
    }

    protected void addMaxComputeEndpointOptions(List<ValueDef> options, String regionId, String regionName, String publicSdkEndpoint) {
        Map<String, Object> publicEndpoint = new LinkedHashMap<>();
        publicEndpoint.put("regionId", regionId);
        publicEndpoint.put("accessType", "public");
        publicEndpoint.put("host", "https://service." + regionId + ".maxcompute.aliyun.com/api");
        publicEndpoint.put("sdkEndpoint", publicSdkEndpoint);
        options.add(FieldOptionValueDef.builder().labelI18N(regionName + " / 公网").value(publicEndpoint).build());

        Map<String, Object> vpcEndpoint = new LinkedHashMap<>();
        vpcEndpoint.put("regionId", regionId);
        vpcEndpoint.put("accessType", "vpc");
        vpcEndpoint.put("host", "https://service." + regionId + "-vpc.maxcompute.aliyun-inc.com/api");
        vpcEndpoint.put("sdkEndpoint", "maxcompute-vpc." + regionId + ".aliyuncs.com");
        options.add(FieldOptionValueDef.builder().labelI18N(regionName + " / VPC").value(vpcEndpoint).build());
    }

    @Override
    public boolean supportSSL() {
        return false;
    }

    @Override
    public boolean supportSSH() {
        return false;
    }

    @Override
    public List<SecurityType> securityTypes() {
        List<SecurityType> options = new ArrayList<>();
        options.add(SecurityType.AK_SK);
        return options;
    }
}
