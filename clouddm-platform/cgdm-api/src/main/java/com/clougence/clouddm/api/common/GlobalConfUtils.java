package com.clougence.clouddm.api.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.HostUtil;
import com.clougence.utils.ResourcesUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.input.AutoCloseInputStream;

import lombok.extern.slf4j.Slf4j;

/**
 * Scan jar resource
 * @author noear 2020/11/17
 */
@Slf4j
public class GlobalConfUtils {

    public static final String              CONSOLE_HOST           = "clouddm.console.host";
    public static final String              CONSOLE_PORT           = "clouddm.console.port";
    public static final String              WORKER_SEQUENCE_NUMBER = "clouddm.worker.wsn";
    public static final String              CLOUGENCE_AK           = "clouddm.auth.ak";
    public static final String              CLOUGENCE_SK           = "clouddm.auth.sk";
    public static final String              CONF_PATH_SUFFIX       = "global_conf.properties";
    public static final Map<String, String> config                 = new HashMap<>();
    private static ConnAuthDTO              cacheAuth;

    public static String getAppHome() {
        String jarPath = System.getProperty("app.home");
        if (StringUtils.isBlank(jarPath)) {
            return System.getProperty("user.home") + File.separator + "clouddm";
        } else {
            return jarPath;
        }
    }

    public static String getAppDataHome() {
        String dataPath = System.getProperty("app.data");
        if (StringUtils.isBlank(dataPath)) {
            return System.getProperty("user.home") + File.separator + ".clougence" + File.separator + "clouddm";
        } else {
            return dataPath;
        }
    }

    public static String getUserDataHome() { return getAppDataHome() + File.separator + "userdata"; }

    public static String getTempDataHome() { return getAppDataHome() + File.separator + "temporary"; }

    public static String getLogHome() {
        String dataPath = System.getProperty("app.logPath");
        if (StringUtils.isBlank(dataPath)) {
            return getAppDataHome() + File.separator + "logs";
        } else {
            return dataPath;
        }
    }

    public static String getPluginDir(String moduleName) {
        if (StringUtils.isBlank(moduleName)) {
            throw new NullPointerException("moduleName is null.");
        }
        // 1.determine the extension directory
        String jarPath = getAppHome();
        log.info("plugin[" + moduleName + "] path app.home is " + jarPath);
        if (jarPath.endsWith(File.separator)) {
            jarPath = jarPath + moduleName;
        } else {
            jarPath = jarPath + File.separator + moduleName;
        }
        return jarPath;
    }

    public static ConnAuthDTO loadGlobalConf() throws IOException {
        if (cacheAuth != null) {
            return cacheAuth;
        }

        URL globalConfResource = null;
        String workerSequenceNumber = null;
        String accessKey = null;
        String securityKey = null;
        String consoleHost = null;
        String consolePort = null;

        if (StringUtils.equalsIgnoreCase(System.getProperty("app.mode"), "embedded")) {
            if (config.isEmpty()) {
                throw new IllegalArgumentException("embedded worker not configured.");
            }
            workerSequenceNumber = config.get(WORKER_SEQUENCE_NUMBER);
            accessKey = config.get(CLOUGENCE_AK);
            securityKey = config.get(CLOUGENCE_SK);
            consoleHost = config.get(CONSOLE_HOST);
            consolePort = config.get(CONSOLE_PORT);
        } else {
            File globalResourceFile = new File(getAppHome(), "conf" + File.separator + CONF_PATH_SUFFIX);
            Properties prop = new Properties();
            if (globalResourceFile.exists()) {
                globalConfResource = globalResourceFile.toURI().toURL();
                prop = getProperties(new AutoCloseInputStream(new FileInputStream(globalResourceFile)));
            } else {
                globalConfResource = ResourcesUtils.getResource(GlobalConfUtils.class.getClassLoader(), CONF_PATH_SUFFIX);
                prop = getProperties(new AutoCloseInputStream(ResourcesUtils.getResourceAsStream(globalConfResource)));
            }
            log.info("global conf path:" + globalConfResource);

            workerSequenceNumber = prop.getProperty(WORKER_SEQUENCE_NUMBER);
            accessKey = prop.getProperty(CLOUGENCE_AK);
            securityKey = prop.getProperty(CLOUGENCE_SK);
            consoleHost = prop.getProperty(CONSOLE_HOST);
            consolePort = prop.getProperty(CONSOLE_PORT);
        }

        ConnAuthDTO connAuthDTO = new ConnAuthDTO();
        connAuthDTO.setAk(accessKey);
        connAuthDTO.setSk(securityKey);
        connAuthDTO.setIp(HostUtil.getHostIp());
        connAuthDTO.setWsn(workerSequenceNumber);
        connAuthDTO.setConsoleHost(consoleHost);
        connAuthDTO.setConsolePort(consolePort);
        connAuthDTO.setSendAuthTimeMs(System.currentTimeMillis());
        connAuthDTO.setGlobalConfResource(globalConfResource);
        cacheAuth = connAuthDTO;
        return connAuthDTO;
    }

    private static Properties getProperties(InputStream globalConfResource) {
        try {
            Properties prop = new Properties();
            prop.load(globalConfResource);
            return prop;
        } catch (Exception e) {
            String msg = "load " + globalConfResource + " error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
