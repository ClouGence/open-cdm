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
package com.clougence.clouddm.console.web.component.whitelist;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ui.DsFeatureIDs;
import com.clougence.clouddm.console.web.component.whitelist.impl.WhiteListServiceForBasic;
import com.clougence.clouddm.console.web.component.whitelist.impl.WhiteListServiceForFull;
import com.clougence.clouddm.console.web.constants.DmModeFeatured;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.utils.ExceptionUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Primary
@Service
public class WhiteListServiceImpl implements WhiteListService, DsFeatureIDs, UnifiedPostConstruct {

    @Resource
    private DmConsoleConfig                       dmConfig;
    @Resource
    private ApplicationContext                    appContext;

    private Map<DmModeFeatured, WhiteListService> forFeatured;
    private final AtomicBoolean                   inited = new AtomicBoolean();

    @Override
    public void init() {
        if (this.inited.compareAndSet(false, true)) {
            this.forFeatured = new HashMap<>();
            this.forFeatured.put(DmModeFeatured.basic, this.appContext.getBean(WhiteListServiceForBasic.class));
            this.forFeatured.put(DmModeFeatured.full, this.appContext.getBean(WhiteListServiceForFull.class));

            for (WhiteListService service : this.forFeatured.values()) {
                try {
                    ((UnifiedPostConstruct) service).init();
                } catch (Exception e) {
                    throw ExceptionUtils.toRuntime(e);
                }
            }
        }
    }

    @Override
    public void stop() {

    }

    private WhiteListService onFeatured() {
        if (forFeatured == null) {
            this.init();
        }

        if (this.forFeatured.containsKey(this.dmConfig.getDmModeFeatured())) {
            return this.forFeatured.get(this.dmConfig.getDmModeFeatured());
        } else {
            return this.forFeatured.get(DmModeFeatured.basic);
        }
    }

    @Override
    public boolean checkMenuQuery(String menuId) {
        return this.onFeatured().checkMenuQuery(menuId);
    }

    @Override
    public boolean checkMenuManager(String menuId) {
        return this.onFeatured().checkMenuManager(menuId);
    }

    @Override
    public boolean checkMenuMaintenance(String menuId) {
        return this.onFeatured().checkMenuMaintenance(menuId);
    }

    @Override
    public boolean checkDs(DataSourceType dsType) {
        return this.onFeatured().checkDs(dsType);
    }

    @Override
    public boolean checkChangeCatalog(DataSourceType dsType) {
        return this.onFeatured().checkChangeCatalog(dsType);
    }

    @Override
    public boolean checkChangeSchema(DataSourceType dsType) {
        return this.onFeatured().checkChangeSchema(dsType);
    }

    @Override
    public boolean checkChangeIsolation(DataSourceType dsType) {
        return this.onFeatured().checkChangeIsolation(dsType);
    }

    @Override
    public boolean checkChangeAutoCommit(DataSourceType dsType) {
        return this.onFeatured().checkChangeAutoCommit(dsType);
    }

    @Override
    public boolean checkChangeReadOnly(DataSourceType dsType) {
        return this.onFeatured().checkChangeReadOnly(dsType);
    }

    @Override
    public boolean checkCancelQuery(DataSourceType dsType) {
        return this.onFeatured().checkCancelQuery(dsType);
    }

    @Override
    public boolean checkExplain(DataSourceType dsType) {
        return this.onFeatured().checkExplain(dsType);
    }

    @Override
    public boolean checkFormat(DataSourceType dsType) {
        return this.onFeatured().checkFormat(dsType);
    }

    @Override
    public boolean checkArgs(DataSourceType dsType) {
        return this.onFeatured().checkArgs(dsType);
    }

    @Override
    public boolean checkUserConfigNumber(String configKey, String configValue) {
        return this.onFeatured().checkUserConfigNumber(configKey, configValue);
    }
}
