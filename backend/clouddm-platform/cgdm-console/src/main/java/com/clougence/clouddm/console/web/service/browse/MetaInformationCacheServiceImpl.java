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
package com.clougence.clouddm.console.web.service.browse;

import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsMetaDataDO;
import com.clougence.clouddm.platform.dal.model.datasource.MetaInformationType;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MetaInformationCacheServiceImpl implements MetaInformatinCacheService, UnifiedPostConstruct {
    @Resource
    private DataSourceDal               dsDal;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private final AtomicBoolean         inited = new AtomicBoolean();

    private boolean supportType(MetaInformationType type) {
        switch (type) {
            case KeyList:
            case KeyDetail:
                return false;
            default:
                return true;
        }
    }

    @Override
    public void putListCache(String puid, Long dsId, String catalog, String schema, MetaInformationType type, String context) {
        if (!this.supportType(type)) {
            return;
        }

        String path = getListPath(catalog, schema);
        dsDal.metaDataMapper().insertOrUpdate(puid, dsId, path, type, context);
    }

    @Override
    public String getListCache(String puid, Long dsId, String catalog, String schema, MetaInformationType type) {
        String path = getListPath(catalog, schema);
        DmDsMetaDataDO cacheDO = dsDal.metaDataMapper().queryCache(puid, dsId, path, type);
        if (cacheDO != null) {
            return cacheDO.getContext();
        }
        return null;
    }

    @Override
    public void putDetailCache(String puid, Long dsId, String catalog, String schema, MetaInformationType type, String objName, String context) {
        if (!this.supportType(type)) {
            return;
        }

        String path = getDetailPath(catalog, schema, objName);
        dsDal.metaDataMapper().insertOrUpdate(puid, dsId, path, type, context);
    }

    @Override
    public String getDetailCache(String puid, Long dsId, String catalog, String schema, MetaInformationType type, String objName) {
        String path = getDetailPath(catalog, schema, objName);
        DmDsMetaDataDO cacheDO = dsDal.metaDataMapper().queryCache(puid, dsId, path, type);
        if (cacheDO != null) {
            return cacheDO.getContext();
        }
        return null;
    }

    private String getDetailPath(String catalog, String schema, String objName) {
        StringBuilder path = new StringBuilder("/");
        if (StringUtils.isNotEmpty(catalog)) {
            path.append(catalog).append("/");
        }
        if (StringUtils.isNotEmpty(schema)) {
            path.append(schema).append("/");
        }
        path.append(objName);
        return path.toString();
    }

    private String getListPath(String catalog, String schema) {
        StringBuilder path = new StringBuilder("/");
        if (StringUtils.isNotEmpty(catalog)) {
            path.append(catalog).append("/");
        }
        if (StringUtils.isNotEmpty(schema)) {
            path.append(schema).append("/");
        }
        return path.toString();
    }

    @Override
    public void init() throws Exception {
        if (!this.inited.compareAndSet(false, true)) {
            return;
        }
        this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1,
            ThreadUtils.daemonThreadFactory(this.getClass().getClassLoader(), "meta-information-cache-delete-%s"));
        this.scheduledThreadPoolExecutor.scheduleWithFixedDelay(this::deleteTimeoutLog, 0, 5, TimeUnit.HOURS);
    }

    @Override
    public void stop() {

    }

    private void deleteTimeoutLog() {
        Date now = new Date();
        int day = 10;
        Date date = new Date(now.getTime() - (long) day * 24 * 60 * 60 * 1000);
        int deleteCount;
        do {
            deleteCount = dsDal.metaDataMapper().deleteBeforeDate(date);
        } while (deleteCount > 0);
    }
}
