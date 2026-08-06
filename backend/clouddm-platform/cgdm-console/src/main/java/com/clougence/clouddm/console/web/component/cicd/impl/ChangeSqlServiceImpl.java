/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.console.web.component.cicd.impl;

import java.nio.file.*;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.cicd.CicdSqlFileUtils;
import com.clougence.clouddm.console.web.component.cicd.ChangeSqlService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.access.ChangeFlowDal;
import com.clougence.clouddm.platform.dal.model.cicd.ChangeItemType;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeDO;
import com.clougence.utils.function.EFunction;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChangeSqlServiceImpl implements ChangeSqlService {

    @Resource
    private ChangeFlowDal changeFlowDal;

    @Override
    public <T> T consumeSqlFile(long changeId, EFunction<Path, T, Exception> consumer) {
        DmChangeDO change = this.changeFlowDal.changeMapper().queryChangeById(changeId);
        if (change == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_CHANGE_NOT_EXIST_ERROR.name()));
        }

        Path sqlFile = CicdSqlFileUtils.cacheFile(change);
        if (!Files.isRegularFile(sqlFile)) {
            sqlFile = this.prepareSqlFile(change, sqlFile);
        }

        try {
            return consumer.eApply(sqlFile);
        } catch (ErrorMessageException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("consume change SQL file failed, changeId: " + changeId, e);
        }
    }

    private Path prepareSqlFile(DmChangeDO change, Path target) {
        try {
            Files.createDirectories(target.getParent());
            Path staging = target.resolveSibling(target.getFileName() + ".tmp");
            try {
                try (var output = Files.newOutputStream(staging, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                    this.changeFlowDal.readChangeItemContent(change.getOwnerUid(), change.getId(), ChangeItemType.REVIEW, output);
                }

                try {
                    Files.move(staging, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
                } catch (AtomicMoveNotSupportedException e) {
                    Files.move(staging, target, StandardCopyOption.REPLACE_EXISTING);
                }
            } finally {
                Files.deleteIfExists(staging);
            }
            return target;
        } catch (Exception e) {
            log.error("prepare change SQL file failed, changeId: " + change.getId(), e);
            throw new IllegalStateException("prepare change SQL file failed, changeId: " + change.getId(), e);
        }
    }
}
