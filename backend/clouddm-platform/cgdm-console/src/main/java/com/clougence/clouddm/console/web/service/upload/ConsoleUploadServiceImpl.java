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
import com.clougence.clouddm.console.web.model.vo.datasource.ConsoleUploadVO;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

@Service
public class ConsoleUploadServiceImpl implements ConsoleUploadService {

    private static final String      ROOT_DIR          = "console-upload";
    private static final long        TEXT_MAX_SIZE     = 1024L * 1024L;
    private static final long        BINARY_MAX_SIZE   = 10L * 1024L * 1024L;
    private static final String      UPLOAD_MARK       = "://upload:";
    private static final Set<String> TEXT_FORMATS      = Set.of("pem", "key", "crt", "cer");
    private static final Set<String> BINARY_FORMATS    = Set.of("p12", "pfx", "jks");
    private static final Set<String> SUPPORTED_FORMATS = Set.of("pem", "key", "crt", "cer", "p12", "pfx", "jks");

    @Override
    public ConsoleUploadVO uploadCertificate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("upload file can not be empty.");
        }
        String normalizedFormat = resolveFormat(file.getOriginalFilename());
        validateSize(normalizedFormat, file.getSize());

        String fileId = UUID.randomUUID().toString().replace("-", "");
        try {
            Files.createDirectories(uploadDir());
            Map<String, Object> record = new LinkedHashMap<>();
            record.put("fileId", fileId);
            record.put("fileName", file.getOriginalFilename());
            record.put("format", normalizedFormat);
            record.put("size", file.getSize());
            record.put("data", Base64.getEncoder().encodeToString(file.getBytes()));

            String encrypted = CryptService.INSTANCE.encryptUseDefaultKeyAndSalt(JsonUtils.toJson(record));
            Files.writeString(uploadDir().resolve(fileId + ".upload"), encrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("save upload file failed.", e);
        }

        ConsoleUploadVO vo = new ConsoleUploadVO();
        vo.setFileId(fileId);
        vo.setFileName(file.getOriginalFilename());
        vo.setFormat(normalizedFormat);
        vo.setSize(file.getSize());
        return vo;
    }

    @Override
    public String resolveCertificateData(String value) {
        if (StringUtils.isBlank(value) || !value.contains(UPLOAD_MARK)) {
            return value;
        }

        int index = value.indexOf(UPLOAD_MARK);
        String format = value.substring(0, index);
        String fileId = value.substring(index + UPLOAD_MARK.length());
        if (StringUtils.isBlank(format) || StringUtils.isBlank(fileId)) {
            throw new IllegalArgumentException("invalid upload certificate value.");
        }

        try {
            Path uploadFile = uploadDir().resolve(fileId + ".upload");
            if (!Files.exists(uploadFile)) {
                throw new IllegalArgumentException("upload certificate file not found.");
            }

            String encrypted = Files.readString(uploadFile, StandardCharsets.UTF_8);
            Map<String, String> record = JsonUtils.toMap(CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(encrypted));
            String storedFileId = record.get("fileId");
            String storedFormat = record.get("format");
            String data = record.get("data");
            if (!StringUtils.equals(fileId, storedFileId) || StringUtils.isBlank(storedFormat) || StringUtils.isBlank(data)) {
                throw new IllegalArgumentException("invalid upload certificate record.");
            }

            return storedFormat + "://" + data;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("resolve upload certificate file failed.", e);
        }
    }

    private String resolveFormat(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("certificate file name can not be empty.");
        }
        int index = fileName.lastIndexOf('.');
        if (index < 0 || index == fileName.length() - 1) {
            throw new IllegalArgumentException("certificate file extension can not be empty.");
        }
        String normalizedFormat = fileName.substring(index + 1).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
        if (!SUPPORTED_FORMATS.contains(normalizedFormat)) {
            throw new IllegalArgumentException("unsupported certificate format: " + normalizedFormat);
        }
        return normalizedFormat;
    }

    private void validateSize(String format, long size) {
        long maxSize = TEXT_FORMATS.contains(format) ? TEXT_MAX_SIZE : BINARY_MAX_SIZE;
        if (size > maxSize) {
            throw new IllegalArgumentException("certificate file is too large.");
        }
        if (!TEXT_FORMATS.contains(format) && !BINARY_FORMATS.contains(format)) {
            throw new IllegalArgumentException("unsupported certificate format: " + format);
        }
    }

    private Path uploadDir() {
        return Paths.get(GlobalConfUtils.getTempDataHome(), ROOT_DIR);
    }
}
