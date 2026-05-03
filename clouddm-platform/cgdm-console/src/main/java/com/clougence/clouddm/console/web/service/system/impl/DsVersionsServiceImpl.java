package com.clougence.clouddm.console.web.service.system.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DsVersionsServiceImpl {

    private Object buildInfo() {
        Properties properties = new Properties();
        try {
            InputStream asStream = DsVersionsServiceImpl.class.getClassLoader().getResourceAsStream("build-info.properties");
            if (asStream != null) {
                properties.load(asStream);
            }
        } catch (Exception ignore) {
            // ignore
        }

        if (properties.isEmpty()) {
            properties.put("mainVersion", "unknown");
            properties.put("time", "unknown");
        }
        return properties;
    }

    public Map<String, Object> fetchDsVersions() {
        Map<String, Object> allVersions = new HashMap<>();

        allVersions.put("buildInfo", buildInfo());

        return allVersions;
    }

}
