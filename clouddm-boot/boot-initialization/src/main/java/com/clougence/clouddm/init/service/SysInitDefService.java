package com.clougence.clouddm.init.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.init.model.InitFieldDef;
import com.clougence.rdp.util.RdpI18nUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.ResourcesUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 系统初始化核心服务。
 * - 合并 init-fields.json schema + classpath 运行时配置值
 * - DB 连接自检判断系统状态
 * - 应用配置（写入 classpath properties / 执行 Flyway 迁移）
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
            def.setLabel(labelKey != null ? RdpI18nUtils.getMessage(labelKey) : "");
            def.setDescription(descKey != null ? RdpI18nUtils.getMessage(descKey) : "");
            defs.add(def);
        }
        this.fieldDefsCache = defs;
    }

    public List<InitFieldDef> loadInitFieldDefs() {
        List<InitFieldDef> schema = getFieldDefsSchema();
        Properties runtimeProps = loadSystemProperties();

        List<InitFieldDef> result = new ArrayList<>();
        for (InitFieldDef def : schema) {
            InitFieldDef copy = new InitFieldDef();
            copy.setPropertyKey(def.getPropertyKey());
            copy.setCategory(def.getCategory());
            copy.setInputType(def.getInputType());
            copy.setRequired(def.isRequired());
            copy.setLabel(def.getLabel());
            copy.setDescription(def.getDescription());

            //
            String value = runtimeProps.getProperty(def.getPropertyKey());
            if (StringUtils.isNotBlank(value)) {
                copy.setDefaultValue(value);
            } else {
                copy.setDefaultValue("");
            }
            result.add(copy);
        }
        return result;
    }

    public List<InitFieldDef> getFieldDefsSchema() { return this.fieldDefsCache; }

    //

    /** 加载运行时配置（根据 app.mode 选择 alone 或 console 配置文件） */
    public Properties loadSystemProperties() {
        Properties props = new Properties();
        try {
            if (isAloneMode()) {
                loadInto(props, DEFAULT_ALONE_CONFIG);
                loadInto(props, ALONE_CONFIG);
            } else {
                loadInto(props, DEFAULT_CONSOLE_CONFIG);
                loadInto(props, CONSOLE_CONFIG);
            }
        } catch (Exception e) {
            log.error("[SysInitService] Failed to load runtime properties", e);
        }
        return props;
    }

    private void loadInto(Properties props, String resourcePath) throws IOException {
        Map<String, String> map = ResourcesUtils.getProperty(resourcePath);
        if (map != null) {
            props.putAll(map);
        }
    }

    /** 读取原始配置文件内容（供高级选项编辑）  */
    public String readRawConfigFile() throws IOException {
        String configName = isAloneMode() ? ALONE_CONFIG : CONSOLE_CONFIG;
        String defaultConfigName = isAloneMode() ? DEFAULT_ALONE_CONFIG : DEFAULT_CONSOLE_CONFIG;
        URL resource = ResourcesUtils.getResource(configName);
        if (resource != null && "file".equals(resource.getProtocol())) {
            try {
                return new String(Files.readAllBytes(Paths.get(resource.toURI())), StandardCharsets.UTF_8);
            } catch (Exception e) {
                throw new IOException("Failed to read config file: " + configName, e);
            }
        }
        // fallback: 优先读取主配置文件的 classpath 资源；若不存在，则回退到默认配置模板
        String content = readClasspathResource(configName);
        if (content != null) {
            return content;
        }

        content = readClasspathResource(defaultConfigName);
        if (content != null) {
            return content;
        }

        content = readFileSystemResource(configName);
        if (content != null) {
            return content;
        }

        content = readFileSystemResource(defaultConfigName);
        if (content != null) {
            return content;
        }

        Properties runtimeProps = loadSystemProperties();
        if (!runtimeProps.isEmpty()) {
            return serializeProperties(runtimeProps);
        }

        throw new IOException("Config resource not found: " + configName + " or " + defaultConfigName);
    }

    private String readClasspathResource(String resourcePath) throws IOException {
        try (InputStream inputStream = ResourcesUtils.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                return null;
            }
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }

    private String readFileSystemResource(String resourcePath) throws IOException {
        for (Path candidate : getFileSystemCandidates(resourcePath)) {
            if (Files.exists(candidate) && Files.isRegularFile(candidate)) {
                return Files.readString(candidate, StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    private List<Path> getFileSystemCandidates(String resourcePath) {
        Path currentDir = Paths.get(System.getProperty("user.dir", ".")).toAbsolutePath().normalize();
        Set<Path> candidates = new LinkedHashSet<>();

        for (Path baseDir = currentDir; baseDir != null; baseDir = baseDir.getParent()) {
            candidates.add(baseDir.resolve(resourcePath));
            candidates.add(baseDir.resolve("clouddm-boot/boot-console/src/main/resources").resolve(resourcePath));
            candidates.add(baseDir.resolve("clouddm-boot/boot-alone/src/main/resources").resolve(resourcePath));
            candidates.add(baseDir.resolve("clouddm-server/src/main/resources").resolve(resourcePath));
            candidates.add(baseDir.resolve("package/pkg/console").resolve(resourcePath));
            candidates.add(baseDir.resolve("package/pkg/alone").resolve(resourcePath));
        }

        return new ArrayList<>(candidates);
    }

    private String serializeProperties(Properties properties) throws IOException {
        try (StringWriter writer = new StringWriter()) {
            properties.store(writer, "Generated from runtime properties");
            return writer.toString();
        }
    }

    private boolean isAloneMode() { return "embedded".equals(System.getProperty("app.mode")); }
}
