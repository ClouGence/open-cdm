package com.clougence.drivers;

import java.util.Properties;

/**
 * @author mode 2020/10/31 12:05
 */
public interface DsFactory<T> {

    DsObject<T> create(Properties dsConfig) throws Exception;
}
