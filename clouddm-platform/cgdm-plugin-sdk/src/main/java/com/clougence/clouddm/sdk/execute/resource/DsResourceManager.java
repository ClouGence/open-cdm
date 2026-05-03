package com.clougence.clouddm.sdk.execute.resource;

import java.io.IOException;
import java.util.concurrent.Executor;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityFileType;
import com.clougence.drivers.DsObject;
import com.clougence.utils.timer.Timer;

public interface DsResourceManager {

    <C extends DataSourceConfig> Timer getTimer(C dbConfig);

    <C extends DataSourceConfig> Executor getExecutor(C dbConfig);

    <C extends DataSourceConfig, T extends AutoCloseable> DsObject<T> requestResource(C dbConfig) throws Exception;

    boolean isTask();

    boolean isReady();

    String getSecurityFilePath(DataSourceConfig config, String fileName, SecurityFileType fileType) throws IOException;
}
