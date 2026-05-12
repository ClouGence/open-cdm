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
package com.clougence.clouddm.init.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.init.constant.InitSeedConstants;
import com.clougence.clouddm.init.model.InitFieldDef;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.utils.HostUtil;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.ResourcesUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides initialization field definitions and default property values for the initialization UI.
 * It combines field metadata from init-fields.json with property defaults resolved from classpath and app-home configuration files.
 */
@Slf4j
@Service
public class SysInitDefService {

    private static final String ALONE_CONFIG           = "alone.properties";
    private static final String DEFAULT_ALONE_CONFIG   = "default_alone.properties";
    private static final String CONSOLE_CONFIG         = "console.properties";
    private static final String DEFAULT_CONSOLE_CONFIG = "default_console.properties";
    private static final String INIT_FIELDS_JSON       = "config/init-fields.json";
    private List<InitFieldDef>  fieldDefsCache;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = JsonUtils.defaultObjectMapper();
        String json = IOUtils.toString(ResourcesUtils.getResourceAsStream(INIT_FIELDS_JSON), StandardCharsets.UTF_8);
        Map<String, Object> root = mapper.readValue(json, Map.class);
        List<Map<String, Object>> fields = (List<Map<String, Object>>) root.get("fields");

        List<InitFieldDef> defs = new ArrayList<>();
        for (Map<String, Object> f : fields) {
            InitFieldDef def = new InitFieldDef();
            def.setPropertyKey((String) f.get("propertyKey"));
            def.setCategory((String) f.get("category"));
            def.setInputType((String) f.get("inputType"));
            def.setRequired(Boolean.TRUE.equals(f.get("required")));
            String labelKey = (String) f.get("labelKey");
            String descKey = (String) f.get("descriptionKey");
            def.setLabel(labelKey != null ? DmI18nUtils.getMessage(labelKey) : "");
            def.setDescription(descKey != null ? DmI18nUtils.getMessage(descKey) : "");
            defs.add(def);
        }
        this.fieldDefsCache = defs;
    }

    public List<InitFieldDef> loadInitFieldDefs() {
        List<InitFieldDef> schema = getFieldDefsSchema();
        Properties runtimeProps = loadDefaultConfigProperties();
        Properties initDefaults = buildInitDefaultProperties();

        List<InitFieldDef> result = new ArrayList<>();
        for (InitFieldDef def : schema) {
            InitFieldDef copy = new InitFieldDef();
            copy.setPropertyKey(def.getPropertyKey());
            copy.setCategory(def.getCategory());
            copy.setInputType(def.getInputType());
            copy.setRequired(def.isRequired());
            copy.setLabel(def.getLabel());
            copy.setDescription(def.getDescription());

            String value = runtimeProps.getProperty(def.getPropertyKey());
            if (StringUtils.isBlank(value)) {
                value = initDefaults.getProperty(def.getPropertyKey());
            }
            copy.setDefaultValue(StringUtils.defaultString(value));
            result.add(copy);
        }
        return result;
    }

    public List<InitFieldDef> getFieldDefsSchema() { return this.fieldDefsCache; }

    /**
     * Loads runtime properties, choosing the alone or console configuration set according to app.mode.
     */
    public Properties loadSystemProperties() {
        Properties props = new Properties();
        try {
            if (isAloneMode()) {
                loadRuntimeProperties(props, DEFAULT_ALONE_CONFIG, ALONE_CONFIG);
            } else {
                loadRuntimeProperties(props, DEFAULT_CONSOLE_CONFIG, CONSOLE_CONFIG);
            }
            overlaySystemProperties(props);
        } catch (Exception e) {
            log.error("[SysInitService] Failed to load runtime properties", e);
        }
        return props;
    }

    private Properties loadDefaultConfigProperties() {
        Properties props = new Properties();
        try {
            if (isAloneMode()) {
                loadClasspathProperties(props, DEFAULT_ALONE_CONFIG);
                if (hasExplicitAppHome()) {
                    loadAppHomeProperties(props, ALONE_CONFIG);
                }
            } else {
                loadClasspathProperties(props, DEFAULT_CONSOLE_CONFIG);
                if (hasExplicitAppHome()) {
                    loadAppHomeProperties(props, CONSOLE_CONFIG);
                }
            }
            overlaySystemProperties(props);
        } catch (Exception e) {
            log.error("[SysInitService] Failed to load default config properties", e);
        }
        return props;
    }

    private Properties buildInitDefaultProperties() {
        Properties props = new Properties();
        props.setProperty("spring.datasource.username", "");
        props.setProperty("spring.datasource.password", "");
        props.setProperty("clougence.rdp.product.trial.verify_code", "777777");
        props.setProperty("clougence.init.admin.email", InitSeedConstants.DEFAULT_PRIMARY_EMAIL);
        props.setProperty("server.port", "8222");
        props.setProperty("clouddm.rsocket.dns", resolveDefaultHostIp());
        props.setProperty("clouddm.rsocket.console.port", "8008");
        return props;
    }

    private String resolveDefaultHostIp() {
        try {
            String hostIp = HostUtil.getHostIp();
            return StringUtils.isBlank(hostIp) ? "127.0.0.1" : hostIp;
        } catch (Exception e) {
            log.warn("[SysInitService] Failed to resolve host ip for init default config", e);
            return "127.0.0.1";
        }
    }

    private void loadRuntimeProperties(Properties props, String defaultConfigName, String runtimeConfigName) throws IOException {
        loadClasspathProperties(props, defaultConfigName);
        if (hasExplicitAppHome()) {
            loadAppHomeProperties(props, runtimeConfigName);
        } else {
            loadClasspathProperties(props, runtimeConfigName);
        }
    }

    private void loadClasspathProperties(Properties props, String resourcePath) throws IOException {
        Map<String, String> map = ResourcesUtils.getProperty(resourcePath);
        if (map != null) {
            props.putAll(map);
        }
    }

    private void loadAppHomeProperties(Properties props, String configName) throws IOException {
        Path configPath = Paths.get(GlobalConfUtils.getAppHome(), "conf", configName);
        if (!Files.exists(configPath)) {
            return;
        }

        try (InputStream input = Files.newInputStream(configPath)) {
            props.load(input);
        }
    }

    private void overlaySystemProperties(Properties props) {
        System.getProperties().forEach((key, value) -> {
            if (key instanceof String && value != null) {
                props.setProperty((String) key, String.valueOf(value));
            }
        });
    }

    private boolean hasExplicitAppHome() {
        return StringUtils.isNotBlank(System.getProperty("app.home"));
    }

    private boolean isAloneMode() { return "embedded".equals(System.getProperty("app.mode")); }
}
