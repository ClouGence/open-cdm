package com.clougence.drivers.testsupport;

import java.util.Properties;

import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;

public class TestDsFactory implements DsFactory<String> {

    @Override
    public DsObject<String> create(Properties dsConfig) {
        String suffix = dsConfig == null ? "null" : dsConfig.getProperty("name", "default");
        return new DsObject<>(dsConfig != null ? dsConfig : new Properties(), "factory-created-" + suffix, this);
    }
}