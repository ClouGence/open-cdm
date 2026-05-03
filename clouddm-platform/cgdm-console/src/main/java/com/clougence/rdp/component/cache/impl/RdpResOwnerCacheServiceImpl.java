package com.clougence.rdp.component.cache.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.rdp.component.cache.RdpResOwnerCacheService;
import com.clougence.rdp.component.cache.model.DsCacheEntry;
import com.clougence.rdp.component.cache.model.UserCacheEntry;
import com.clougence.rdp.constant.ConsoleErrorCode;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpDsService;
import com.clougence.rdp.service.RdpUserService;
import com.clougence.rdp.util.NamedThreadFactory;
import com.clougence.utils.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2024/2/27 13:34:10
 */
@Service
@Slf4j
public class RdpResOwnerCacheServiceImpl implements RdpResOwnerCacheService, UnifiedPostConstruct {

    private final AtomicBoolean               running     = new AtomicBoolean(false);

    @Resource
    private RdpUserService                    rdpUserService;

    @Resource
    private RdpDsService                      rdpDsService;

    private final Map<String, UserCacheEntry> akUserCache = new ConcurrentHashMap<>();

    private final Map<Long, DsCacheEntry>     idDsCache   = new ConcurrentHashMap<>();

    @Override
    public void init() throws Exception {
        if (running.compareAndSet(false, true)) {
            ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("biz-local-cache-cleaner", true));
            cleaner.scheduleAtFixedRate(() -> {
                try {
                    // invalid user cache
                    Iterator<UserCacheEntry> uit = akUserCache.values().iterator();
                    List<String> rAks = new ArrayList<>();
                    while (uit.hasNext()) {
                        UserCacheEntry e = uit.next();
                        if (e.getAddTime().plusHours(1).isAfter(LocalDateTime.now())) {
                            rAks.add(e.getAk());
                        }
                    }

                    for (String ak : rAks) {
                        removeUserFromCache(ak);
                    }

                    // invalid ds cache
                    Iterator<DsCacheEntry> dsit = idDsCache.values().iterator();
                    List<Long> dsIds = new ArrayList<>();
                    while (dsit.hasNext()) {
                        DsCacheEntry e = dsit.next();
                        if (e.getAddTime().plusHours(1).isAfter(LocalDateTime.now())) {
                            dsIds.add(e.getDsNumId());
                        }
                    }

                    for (Long id : dsIds) {
                        removeDsFromCache(id);
                    }
                } catch (Throwable e) {
                    log.error(this.getClass().getSimpleName() + " error.msg:" + ExceptionUtils.getRootCauseMessage(e), e);
                }
            }, 60, 60, TimeUnit.SECONDS);
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public UserCacheEntry queryByUid(String uid) {
        UserCacheEntry result = queryUserCacheByUid(uid);

        if (result == null) {
            synchronized (akUserCache) {
                result = queryUserCacheByUid(uid);
                if (result == null) {
                    RdpUserDO userDO = rdpUserService.getUserByUid(uid);
                    result = fillUserToCache(userDO);
                }
            }
        }

        return result;
    }

    @Override
    public UserCacheEntry queryByAk(String ak) {
        UserCacheEntry result = akUserCache.get(ak);

        if (result == null) {
            synchronized (akUserCache) {
                result = akUserCache.get(ak);
                if (result == null) {
                    RdpUserDO userDO = rdpUserService.getUserByAk(ak);
                    result = fillUserToCache(userDO);
                }
            }
        }

        return result;
    }

    protected UserCacheEntry queryUserCacheByUid(String uid) {
        UserCacheEntry result = null;
        if (!akUserCache.isEmpty()) {
            for (UserCacheEntry entry : akUserCache.values()) {
                if (entry.getUid().equals(uid)) {
                    result = entry;
                }
            }
        }

        return result;
    }

    protected UserCacheEntry fillUserToCache(RdpUserDO userDO) {
        if (userDO == null) {
            return null;
        }

        UserCacheEntry entry = new UserCacheEntry();
        entry.setUid(userDO.getUid());
        entry.setUserNumId(userDO.getId());
        entry.setAk(userDO.getAccessKey());
        akUserCache.put(entry.getAk(), entry);

        return entry;
    }

    @Override
    public DsCacheEntry queryByDsId(Long dsId) {
        DsCacheEntry result = idDsCache.get(dsId);

        if (result == null) {
            synchronized (idDsCache) {
                result = idDsCache.get(dsId);
                if (result == null) {
                    RdpDataSourceDO dsDO = rdpDsService.fetchAndCheckById(dsId);
                    if (dsDO != null) {
                        result = new DsCacheEntry();
                        result.setDsNumId(dsDO.getId());
                        result.setOwnerUid(dsDO.getUid());
                        idDsCache.put(dsDO.getId(), result);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public void ownDataSource(String uid, Long dsId) {
        DsCacheEntry entry = this.queryByDsId(dsId);

        if (entry == null) {
            throw new IllegalArgumentException("DataSource (" + dsId + ") not exist.");
        }

        if (!entry.getOwnerUid().equals(uid)) {
            throw new ErrorMessageException(ConsoleErrorCode.NO_AUTHORITY_TO_OPERATE_ON_THIS_RESOURCE.name());
        }
    }

    protected void removeUserFromCache(String ak) {
        synchronized (akUserCache) {
            akUserCache.remove(ak);
        }
    }

    protected void removeDsFromCache(Long dsId) {
        synchronized (idDsCache) {
            idDsCache.remove(dsId);
        }
    }
}
