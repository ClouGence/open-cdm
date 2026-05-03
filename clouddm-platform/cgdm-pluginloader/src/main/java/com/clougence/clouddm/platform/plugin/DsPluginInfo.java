package com.clougence.clouddm.platform.plugin;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.execute.session.SessionFactory;
import com.clougence.drivers.DsFactory;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.provider.SqlBuilder;

public interface DsPluginInfo extends PluginInfo {

    DataSourceType getDsType();

    SqlBuilder getDsSqlBuilder();

    Dialect getDsDialect();

    List<String> getBindDrivers();

    //

    DsFactory<?> createDriver(String driverFamily, String driverVer) throws Exception;

    SessionFactory<?> createSessionFactory(String driverFamily, String driverVer) throws Exception;
}
