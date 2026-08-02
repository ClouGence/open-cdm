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
package com.clougence.clouddm.console.web.service.upload.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Locale;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.file.LocalFileService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.vo.datasource.ConsoleUploadVO;
import com.clougence.clouddm.console.web.service.upload.UploadService4Certificate;
import com.clougence.clouddm.platform.dal.model.system.SysAttachmentType;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;

@lombok.extern.slf4j.Slf4j
@Service
public class UploadService4CertificateImpl extends AbstractUploadService implements UploadService4Certificate {

    private static final long        TEXT_MAX_SIZE     = MEGABYTE;
    private static final long        BINARY_MAX_SIZE   = 10L * MEGABYTE;
    private static final String      UPLOAD_MARK       = "://upload:";
    private static final Set<String> TEXT_FORMATS      = Set.of("pem", "key", "crt", "cer");
    private static final Set<String> SUPPORTED_FORMATS = Set.of("pem", "key", "crt", "cer", "pk8", "p7b", "p12", "pfx", "jks");

    @Resource
    private LocalFileService         localFileService;

    @Override
    public ConsoleUploadVO uploadCertificate(String uid, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_EMPTY_ERROR.name()));
        }
        String fileName = this.normalizeFileName(file.getOriginalFilename());
        int index = fileName.lastIndexOf('.');
        String normalizedFormat = fileName.substring(index + 1).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
        if (!SUPPORTED_FORMATS.contains(normalizedFormat)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_FORMAT_UNSUPPORTED_ERROR.name(), normalizedFormat));
        }
        long maxSize = TEXT_FORMATS.contains(normalizedFormat) ? TEXT_MAX_SIZE : BINARY_MAX_SIZE;
        this.checkFileSize(file.getSize(), maxSize, UploadService4CertificateImpl::fileTooLarge);

        Path staging = null;
        try {
            staging = this.createStagingFile(".cert");
            long fileSize = this.copyWithLimit(file.getInputStream(), staging, maxSize, UploadService4CertificateImpl::fileTooLarge);
            long attachmentId = this.localFileService.addAsEditing(uid, staging, fileName, SysAttachmentType.CERTIFICATE_FILE);
            staging = null;

            ConsoleUploadVO vo = new ConsoleUploadVO();
            vo.setFileId(String.valueOf(attachmentId));
            vo.setFileName(fileName);
            vo.setFormat(normalizedFormat);
            vo.setSize(fileSize);
            return vo;
        } catch (ErrorMessageException e) {
            this.deleteQuietly(staging);
            throw e;
        } catch (Exception e) {
            this.deleteQuietly(staging);
            log.error(e.getMessage(), e);
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_SAVE_FAILED_ERROR.name(), e.getMessage()));
        }
    }

    @Override
    public String readCertificateData(String uid, String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        if (StringUtils.isBlank(value)) {
            return value;
        }
        if (value.startsWith("text://")) {
            if (StringUtils.isBlank(value.substring("text://".length()))) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_VALUE_INVALID_ERROR.name()));
            }
            return value;
        }
        if (!value.contains(UPLOAD_MARK)) {
            return value;
        }

        int index = value.indexOf(UPLOAD_MARK);
        String format = value.substring(0, index);
        String attachmentIdValue = value.substring(index + UPLOAD_MARK.length());
        if (StringUtils.isBlank(format) || StringUtils.isBlank(attachmentIdValue)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_VALUE_INVALID_ERROR.name()));
        }
        try {
            long attachmentId = Long.parseLong(attachmentIdValue);
            return this.localFileService.consumeEditing(uid, attachmentId, path -> {
                String storedFormat = fileFormat(path, format);
                String data = Base64.getEncoder().encodeToString(Files.readAllBytes(path));
                if (StringUtils.isBlank(data)) {
                    throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_RECORD_INVALID_ERROR.name()));
                }
                return storedFormat + "://" + data;
            });
        } catch (NumberFormatException e) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_VALUE_INVALID_ERROR.name()));
        } catch (ErrorMessageException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_RESOLVE_FAILED_ERROR.name(), e.getMessage()));
        }
    }

    @Override
    public void deleteCertificateData(String uid, String value) {
        if (StringUtils.isBlank(value)) {
            return;
        }
        int index = value.indexOf(UPLOAD_MARK);
        if (index < 0) {
            return;
        }
        long attachmentId;
        try {
            attachmentId = Long.parseLong(value.substring(index + UPLOAD_MARK.length()));
        } catch (NumberFormatException e) {
            return;
        }

        try {
            this.localFileService.consumeEditing(uid, attachmentId, path -> null);
            this.localFileService.deleteRecord(attachmentId);
        } catch (Exception e) {
            log.warn("delete temporary certificate failed, attachmentId: {}", attachmentId, e);
        }
    }

    private static String fileFormat(Path path, String expectedFormat) {
        String fileName = path.getFileName().toString();
        int index = fileName.lastIndexOf('.');
        String storedFormat = index < 0 ? "" : fileName.substring(index + 1).toLowerCase(Locale.ROOT);
        if (!SUPPORTED_FORMATS.contains(storedFormat) || !storedFormat.equalsIgnoreCase(expectedFormat)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_RECORD_INVALID_ERROR.name()));
        }
        return storedFormat;
    }

    private String normalizeFileName(String originalFileName) {
        if (StringUtils.isBlank(originalFileName)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_FILE_NAME_EMPTY_ERROR.name()));
        }
        String fileName = this.extractFileName(originalFileName);
        int index = fileName.lastIndexOf('.');
        if (index < 0 || index == fileName.length() - 1) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_FILE_EXTENSION_EMPTY_ERROR.name()));
        }
        return fileName;
    }

    private static ErrorMessageException fileTooLarge() {
        return new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_FILE_TOO_LARGE_ERROR.name()));
    }
}
