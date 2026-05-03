package com.clougence.drivers.factory;

import com.clougence.utils.StringUtils;

import lombok.Getter;

@Getter
public final class DsFactoryDef {

    private final String      dsFactoryName;
    private final ClassLoader dsFactoryClassLoader;

    public DsFactoryDef(String dsFactoryName, ClassLoader dsFactoryClassLoader){
        this.dsFactoryName = StringUtils.trimToNull(dsFactoryName);
        this.dsFactoryClassLoader = dsFactoryClassLoader;
    }
}
