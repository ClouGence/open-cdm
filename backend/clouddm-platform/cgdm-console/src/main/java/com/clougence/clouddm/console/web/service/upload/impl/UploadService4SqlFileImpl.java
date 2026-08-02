/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.console.web.service.upload.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.config.UserConfigService;
import com.clougence.clouddm.console.web.component.file.LocalFileService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.service.upload.UploadService4SqlFile;
import com.clougence.clouddm.console.web.service.upload.model.SqlFilePreviewVO;
import com.clougence.clouddm.console.web.service.upload.model.SqlFileUploadVO;
import com.clougence.clouddm.platform.dal.model.system.SysAttachmentType;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UploadService4SqlFileImpl extends AbstractUploadService implements UploadService4SqlFile {

    @Resource
    private LocalFileService  localFileService;
    @Resource
    private UserConfigService userConfigService;

    @Override
    public SqlFileUploadVO upload(String uid, MultipartFile file) {
        String fileName = this.normalizeFileName(file);
        int maxMegaByte = this.userConfigService.sqlFileMaxSize();
        long maxBytes = maxMegaByte * MEGABYTE;
        this.checkFileSize(file.getSize(), maxBytes, () -> fileTooLarge(maxMegaByte));
        Path staging = null;

        try {
            staging = this.createStagingFile(".sql");
            long fileSize = this.copyWithLimit(file.getInputStream(), staging, maxBytes, () -> fileTooLarge(maxMegaByte));
            validateUtf8(staging);

            long attachmentId = this.localFileService.addAsEditing(uid, staging, fileName, SysAttachmentType.SQL_FILE);
            staging = null;

            SqlFileUploadVO vo = new SqlFileUploadVO();
            vo.setAttachmentId(attachmentId);
            vo.setFileName(fileName);
            vo.setFileSize(fileSize);
            vo.setMaxMegaByte(maxMegaByte);
            return vo;
        } catch (CharacterCodingException e) {
            this.deleteQuietly(staging);
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SQL_FILE_UTF8_ERROR.name()));
        } catch (ErrorMessageException e) {
            this.deleteQuietly(staging);
            throw e;
        } catch (Exception e) {
            this.deleteQuietly(staging);
            log.error("upload SQL file failed", e);
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SQL_FILE_SAVE_FAILED_ERROR.name(), e.getMessage()));
        }
    }

    @Override
    public SqlFilePreviewVO preview(String uid, long attachmentId, int startLine, int lineCount) {
        SqlFilePreviewData preview = this.localFileService.consumeEditing(uid, attachmentId, file -> {
            return SqlFilePreviewReader.read(file, startLine, lineCount);
        });
        SqlFilePreviewVO vo = new SqlFilePreviewVO();
        vo.setStartLine(preview.startLine());
        vo.setTotalLines(preview.totalLines());
        vo.setContent(preview.content());
        vo.setEof(preview.eof());
        return vo;
    }

    private String normalizeFileName(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SQL_FILE_INVALID_ERROR.name()));
        }
        String original = file.getOriginalFilename();
        if (original == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SQL_FILE_INVALID_ERROR.name()));
        }
        String fileName = this.extractFileName(original);
        if (fileName.isBlank() || !fileName.toLowerCase(Locale.ROOT).endsWith(".sql")) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SQL_FILE_INVALID_ERROR.name()));
        }
        return fileName;
    }

    private static ErrorMessageException fileTooLarge(int maxMegaByte) {
        return new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.TICKET_SQL_SIZE_OVER_ERROR.name(), maxMegaByte));
    }

    private static void validateUtf8(Path file) throws IOException {
        var decoder = StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
        try (Reader reader = new InputStreamReader(Files.newInputStream(file), decoder)) {
            char[] buffer = new char[8192];
            while (reader.read(buffer) >= 0) {
                // Decoding the stream is the validation.
            }
        }
    }

}
