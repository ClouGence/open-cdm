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
package com.clougence.clouddm.console.web.service.upload;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.vo.datasource.ConsoleUploadVO;
import com.clougence.clouddm.platform.dal.access.NamingDao;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import groovy.util.logging.Slf4j;
import jakarta.annotation.Resource;

@lombok.extern.slf4j.Slf4j
@Slf4j
@Service
public class ConsoleUploadServiceImpl implements ConsoleUploadService {

    private static final String      ROOT_DIR          = "console-upload";
    private static final long        TEXT_MAX_SIZE     = 1024L * 1024L;
    private static final long        BINARY_MAX_SIZE   = 10L * 1024L * 1024L;
    private static final String      UPLOAD_MARK       = "://upload:";
    private static final Set<String> TEXT_FORMATS      = Set.of("pem", "key", "crt", "cer");
    private static final Set<String> SUPPORTED_FORMATS = Set.of("pem", "key", "crt", "cer", "pk8", "p7b", "p12", "pfx", "jks");

    @Resource
    private NamingDao                namingDao;

    @Override
    public ConsoleUploadVO uploadCertificate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_EMPTY_ERROR.name()));
        }
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_FILE_NAME_EMPTY_ERROR.name()));
        }
        int index = fileName.lastIndexOf('.');
        if (index < 0 || index == fileName.length() - 1) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_FILE_EXTENSION_EMPTY_ERROR.name()));
        }
        String normalizedFormat = fileName.substring(index + 1).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
        if (!SUPPORTED_FORMATS.contains(normalizedFormat)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_FORMAT_UNSUPPORTED_ERROR.name(), normalizedFormat));
        }
        long maxSize = TEXT_FORMATS.contains(normalizedFormat) ? TEXT_MAX_SIZE : BINARY_MAX_SIZE;
        if (file.getSize() > maxSize) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_FILE_TOO_LARGE_ERROR.name()));
        }

        String fileId = this.namingDao.genUploadFileId();
        try {
            Path uploadDir = Paths.get(GlobalConfUtils.getTempDataHome(), ROOT_DIR);
            Files.createDirectories(uploadDir);
            Map<String, Object> record = new LinkedHashMap<>();
            record.put("fileId", fileId);
            record.put("fileName", fileName);
            record.put("format", normalizedFormat);
            record.put("size", file.getSize());
            record.put("data", Base64.getEncoder().encodeToString(file.getBytes()));

            String encrypted = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(JsonUtils.toJson(record));
            Files.writeString(uploadDir.resolve(fileId + ".upload"), encrypted, StandardCharsets.UTF_8);
        } catch (ErrorMessageException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_SAVE_FAILED_ERROR.name(), e.getMessage()));
        }

        ConsoleUploadVO vo = new ConsoleUploadVO();
        vo.setFileId(fileId);
        vo.setFileName(fileName);
        vo.setFormat(normalizedFormat);
        vo.setSize(file.getSize());
        return vo;
    }

    @Override
    public String resolveCertificateData(String value) {
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

        // is UPLOAD
        int index = value.indexOf(UPLOAD_MARK);
        String format = value.substring(0, index);
        String fileId = value.substring(index + UPLOAD_MARK.length());
        if (StringUtils.isBlank(format) || StringUtils.isBlank(fileId)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_VALUE_INVALID_ERROR.name()));
        }
        try {
            Path uploadDir = Paths.get(GlobalConfUtils.getTempDataHome(), ROOT_DIR);
            Path uploadFile = uploadDir.resolve(fileId + ".upload");
            if (!Files.exists(uploadFile)) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_FILE_NOT_FOUND_ERROR.name()));
            }

            String encrypted = Files.readString(uploadFile, StandardCharsets.UTF_8);
            Map<String, String> record = JsonUtils.toMap(CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(encrypted));
            String storedFileId = record.get("fileId");
            String storedFormat = record.get("format");
            String data = record.get("data");
            if (!StringUtils.equals(fileId, storedFileId) || StringUtils.isBlank(storedFormat) || StringUtils.isBlank(data)) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_RECORD_INVALID_ERROR.name()));
            }

            return storedFormat + "://" + data;
        } catch (ErrorMessageException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_UPLOAD_CERT_RESOLVE_FAILED_ERROR.name(), e.getMessage()));
        }
    }
}
