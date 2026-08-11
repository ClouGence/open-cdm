/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.console.web.component.file.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.file.LocalFileService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.system.DmSysAttachmentDO;
import com.clougence.clouddm.platform.dal.model.system.SysAttachmentStatus;
import com.clougence.clouddm.platform.dal.model.system.SysAttachmentType;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;
import com.clougence.utils.function.EFunction;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocalFileServiceImpl implements LocalFileService, UnifiedPostConstruct {

    private static final long           EDITING_TTL_MS = TimeUnit.HOURS.toMillis(1);
    private static final int            CLEAN_BATCH    = 500;
    private final Object[]              restoreLocks   = new Object[64];
    @Resource
    private SystemDal                   systemDal;
    private ScheduledThreadPoolExecutor cleanExecutor;

    public LocalFileServiceImpl(){
        for (int i = 0; i < this.restoreLocks.length; i++) {
            this.restoreLocks[i] = new Object();
        }
    }

    @Override
    public void init() {
        ThreadFactory factory = ThreadUtils.daemonThreadFactory(this.getClass().getClassLoader(), "LocalFileClear-%s");
        this.cleanExecutor = new ScheduledThreadPoolExecutor(1, factory);
        this.cleanExecutor.scheduleWithFixedDelay(() -> {
            Date before = Date.from(Instant.ofEpochMilli(System.currentTimeMillis() - EDITING_TTL_MS));
            try {
                while (true) {
                    List<DmSysAttachmentDO> attachments = this.systemDal.attachmentMapper().listExpiredEditing(before, CLEAN_BATCH);
                    if (attachments.isEmpty()) {
                        break;
                    }
                    for (DmSysAttachmentDO attachment : attachments) {
                        if (this.systemDal.attachmentMapper().deleteExpiredEditing(attachment.getId(), before) == 1) {
                            deleteTemporaryFile(attachment);
                        }
                    }
                }

                Path uploadDirectory = uploadDir();
                if (Files.isDirectory(uploadDirectory)) {
                    try (DirectoryStream<Path> files = Files.newDirectoryStream(uploadDirectory)) {
                        for (Path file : files) {
                            if (!Files.isRegularFile(file) || Files.getLastModifiedTime(file).toMillis() > before.getTime()) {
                                continue;
                            }
                            String fileName = file.getFileName().toString();
                            if (fileName.startsWith(".uploading-") || fileName.endsWith(".tmp")) {
                                deleteQuietly(file);
                                continue;
                            }
                            int extensionIndex = fileName.lastIndexOf('.');
                            if (extensionIndex <= 0) {
                                continue;
                            }
                            try {
                                long fileId = Long.parseLong(fileName.substring(0, extensionIndex));
                                DmSysAttachmentDO attachment = this.systemDal.attachmentMapper().selectById(fileId);
                                if (attachment == null || attachment.getAttachmentStatus() != SysAttachmentStatus.EDITING || !file.equals(uploadFile(attachment))) {
                                    deleteQuietly(file);
                                }
                            } catch (NumberFormatException ignored) {
                                // The upload directory only owns numeric resource files and upload staging files.
                            }
                        }
                    }
                }

                Path execDirectory = execDir();
                if (Files.isDirectory(execDirectory)) {
                    try (DirectoryStream<Path> files = Files.newDirectoryStream(execDirectory, "*.tmp")) {
                        for (Path file : files) {
                            if (Files.isRegularFile(file) && Files.getLastModifiedTime(file).toMillis() <= before.getTime()) {
                                deleteQuietly(file);
                            }
                        }
                    }
                }

                Path sqlFileDirectory = sqlFileCacheDir();
                if (Files.isDirectory(sqlFileDirectory)) {
                    try (var paths = Files.walk(sqlFileDirectory)) {
                        paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                            try {
                                if (Files.isRegularFile(path)) {
                                    if (Files.getLastModifiedTime(path).toMillis() <= before.getTime()) {
                                        Files.deleteIfExists(path);
                                    }
                                } else if (!sqlFileDirectory.equals(path)) {
                                    Files.deleteIfExists(path);
                                }
                            } catch (DirectoryNotEmptyException ignored) {
                                // Keep directories that still contain live cache files.
                            } catch (IOException e) {
                                log.warn("delete expired SQL file cache failed: {}", path, e);
                            }
                        });
                    }
                }

            } catch (Throwable e) {
                log.error("clean expired local files failed", e);
            }
        }, 10, 10, TimeUnit.MINUTES);
    }

    @Override
    public void stop() {
        if (this.cleanExecutor != null) {
            this.cleanExecutor.shutdown();
        }
    }

    @Override
    public void invalidateCache(Path cacheFile) {
        Path cacheDirectory = sqlFileCacheDir();
        Path localFile = cacheFile.toAbsolutePath().normalize();
        if (cacheDirectory.equals(localFile) || !localFile.startsWith(cacheDirectory)) {
            throw new IllegalArgumentException("cache file must be under the CloudDM SQL file cache directory");
        }
        try {
            Files.deleteIfExists(localFile);
        } catch (IOException e) {
            throw new IllegalStateException("invalidate local file cache failed: " + localFile, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public long addAsEditing(String userUid, Path temporaryFile, String fileName, SysAttachmentType attachmentType) {
        if (StringUtils.isBlank(userUid)) {
            throw new IllegalArgumentException("userUid cannot be blank");
        }
        if (attachmentType == null) {
            throw new IllegalArgumentException("attachmentType cannot be null");
        }

        Path sourceDirectory = attachmentType == SysAttachmentType.SQL_FILE_TASK ? execDir().toAbsolutePath().normalize() : uploadDir().toAbsolutePath().normalize();
        Path source = temporaryFile.toAbsolutePath().normalize();
        if (!sourceDirectory.equals(source.getParent()) || !Files.isRegularFile(source)) {
            throw new IllegalArgumentException("temporary file is outside its managed directory");
        }

        Path target = null;
        try {
            long fileSize = Files.size(source);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (InputStream input = new DigestInputStream(Files.newInputStream(source), digest)) {
                input.transferTo(OutputStream.nullOutputStream());
            }
            String fileHash = HexFormat.of().formatHex(digest.digest());

            if (attachmentType == SysAttachmentType.SQL_FILE_TASK) {
                List<DmSysAttachmentDO> oldPackages = this.systemDal.attachmentMapper().listEditingByTypeAndFileName(attachmentType, fileName);
                for (DmSysAttachmentDO oldPackage : oldPackages) {
                    if (this.systemDal.attachmentMapper().deleteById(oldPackage.getId()) == 1) {
                        deleteTemporaryFile(oldPackage);
                    }
                }
            }

            DmSysAttachmentDO attachment = new DmSysAttachmentDO();
            attachment.setOwnerUid(userUid);
            attachment.setAttachmentType(attachmentType);
            attachment.setAttachmentStatus(SysAttachmentStatus.EDITING);
            attachment.setFileName(fileName);
            attachment.setFileSize(fileSize);
            attachment.setFileHash(fileHash);
            this.systemDal.attachmentMapper().insert(attachment);

            target = uploadFile(attachment);
            Files.createDirectories(target.getParent());
            move(source, target);
            return attachment.getId();
        } catch (Exception e) {
            deleteQuietly(target);
            if (e instanceof ErrorMessageException error) {
                throw error;
            }
            log.error("store local file failed: {}", source, e);
            I18nDmMsgKeys messageKey = attachmentType == SysAttachmentType.CERTIFICATE_FILE ? I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_SAVE_FAILED_ERROR : I18nDmMsgKeys.TICKET_SQL_FILE_SAVE_FAILED_ERROR;
            throw new ErrorMessageException(DmI18nUtils.getMessage(messageKey.name(), e.getMessage()));
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public long addAsLocked(String userUid, Path sourceFile, String fileName, SysAttachmentType attachmentType, long approvalId) {
        if (StringUtils.isBlank(userUid)) {
            throw new IllegalArgumentException("userUid cannot be blank");
        }
        if (attachmentType == null) {
            throw new IllegalArgumentException("attachmentType cannot be null");
        }
        Path temporaryDirectory = Paths.get(GlobalConfUtils.getTempDataHome()).toAbsolutePath().normalize();
        Path source = sourceFile.toAbsolutePath().normalize();
        if (!source.startsWith(temporaryDirectory) || !Files.isRegularFile(source)) {
            throw new IllegalArgumentException("source file must be under the CloudDM temporary directory");
        }

        try {
            long fileSize = Files.size(source);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (InputStream input = new DigestInputStream(Files.newInputStream(source), digest)) {
                input.transferTo(OutputStream.nullOutputStream());
            }

            DmSysAttachmentDO attachment = new DmSysAttachmentDO();
            attachment.setOwnerUid(userUid);
            attachment.setApprovalId(approvalId);
            attachment.setAttachmentType(attachmentType);
            attachment.setAttachmentStatus(SysAttachmentStatus.CONFIRMED);
            attachment.setFileName(fileName);
            attachment.setFileSize(fileSize);
            attachment.setFileHash(HexFormat.of().formatHex(digest.digest()));
            this.systemDal.attachmentMapper().insert(attachment);
            try (InputStream input = Files.newInputStream(source)) {
                this.systemDal.writeAttachment(attachment.getId(), input, fileSize);
            }
            return attachment.getId();
        } catch (Exception e) {
            if (e instanceof ErrorMessageException error) {
                throw error;
            }
            log.error("store locked local file failed: {}", source, e);
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SQL_FILE_SAVE_FAILED_ERROR.name(), e.getMessage()));
        }
    }

    @Override
    public boolean exists(long fileId) {
        DmSysAttachmentDO attachment = this.systemDal.attachmentMapper().selectById(fileId);
        if (attachment == null) {
            return false;
        }
        if (attachment.getAttachmentStatus() == SysAttachmentStatus.EDITING) {
            return !expired(attachment) && Files.isRegularFile(uploadFile(attachment));
        }
        return attachment.getAttachmentStatus() == SysAttachmentStatus.CONFIRMED;
    }

    @Override
    public <T> T consumeLocked(long fileId, Path cacheFile, EFunction<Path, T, Exception> visitor) {
        DmSysAttachmentDO attachment = this.systemDal.attachmentMapper().selectById(fileId);
        if (attachment == null || attachment.getAttachmentStatus() != SysAttachmentStatus.CONFIRMED) {
            throw fileNotFound();
        }

        Path temporaryDirectory = Paths.get(GlobalConfUtils.getTempDataHome()).toAbsolutePath().normalize();
        Path localFile = cacheFile.toAbsolutePath().normalize();
        if (temporaryDirectory.equals(localFile) || !localFile.startsWith(temporaryDirectory)) {
            throw new IllegalArgumentException("cache file must be under the CloudDM temporary directory");
        }
        synchronized (this.restoreLocks[Math.floorMod(attachment.getId().hashCode(), this.restoreLocks.length)]) {
            if (!Files.isRegularFile(localFile)) {
                try {
                    Files.createDirectories(localFile.getParent());
                    Path writingDirectory = localFile.getParent().resolveSibling(".writing");
                    Files.createDirectories(writingDirectory);
                    Path staging = Files.createTempFile(writingDirectory, localFile.getFileName() + ".writing-", "");
                    try {
                        MessageDigest digest = MessageDigest.getInstance("SHA-256");
                        try (OutputStream output = new DigestOutputStream(Files.newOutputStream(staging), digest)) {
                            if (!this.systemDal.readAttachment(attachment.getId(), output)) {
                                throw fileNotFound();
                            }
                        } catch (Exception e) {
                            throw new IllegalStateException("restore local file content failed", e);
                        }
                        String fileHash = HexFormat.of().formatHex(digest.digest());
                        if (Files.size(staging) != attachment.getFileSize() || !fileHash.equals(attachment.getFileHash())) {
                            throw new IllegalStateException("local file checksum mismatch");
                        }
                        move(staging, localFile);
                    } finally {
                        Files.deleteIfExists(staging);
                    }
                } catch (Exception e) {
                    log.error("restore local file failed, fileId: {}", attachment.getId(), e);
                    throw new IllegalStateException("restore local file failed, fileId: " + attachment.getId(), e);
                }
            }
        }
        try {
            return visitor.eApply(localFile);
        } catch (ErrorMessageException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("consume local file failed, fileId: " + fileId, e);
        }
    }

    @Override
    public <T> T consumeEditing(String userUid, long fileId, EFunction<Path, T, Exception> visitor) {
        DmSysAttachmentDO attachment = this.systemDal.attachmentMapper().selectById(fileId);
        if (!isEditingOwner(attachment, userUid)) {
            throw fileNotFound(attachment == null ? null : attachment.getAttachmentType());
        }
        Path localFile = this.requireEditingFile(attachment);
        try {
            return visitor.eApply(localFile);
        } catch (ErrorMessageException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("consume editing local file failed, fileId: " + fileId, e);
        }
    }

    @Override
    public void renewEditing(String userUid, long fileId) {
        Date before = Date.from(Instant.ofEpochMilli(System.currentTimeMillis() - EDITING_TTL_MS));
        if (this.systemDal.attachmentMapper().touchEditing(fileId, userUid, before) != 1) {
            throw fileNotFound();
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void lockEditing(String userUid, long fileId, long approvalId) {
        DmSysAttachmentDO attachment = this.systemDal.attachmentMapper().selectByIdForUpdate(fileId);
        if (attachment != null && attachment.getAttachmentStatus() == SysAttachmentStatus.CONFIRMED) {
            if (Objects.equals(attachment.getApprovalId(), approvalId)) {
                return;
            }
            throw fileNotFound();
        }
        if (!isEditingOwner(attachment, userUid)) {
            throw fileNotFound();
        }
        Date before = Date.from(Instant.ofEpochMilli(System.currentTimeMillis() - EDITING_TTL_MS));
        if (!attachment.getGmtModified().after(before)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SQL_FILE_EXPIRED_ERROR.name()));
        }

        Path file = this.requireEditingFile(attachment);
        try (InputStream input = Files.newInputStream(file)) {
            this.systemDal.writeAttachment(fileId, input, attachment.getFileSize());
        } catch (IOException e) {
            log.error("persist local file failed, fileId: {}", fileId, e);
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SQL_FILE_SAVE_FAILED_ERROR.name(), e.getMessage()));
        }

        if (this.systemDal.attachmentMapper().lock(fileId, approvalId, userUid, before) != 1) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SQL_FILE_EXPIRED_ERROR.name()));
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                deleteTemporaryFile(attachment);
            }
        });
    }

    @Override
    public void deleteRecord(long fileId) {
        DmSysAttachmentDO attachment = this.systemDal.attachmentMapper().selectById(fileId);
        if (attachment == null) {
            return;
        }
        if (this.systemDal.attachmentMapper().deleteById(fileId) == 1 && attachment.getAttachmentStatus() == SysAttachmentStatus.EDITING) {
            deleteTemporaryFile(attachment);
        }
    }

    private static boolean isEditingOwner(DmSysAttachmentDO attachment, String ownerUid) {
        return attachment != null && attachment.getAttachmentStatus() == SysAttachmentStatus.EDITING && StringUtils.isNotBlank(ownerUid)
               && ownerUid.equals(attachment.getOwnerUid());
    }

    private Path requireEditingFile(DmSysAttachmentDO attachment) {
        if (attachment.getAttachmentStatus() != SysAttachmentStatus.EDITING) {
            throw fileNotFound(attachment.getAttachmentType());
        }
        if (expired(attachment)) {
            if (attachment.getAttachmentType() == SysAttachmentType.CERTIFICATE_FILE) {
                throw fileNotFound(SysAttachmentType.CERTIFICATE_FILE);
            }
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SQL_FILE_EXPIRED_ERROR.name()));
        }
        Path file = uploadFile(attachment);
        if (!Files.isRegularFile(file)) {
            throw fileNotFound(attachment.getAttachmentType());
        }
        return file;
    }

    private static boolean expired(DmSysAttachmentDO attachment) {
        return attachment.getGmtModified().getTime() <= System.currentTimeMillis() - EDITING_TTL_MS;
    }

    private static Path uploadDir() {
        return Paths.get(GlobalConfUtils.getTempDataHome(), "upload");
    }

    private static Path execDir() {
        return Paths.get(GlobalConfUtils.getTempDataHome(), "exec");
    }

    private static Path sqlFileCacheDir() {
        return Paths.get(GlobalConfUtils.getTempDataHome(), "sqlfile").toAbsolutePath().normalize();
    }

    private static Path uploadFile(DmSysAttachmentDO attachment) {
        if (attachment.getAttachmentType() == SysAttachmentType.SQL_FILE_TASK) {
            return execDir().resolve(attachment.getFileName());
        }
        String extension;
        if (attachment.getAttachmentType() == SysAttachmentType.SQL_FILE) {
            extension = ".sql";
        } else {
            String fileName = attachment.getFileName();
            int index = fileName == null ? -1 : fileName.lastIndexOf('.');
            if (index < 0 || index == fileName.length() - 1) {
                extension = ".cert";
            } else {
                String fileExtension = fileName.substring(index + 1).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
                extension = fileExtension.isEmpty() ? ".cert" : "." + fileExtension;
            }
        }
        return uploadDir().resolve(attachment.getId() + extension);
    }

    private static void move(Path source, Path target) throws IOException {
        try {
            Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(source, target);
        }
    }

    private static void deleteTemporaryFile(DmSysAttachmentDO attachment) {
        deleteQuietly(uploadFile(attachment));
    }

    private static void deleteQuietly(Path file) {
        if (file == null) {
            return;
        }
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            log.warn("delete temporary file failed: {}", file, e);
        }
    }

    private static ErrorMessageException fileNotFound() {
        return new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SQL_FILE_NOT_FOUND_ERROR.name()));
    }

    private static ErrorMessageException fileNotFound(SysAttachmentType attachmentType) {
        I18nDmMsgKeys messageKey = attachmentType == SysAttachmentType.CERTIFICATE_FILE ? I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_FILE_NOT_FOUND_ERROR : I18nDmMsgKeys.TICKET_SQL_FILE_NOT_FOUND_ERROR;
        return new ErrorMessageException(DmI18nUtils.getMessage(messageKey.name()));
    }
}
