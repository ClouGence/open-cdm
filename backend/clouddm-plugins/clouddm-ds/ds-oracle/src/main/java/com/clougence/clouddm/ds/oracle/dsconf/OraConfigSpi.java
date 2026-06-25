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
package com.clougence.clouddm.ds.oracle.dsconf;

import java.util.ArrayList;
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
import com.clougence.clouddm.ds.oracle.i18n.OraConfigI18nKeys;
import com.clougence.clouddm.dsfamily.dsconf.AbstractDsConfigSpi;
import com.clougence.drivers.adapter.ConvertUtils;
import com.clougence.utils.StringUtils;

public class OraConfigSpi extends AbstractDsConfigSpi {

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return OraConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        OraConfig config = (OraConfig) dsConfig;
        Long connectTimeoutMs = ConvertUtils.toLong(defaultConfig.get(OraConfig.Fields.connectTimeoutMs), false);
        Integer soTimeoutSec = ConvertUtils.toInteger(defaultConfig.get(OraConfig.Fields.soTimeoutSec), false);
        OraConnectType connectType = OraConnectType.of(defaultConfig.get(OraConfig.Fields.connectType));
        config.setConnectType(connectType);
        config.setSid(defaultConfig.get(OraConfig.Fields.sid));
        config.setServiceName(defaultConfig.get(OraConfig.Fields.serviceName));
        config.setPdbName(defaultConfig.get(OraConfig.Fields.pdbName));
        config.setTnsAdmin(defaultConfig.get(OraConfig.Fields.tnsAdmin));
        config.setTnsName(defaultConfig.get(OraConfig.Fields.tnsName));
        if (StringUtils.isNotBlank(config.getHost())) {
            String[] ipPort = config.getHost().split(":");
            if (ipPort.length == 3) {
                config.setHost(ipPort[0] + ":" + ipPort[1]);
                switch (connectType) {
                    case SID:
                        config.setSid(ipPort[2]);
                        break;
                    case SERVICE:
                        config.setServiceName(ipPort[2]);
                        break;
                    case PDB:
                        config.setPdbName(ipPort[2]);
                        break;
                    default:
                        throw new IllegalArgumentException("unsupported Oracle connect type:" + connectType);
                }
            } else if (ipPort.length != 2) {
                throw new IllegalArgumentException("unsupported Oracle host format:" + config.getHost());
            }
        }
        if (connectType == OraConnectType.PDB) {
            config.setConnectType(OraConnectType.SERVICE);
            if (StringUtils.isBlank(config.getServiceName())) {
                config.setServiceName(config.getPdbName());
            }
        }

        config.setAutoCommit(!"false".equalsIgnoreCase(defaultConfig.get(OraConfig.Fields.autoCommit)));
        config.setConnectTimeoutMs(connectTimeoutMs == null ? 5000L : connectTimeoutMs);
        config.setSoTimeoutSec(soTimeoutSec == null ? 10 : soTimeoutSec);

        boolean excludeOraMaintainedSchemas = StringUtils.isBlank(defaultConfig.get(OraConfig.Fields.excludeOraMaintainedSchemas));
        config.setExcludeOraMaintainedSchemas((excludeOraMaintainedSchemas ? Boolean.FALSE : //
            ConvertUtils.toBoolean(defaultConfig.get(OraConfig.Fields.excludeOraMaintainedSchemas), false)));
        return dsConfig;
    }

    @Override
    public void customizeAddPanels(Map<DsConfigGroup, UiPanel> panels) {
        setDefaultPort(panels, "1521");
        UiPanel general = panels.get(DsConfigGroup.GENERAL);
        if (general == null) {
            return;
        }
        UiPanel advanced = panels.get(DsConfigGroup.ADVANCED);
        if (advanced != null) {
            UiPanelField excludeOraMaintainedSchemas = advanced.findField(OraConfig.Fields.excludeOraMaintainedSchemas);
            if (excludeOraMaintainedSchemas != null) {
                excludeOraMaintainedSchemas.setTitleI18N(OraConfigI18nKeys.CONFIG_ORACLE_EXCLUDE_ORA_MAINTAINED_SCHEMAS_LABEL);
                excludeOraMaintainedSchemas.setDescI18N(OraConfigI18nKeys.CONFIG_ORACLE_EXCLUDE_ORA_MAINTAINED_SCHEMAS_DESCRIPTION);
            }
        }

        UiPanelField connectType = general.findField(OraConfig.Fields.connectType);
        if (connectType == null) {
            return;
        }

        UiPanelField sid = general.findField(OraConfig.Fields.sid);
        UiPanelField serviceName = general.findField(OraConfig.Fields.serviceName);
        UiPanelField pdbName = general.findField(OraConfig.Fields.pdbName);
        UiPanelField tnsAdmin = general.findField(OraConfig.Fields.tnsAdmin);
        UiPanelField tnsName = general.findField(OraConfig.Fields.tnsName);

        general.removeField(OraConfig.Fields.connectType);
        general.removeField(OraConfig.Fields.sid);
        general.removeField(OraConfig.Fields.serviceName);
        general.removeField(OraConfig.Fields.pdbName);
        general.removeField(OraConfig.Fields.tnsAdmin);
        general.removeField(OraConfig.Fields.tnsName);

        connectType.setType(UiPanelFieldType.Options);
        connectType.setDefaultValue(UiUtils.strValueDef(OraConnectType.SID.name()));
        connectType.setDescI18N("");
        connectType.addField(hiddenField(OraConfig.Fields.connectType));
        if (sid != null) {
            connectType.addField(hiddenField(OraConfig.Fields.sid));
        }
        if (serviceName != null) {
            connectType.addField(hiddenField(OraConfig.Fields.serviceName));
        }
        if (pdbName != null) {
            connectType.addField(hiddenField(OraConfig.Fields.pdbName));
        }
        if (tnsAdmin != null) {
            connectType.addField(hiddenField(OraConfig.Fields.tnsAdmin));
        }
        if (tnsName != null) {
            connectType.addField(hiddenField(OraConfig.Fields.tnsName));
        }

        List<ValueDef> options = new ArrayList<>();
        FieldOptionValueDef sidOption = UiUtils.fieldOptionDef("SID", OraConnectType.SID.name());
        if (sid != null) {
            sidOption.addField(sid);
        }
        options.add(sidOption);

        FieldOptionValueDef serviceOption = UiUtils.fieldOptionDef(OraConfigI18nKeys.CONFIG_ORACLE_SERVICE_OPTION_LABEL, OraConnectType.SERVICE.name());
        if (serviceName != null) {
            serviceName.setTitleI18N(OraConfigI18nKeys.CONFIG_ORACLE_SERVICE_LABEL);
            serviceName.setDescI18N("");
            serviceOption.addField(serviceName);
        }
        options.add(serviceOption);

        FieldOptionValueDef tnsOption = UiUtils.fieldOptionDef("TNS", OraConnectType.TNS.name());
        if (tnsAdmin != null) {
            tnsAdmin.setDescI18N("");
            tnsOption.addField(tnsAdmin);
        }
        if (tnsName != null) {
            tnsName.setDescI18N("");
            tnsOption.addField(tnsName);
        }
        options.add(tnsOption);
        connectType.setOptions(options);

        general.beforeAddField(connectType, DataSourceConfig.Fields.securityType);
    }

    protected UiPanelField hiddenField(String field) {
        return UiPanelField.builder().field(field).type(UiPanelFieldType.Input).hide(true).build();
    }

    public boolean supportSSL() {
        return false;
    }

    @Override
    public boolean supportSSH() {
        return true;
    }

    @Override
    public List<SecurityType> securityTypes() {
        List<SecurityType> options = new ArrayList<>();
        options.add(SecurityType.NONE);
        options.add(SecurityType.USER_PASSWD);
        return options;
    }
}
