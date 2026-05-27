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
package com.clougence.clouddm.console.web.component.whitelist.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.console.web.component.whitelist.WhiteListService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.rdp.global.config.user.UserDefinedConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WhiteListServiceForBasic implements WhiteListService, DsFeatureIDs, UnifiedPostConstruct {

    private final AtomicBoolean        inited              = new AtomicBoolean();
    private final List<String>         forQueryMenus       = new ArrayList<>(); // always
    private final List<String>         forManagerMenus     = new ArrayList<>(); // need Auth
    private final List<String>         forMaintenanceMenus = new ArrayList<>(); // need Auth
    private final List<DataSourceType> whiteDs             = new ArrayList<>();
    private final Map<String, Range>   userConfigRange     = new HashMap<>();

    @Override
    public void init() throws Exception {
        if (this.inited.compareAndSet(false, true)) {
            // config check
            this.userConfigRange.put(UserDefinedConfig.Fields.defaultColumnDisplayChars, new Range(10, 500));
            this.userConfigRange.put(UserDefinedConfig.Fields.onlineMaxRecordCount, new Range(1, 1000000));
            this.userConfigRange.put(UserDefinedConfig.Fields.onlineMaxResultSetMegaByte, new Range(4, 1024));
            this.userConfigRange.put(UserDefinedConfig.Fields.onlineMaxColumnMegaByte, new Range(1, 16));
            this.userConfigRange.put(UserDefinedConfig.Fields.onlineMaxElementMegaByte, new Range(1, 16));

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
            this.whiteDs.add(DataSourceType.MaxCompute);
            this.whiteDs.add(DataSourceType.StarRocks);
            this.whiteDs.add(DataSourceType.ClickHouse);
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
            // other
            this.whiteDs.add(DataSourceType.Redis);
            this.whiteDs.add(DataSourceType.MongoDB);

            // commons
            this.addMenu(MENU_SEPARATOR, true, true, true);
            this.addMenu(MENU_BROWSE_CONSOLE, true, true, true);
            this.addMenu(MENU_BROWSE_REFRESH, true, true, true);
            this.addMenu(MENU_BROWSE_COPY_NAME, true, true, true);
            this.addMenu(MENU_BROWSE_COPY_JDBC, true, true, true);
            this.addMenu(MENU_BROWSE_PERMISSIONS, true, true, true);
            //
            this.addMenu(MENU_BROWSE_INSTANCE_CREATE, false, false, false); // team can not open.
            this.addMenu(MENU_BROWSE_INSTANCE_DROP, false, false, false);   // team can not open.
            this.addMenu(MENU_BROWSE_INSTANCE_RENAME, false, false, false); // team can not open.
            //
            this.addMenu(MENU_BROWSE_CATALOG_CREATE, false, false, true);
            this.addMenu(MENU_BROWSE_CATALOG_DROP, false, false, true);
            this.addMenu(MENU_BROWSE_CATALOG_RENAME, false, false, true);
            //
            this.addMenu(MENU_BROWSE_SCHEMA_CREATE, false, false, true);
            this.addMenu(MENU_BROWSE_SCHEMA_DROP, false, false, true);
            this.addMenu(MENU_BROWSE_SCHEMA_RENAME, false, false, true);
            //
            this.addMenu(MENU_BROWSE_TABLE_CREATE, false, true, true);
            this.addMenu(MENU_BROWSE_TABLE_ALTER, false, true, true);
            this.addMenu(MENU_BROWSE_TABLE_DROP, false, true, true);
            this.addMenu(MENU_BROWSE_TABLE_RENAME, false, true, true);
            this.addMenu(MENU_BROWSE_TABLE_TRUNCATE, false, true, true);
            //this.addMenu(MENU_BROWSE_TABLE_DATA, true, true, true);
            //this.addMenu(MENU_BROWSE_TABLE_FAKER, false, false, false);
            //this.addMenu(MENU_BROWSE_TABLE_FAKER_INCREMENT, false, false, false);
            //this.addMenu(MENU_BROWSE_TABLE_IMPORT, false, false, false);
            //this.addMenu(MENU_BROWSE_TABLE_EXPORT, false, false, false);
            this.addMenu(MENU_BROWSE_TABLE_REQUEST, true, true, true);
            this.addMenu(MENU_BROWSE_TABLE_GENERATE, true, true, true);
            this.addMenu(MENU_BROWSE_TABLE_GET_DDL, true, true, true);
            //
            this.addMenu(MENU_BROWSE_VIEW_CREATE, false, true, true);
            this.addMenu(MENU_BROWSE_VIEW_ALTER, false, true, true);
            this.addMenu(MENU_BROWSE_VIEW_DROP, false, true, true);
            this.addMenu(MENU_BROWSE_VIEW_RENAME, false, true, true);
            //this.addMenu(MENU_BROWSE_VIEW_DATA, true, true, true);
            //this.addMenu(MENU_BROWSE_VIEW_EXPORT, true, true, true);
            this.addMenu(MENU_BROWSE_VIEW_REQUEST, false, true, true);
            this.addMenu(MENU_BROWSE_VIEW_COMPILE, false, true, true);
            //
            this.addMenu(MENU_BROWSE_DBLINK_CREATE, false, false, true);
            this.addMenu(MENU_BROWSE_DBLINK_DROP, false, false, true);
            this.addMenu(MENU_BROWSE_DBLINK_TEST, true, true, true);
            //
            this.addMenu(MENU_BROWSE_JOB_CREATE, false, true, true);
            this.addMenu(MENU_BROWSE_JOB_ALTER, false, true, true);
            this.addMenu(MENU_BROWSE_JOB_DROP, false, true, true);
            this.addMenu(MENU_BROWSE_JOB_DISABLE, false, true, true);
            this.addMenu(MENU_BROWSE_JOB_ENABLE, false, true, true);
            this.addMenu(MENU_BROWSE_JOB_RUN, false, true, true);
            this.addMenu(MENU_BROWSE_PROPERTY, true, true, true);
            //
            this.addMenu(MENU_BROWSE_SCHEDULE_CREATE, false, true, true);
            this.addMenu(MENU_BROWSE_SCHEDULE_DROP, false, true, true);
            this.addMenu(MENU_BROWSE_SCHEDULE_DISABLE, false, true, true);
            this.addMenu(MENU_BROWSE_SCHEDULE_ENABLE, false, true, true);
            this.addMenu(MENU_BROWSE_SCHEDULE_RUN, false, true, true);
            //
            this.addMenu(MENU_BROWSE_MATERIALIZED_REQUEST, false, true, true);
            //
            this.addMenu(MENU_BROWSE_COLUMN_CREATE, false, true, true);
            this.addMenu(MENU_BROWSE_COLUMN_RENAME, false, true, true);
            this.addMenu(MENU_BROWSE_COLUMN_ALTER, false, true, true);
            this.addMenu(MENU_BROWSE_COLUMN_TRUNCATE, false, true, true);
            this.addMenu(MENU_BROWSE_COLUMN_DROP, false, true, true);
            this.addMenu(MENU_BROWSE_COLUMN_GENERATE, false, true, true);
            //
            this.addMenu(MENU_BROWSE_PRIMARY_CREATE, false, true, true);
            this.addMenu(MENU_BROWSE_PRIMARY_RENAME, false, true, true);
            this.addMenu(MENU_BROWSE_PRIMARY_ALTER, false, true, true);
            this.addMenu(MENU_BROWSE_PRIMARY_DROP, false, true, true);
            this.addMenu(MENU_BROWSE_PRIMARY_GENERATE, false, true, true);
            //
            this.addMenu(MENU_BROWSE_INDEX_CREATE, false, true, true);
            this.addMenu(MENU_BROWSE_INDEX_RENAME, false, true, true);
            this.addMenu(MENU_BROWSE_INDEX_ALTER, false, true, true);
            this.addMenu(MENU_BROWSE_INDEX_DROP, false, true, true);
            this.addMenu(MENU_BROWSE_INDEX_GENERATE, false, true, true);
            //
            this.addMenu(MENU_BROWSE_PARTITION_CREATE, false, true, true);
            this.addMenu(MENU_BROWSE_PARTITION_ALTER, false, true, true);
            this.addMenu(MENU_BROWSE_PARTITION_DROP, false, true, true);
            this.addMenu(MENU_BROWSE_PARTITION_GENERATE, false, true, true);
            //
            this.addMenu(MENU_BROWSE_KEY_CREATE, false, true, true);
            this.addMenu(MENU_BROWSE_KEY_DROP, false, true, true);
            this.addMenu(MENU_BROWSE_KEY_ALTER, false, true, true);
            this.addMenu(MENU_BROWSE_KEY_RENAME, false, true, true);
            //this.addMenu(MENU_BROWSE_KEY_IMPORT, false, true, true);
            //this.addMenu(MENU_BROWSE_KEY_EXPORT, false, true, true);
            //
            this.addMenu(MENU_BROWSE_TRIGGER_CREATE, false, true, true);
            this.addMenu(MENU_BROWSE_TRIGGER_DROP, false, true, true);
            this.addMenu(MENU_BROWSE_TRIGGER_ALTER, false, true, true);
            this.addMenu(MENU_BROWSE_TRIGGER_COMPILE, false, true, true);
            this.addMenu(MENU_BROWSE_TRIGGER_REQUEST, false, true, true);
            //
            this.addMenu(MENU_BROWSE_PROCEDURE_DROP, false, true, true);
            this.addMenu(MENU_BROWSE_PROCEDURE_CREATE, false, true, true);
            this.addMenu(MENU_BROWSE_PROCEDURE_ALTER, false, true, true);
            this.addMenu(MENU_BROWSE_PROCEDURE_COMPILE, false, true, true);
            this.addMenu(MENU_BROWSE_PROCEDURE_REQUEST, false, true, true);
            //
            this.addMenu(MENU_BROWSE_SEQUENCE_DROP, false, true, true);
            //
            this.addMenu(MENU_BROWSE_SYNONYM_DROP, false, true, true);
            this.addMenu(MENU_BROWSE_SYNONYM_REQUEST, false, true, true);
            //
            this.addMenu(MENU_BROWSE_MATERIALIZED_DROP, false, true, true);
            //
            this.addMenu(MENU_BROWSE_FUNCTION_DROP, false, true, true);
            this.addMenu(MENU_BROWSE_FUNCTION_ALTER, false, true, true);
            this.addMenu(MENU_BROWSE_FUNCTION_CREATE, false, true, true);
            this.addMenu(MENU_BROWSE_FUNCTION_COMPILE, false, true, true);
            this.addMenu(MENU_BROWSE_FUNCTION_REQUEST, false, true, true);
            //
            this.addMenu(MENU_BROWSE_CONSTRAINT_ENABLE, false, true, true);
            this.addMenu(MENU_BROWSE_CONSTRAINT_DISABLE, false, true, true);
            //
            this.addMenu(MENU_BROWSE_USER_DROP, false, false, true);
            this.addMenu(MENU_BROWSE_USER_CREATE, false, false, true);
        }
    }

    @Override
    public void stop() {

    }

    private void addMenu(String menuId, boolean inQuery, boolean inManager, boolean inMaintenance) {
        if (inQuery) {
            this.forQueryMenus.add(menuId);
        }
        if (inManager) {
            this.forManagerMenus.add(menuId);
        }
        if (inMaintenance) {
            this.forMaintenanceMenus.add(menuId);
        }
    }

    @Override
    public boolean checkMenuQuery(String menuId) {
        return this.forQueryMenus.contains(menuId);
    }

    @Override
    public boolean checkMenuManager(String menuId) {
        return this.forManagerMenus.contains(menuId);
    }

    @Override
    public boolean checkMenuMaintenance(String menuId) {
        return this.forMaintenanceMenus.contains(menuId);
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
        return false;
    }

    @Override
    public boolean checkChangeAutoCommit(DataSourceType dsType) {
        return false;
    }

    @Override
    public boolean checkChangeReadOnly(DataSourceType dsType) {
        return false;
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
            return range.getMin() <= value && value <= range.getMax();
        } catch (NumberFormatException e) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SYS_CONFIG_NEED_NUMBER_ERROR.name(), configKey, configValue));
        }
    }
}
