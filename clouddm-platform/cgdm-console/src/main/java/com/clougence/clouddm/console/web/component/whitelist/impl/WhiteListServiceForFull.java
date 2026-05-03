package com.clougence.clouddm.console.web.component.whitelist.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.console.web.component.whitelist.WhiteListService;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.rdp.global.config.user.UserDefinedConfig;
import com.clougence.rdp.global.exception.ErrorMessageException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WhiteListServiceForFull implements WhiteListService, DsFeatureIDs, UnifiedPostConstruct {

    private final AtomicBoolean        inited          = new AtomicBoolean();
    private final List<DataSourceType> whiteDs         = new ArrayList<>();
    private final Map<String, Range>   userConfigRange = new HashMap<>();

    @Override
    public void init() throws Exception {
        if (this.inited.compareAndSet(false, true)) {
            // config check
            this.userConfigRange.put(UserDefinedConfig.Fields.defaultColumnDisplayChars, new Range(10, 500));
            this.userConfigRange.put(UserDefinedConfig.Fields.onlineMaxRecordCount, new Range(-1, 1000000));
            this.userConfigRange.put(UserDefinedConfig.Fields.onlineMaxResultSetMegaByte, new Range(-1, 200));
            this.userConfigRange.put(UserDefinedConfig.Fields.onlineMaxColumnMegaByte, new Range(-1, 16));
            this.userConfigRange.put(UserDefinedConfig.Fields.onlineMaxElementMegaByte, new Range(-1, 16));

            // my
            this.whiteDs.add(DataSourceType.MySQL);
            this.whiteDs.add(DataSourceType.MariaDB);
            this.whiteDs.add(DataSourceType.TiDB);
            this.whiteDs.add(DataSourceType.AdbForMySQL);
            this.whiteDs.add(DataSourceType.OceanBase);
            this.whiteDs.add(DataSourceType.PolarDbX);
            this.whiteDs.add(DataSourceType.PolarDbMySQL);
            this.whiteDs.add(DataSourceType.Doris);
            this.whiteDs.add(DataSourceType.SelectDB);
            this.whiteDs.add(DataSourceType.StarRocks);
            this.whiteDs.add(DataSourceType.ClickHouse);
            this.whiteDs.add(DataSourceType.MongoDB);
            // pg
            this.whiteDs.add(DataSourceType.PostgreSQL);
            this.whiteDs.add(DataSourceType.Greenplum);
            this.whiteDs.add(DataSourceType.PolarDBPg);
            this.whiteDs.add(DataSourceType.GaussDBForOpenGauss);
            this.whiteDs.add(DataSourceType.GaussDB);
            //
            this.whiteDs.add(DataSourceType.SQLServer);
            // db2
            this.whiteDs.add(DataSourceType.Db2);
            this.whiteDs.add(DataSourceType.Db2Fori);
            // oracle
            this.whiteDs.add(DataSourceType.Oracle);
            this.whiteDs.add(DataSourceType.ObForOracle);
            // cloud for Aliyun
            this.whiteDs.add(DataSourceType.MaxCompute);
            this.whiteDs.add(DataSourceType.Hologres);
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean checkMenuQuery(String menuId) {
        return true;
    }

    @Override
    public boolean checkMenuManager(String menuId) {
        return true;
    }

    @Override
    public boolean checkMenuMaintenance(String menuId) {
        return true;
    }

    @Override
    public boolean checkDs(DataSourceType dsType) {
        return this.whiteDs.contains(dsType);
    }

    @Override
    public boolean checkChangeCatalog(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkChangeSchema(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkChangeIsolation(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkChangeAutoCommit(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkChangeReadOnly(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkCancelQuery(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkExplain(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkFormat(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkArgs(DataSourceType dsType) {
        return true;
    }

    @Override
    public boolean checkUserConfigNumber(String configKey, String configValue) {
        if (!this.userConfigRange.containsKey(configKey)) {
            return true;
        }

        Range range = this.userConfigRange.get(configKey);
        try {
            long value = Long.parseLong(configValue.trim());
            return value < range.getMin() || value > range.getMax();
        } catch (NumberFormatException e) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SYS_CONFIG_NEED_NUMBER_ERROR.name(), configKey, configValue));
        }
    }
}
