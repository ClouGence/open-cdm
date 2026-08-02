/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.console.web.component.approval.impl;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.approval.ApprovalService;
import com.clougence.clouddm.console.web.component.file.LocalFileService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.access.ApprovalDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.dal.model.approval.SqlContentType;
import com.clougence.clouddm.platform.dal.model.system.DmSysAttachmentDO;
import com.clougence.clouddm.platform.dal.model.system.SysAttachmentStatus;
import com.clougence.clouddm.platform.dal.model.system.SysAttachmentType;
import com.clougence.utils.function.EFunction;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApprovalServiceImpl implements ApprovalService {

    @Resource
    private ApprovalDal      approvalDal;
    @Resource
    private SystemDal        systemDal;
    @Resource
    private LocalFileService localFileService;

    @Override
    public void checkSqlFile(long attachmentId, String ownerUid) {
        DmSysAttachmentDO attachment = this.systemDal.attachmentMapper().selectById(attachmentId);
        if (attachment == null ||                                           //
            attachment.getAttachmentType() != SysAttachmentType.SQL_FILE || //
            !this.localFileService.exists(attachmentId)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SQL_FILE_NOT_FOUND_ERROR.name()));
        }

        if (attachment.getAttachmentStatus() == SysAttachmentStatus.EDITING) {
            this.localFileService.renew(attachmentId, ownerUid);
        }
    }

    @Override
    public <T> T consumeSqlFile(long approvalId, EFunction<Path, T, Exception> visitor) {
        DmApprovalDO approval = this.approvalDal.approvalMapper().queryById(approvalId);
        if (approval == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_NOT_FOUND_ERROR.name()));
        }
        String date = new SimpleDateFormat("yyyyMMdd").format(approval.getGmtCreate());
        Path cacheFile = Paths.get(GlobalConfUtils.getTempDataHome(), "approval", date, approval.getBizId() + ".sql");

        if (approval.getContentType() == SqlContentType.ATTACHMENT) {
            DmSysAttachmentDO attachment = this.systemDal.attachmentMapper().selectConfirmedByApprovalId(approvalId);
            if (attachment == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SQL_FILE_NOT_FOUND_ERROR.name()));
            }

            return this.localFileService.consumeLocked(attachment.getId(), cacheFile, visitor);
        } else {
            Path file = this.prepareSqlFile(approval, cacheFile);
            try {
                return visitor.eApply(file);
            } catch (ErrorMessageException e) {
                throw e;
            } catch (Exception e) {
                throw new IllegalStateException("consume approval SQL file failed, approvalId: " + approvalId, e);
            }
        }
    }

    private Path prepareSqlFile(DmApprovalDO approval, Path target) {
        if (Files.isRegularFile(target)) {
            return target;
        }

        try {
            Files.createDirectories(target.getParent());
            Path writingDirectory = Paths.get(GlobalConfUtils.getTempDataHome(), "approval", ".writing");
            Files.createDirectories(writingDirectory);
            Path staging = Files.createTempFile(writingDirectory, approval.getBizId() + ".sql.writing-", "");
            try {
                Files.writeString(staging, approval.getRawSql(), StandardCharsets.UTF_8);
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
            log.error("prepare approval SQL file failed, approvalId: " + approval.getId(), e);
            throw new IllegalStateException("prepare approval SQL file failed", e);
        }
    }

    @Override
    public void confirmSqlFile(long approvalId, long attachmentId, String userUID) {
        this.localFileService.lock(attachmentId, approvalId, userUID);
    }
}
