package com.clougence.clouddm.console.web.service.browse.impl;

import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.dal.enumeration.MetaInformationType;
import com.clougence.clouddm.console.web.dal.mapper.DmMetaInformationCacheMapper;
import com.clougence.clouddm.console.web.dal.model.DmMetaInformationCacheDO;
import com.clougence.clouddm.console.web.service.browse.MetaInformatinCacheService;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MetaInformationCacheServiceImpl implements MetaInformatinCacheService, UnifiedPostConstruct {

    @Resource
    private DmMetaInformationCacheMapper cacheMapper;

    private ScheduledThreadPoolExecutor  scheduledThreadPoolExecutor;

    private final AtomicBoolean          inited = new AtomicBoolean();

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
        cacheMapper.insertOrUpdate(puid, dsId, path, type, context);
    }

    @Override
    public String getListCache(String puid, Long dsId, String catalog, String schema, MetaInformationType type) {
        String path = getListPath(catalog, schema);
        DmMetaInformationCacheDO cacheDO = cacheMapper.queryCache(puid, dsId, path, type);
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
        cacheMapper.insertOrUpdate(puid, dsId, path, type, context);
    }

    @Override
    public String getDetailCache(String puid, Long dsId, String catalog, String schema, MetaInformationType type, String objName) {
        String path = getDetailPath(catalog, schema, objName);
        DmMetaInformationCacheDO cacheDO = cacheMapper.queryCache(puid, dsId, path, type);
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
            deleteCount = cacheMapper.deleteBeforeDate(date);
        } while (deleteCount > 0);
    }
}
