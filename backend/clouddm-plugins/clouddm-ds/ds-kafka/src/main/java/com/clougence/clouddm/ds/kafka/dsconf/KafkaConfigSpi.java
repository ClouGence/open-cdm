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
package com.clougence.clouddm.ds.kafka.dsconf;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;
import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.strValueDef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ConfigI18nKey;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.ds.kafka.execute.jdbc.KafkaKeys;
import com.clougence.clouddm.ds.kafka.i18n.KafkaConfigI18nKeys;
import com.clougence.clouddm.dsfamily.dsconf.AbstractDsConfigSpi;
import com.clougence.drivers.adapter.ConvertUtils;
import com.clougence.utils.StringUtils;

public class KafkaConfigSpi extends AbstractDsConfigSpi {

    @Override
    public String defaultPort() {
        return String.valueOf(KafkaKeys.DEFAULT_PORT);
    }

    @Override
    public Class<? extends DataSourceConfig> newConfig() {
        return KafkaConfig.class;
    }

    @Override
    public DataSourceConfig fillConfig(DataSourceConfig dsConfig, Map<String, String> defaultConfig) {
        KafkaConfig config = (KafkaConfig) dsConfig;
        Long connectTimeoutMs = ConvertUtils.toLong(defaultConfig.get(KafkaConfig.Fields.connectTimeoutMs), false);
        Integer soTimeoutSec = ConvertUtils.toInteger(defaultConfig.get(KafkaConfig.Fields.soTimeoutSec), false);
        String defaultSchema = defaultConfig.get(KafkaConfig.Fields.defaultSchema);
        config.setDefaultSchema(StringUtils.defaultIfBlank(defaultSchema, KafkaKeys.DEFAULT_SCHEMA));
        config.setConnectTimeoutMs(connectTimeoutMs == null ? 5000L : connectTimeoutMs);
        config.setSoTimeoutSec(soTimeoutSec == null ? 30 : soTimeoutSec);
        if (config.getSecurityType() == null) {
            config.setSecurityType(SecurityType.NONE);
        }
        return dsConfig;
    }

    @Override
    public List<SecurityType> securityTypes() {
        List<SecurityType> options = new ArrayList<>();
        options.add(SecurityType.NONE);
        options.add(SecurityType.USER_PASSWD);
        return options;
    }

    @Override
    public void customizePanels(Map<DsConfigGroup, UiPanel> panels) {
        UiPanel general = panels.get(DsConfigGroup.GENERAL);
        if (general == null) {
            return;
        }

        UiPanelField securityType = general.findField(DataSourceConfig.Fields.securityType);
        if (securityType == null) {
            return;
        }

        UiPanelField userName = UiPanelField.builder()
            .field(DataSourceConfig.Fields.userName)
            .type(UiPanelFieldType.Input)
            .titleI18N(ConfigI18nKey.CONFIG_RDB_USERNAME_LABEL)
            .require(false)
            .build();
        UiPanelField password = UiPanelField.builder()
            .field(DataSourceConfig.Fields.password)
            .type(UiPanelFieldType.Password)
            .titleI18N(ConfigI18nKey.CONFIG_RDB_PASSWORD_LABEL)
            .require(false)
            .build();

        List<ValueDef> options = new ArrayList<>();
        options.add(fieldOptionDef(KafkaConfigI18nKeys.CONFIG_KAFKA_SECURITY_PROTOCOL_PLAINTEXT, SecurityType.NONE.name()));
        options.add(fieldOptionDef(KafkaConfigI18nKeys.CONFIG_KAFKA_SECURITY_PROTOCOL_SASL_PLAINTEXT, SecurityType.USER_PASSWD.name())
            .addField(userName)
            .addField(password));
        securityType.setOptions(options);
        if (securityType.getDefaultValue() == null || securityType.getDefaultValue().asValue() == null
            || StringUtils.isBlank(String.valueOf(securityType.getDefaultValue().asValue()))) {
            securityType.setDefaultValue(strValueDef(SecurityType.NONE.name()));
        }
    }

    @Override
    public List<SslMode> sslModeSet() {
        return Collections.emptyList();
    }

    @Override
    public boolean supportTx() {
        return false;
    }

    @Override
    public boolean supportSSL() {
        return false;
    }

    @Override
    public boolean supportSSH() {
        return true;
    }
}
