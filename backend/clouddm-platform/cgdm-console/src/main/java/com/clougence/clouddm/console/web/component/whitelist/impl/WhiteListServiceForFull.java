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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.console.web.component.config.RootUserConfig;
import com.clougence.clouddm.console.web.component.whitelist.WhiteListService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.plugin.PluginManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WhiteListServiceForFull implements WhiteListService, DsFeatureIDs, UnifiedPostConstruct {

    private final AtomicBoolean      inited          = new AtomicBoolean();
    private final Map<String, Range> userConfigRange = new HashMap<>();

    @Override
    public void init() throws Exception {
        if (this.inited.compareAndSet(false, true)) {
            // config check
            this.userConfigRange.put(RootUserConfig.Fields.defaultColumnDisplayChars, new Range(10, 500));
            this.userConfigRange.put(RootUserConfig.Fields.onlineMaxRecordCount, new Range(1, 1000000));
            this.userConfigRange.put(RootUserConfig.Fields.onlineMaxResultSetMegaByte, new Range(4, 1024));
            this.userConfigRange.put(RootUserConfig.Fields.onlineMaxColumnMegaByte, new Range(1, 16));
            this.userConfigRange.put(RootUserConfig.Fields.onlineMaxElementMegaByte, new Range(1, 16));

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
        return PluginManager.findDsPlugin(dsType) != null;
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
            return range.getMin() <= value && value <= range.getMax();
        } catch (NumberFormatException e) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.SYS_CONFIG_NEED_NUMBER_ERROR.name(), configKey, configValue));
        }
    }
}
