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
package com.clougence.clouddm.worker.component.resource.file;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.api.common.crypt.PasswordInfo;
import com.clougence.utils.HexadecimalUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.RandomUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EncryptedFileCacheManager {

    private static final String ROOT_DIR  = "encrypted-cache";
    private final String        cacheKey  = HexadecimalUtils.bytes2bit(RandomUtils.nextBytes(32));
    private final String        cacheSalt = HexadecimalUtils.bytes2bit(RandomUtils.nextBytes(32));

    public <T> T read(String namespace, String key, Class<T> valueType) {
        Path path = cachePath(namespace, key);
        if (!Files.isRegularFile(path)) {
            return null;
        }

        try {
            PasswordInfo passwordInfo = new PasswordInfo();
            passwordInfo.setKey(this.cacheKey);
            passwordInfo.setSalt(this.cacheSalt);
            passwordInfo.setEncryptPassword(Files.readString(path, StandardCharsets.UTF_8));
            CryptService.INSTANCE.decrypt(passwordInfo);
            return JsonUtils.toObj(passwordInfo.getPlainPassword(), valueType);
        } catch (Exception e) {
            log.warn("read encrypted file cache failed, namespace={}, key={}, delete and refetch. message={}", namespace, key, e.getMessage());
            delete(path);
            return null;
        }
    }

    public void write(String namespace, String key, Object value) {
        try {
            Files.createDirectories(cacheDir(namespace));
            PasswordInfo passwordInfo = new PasswordInfo();
            passwordInfo.setKey(this.cacheKey);
            passwordInfo.setSalt(this.cacheSalt);
            passwordInfo.setPlainPassword(JsonUtils.toJson(value));
            CryptService.INSTANCE.encrypt(passwordInfo);
            Files.writeString(cachePath(namespace, key), passwordInfo.getEncryptPassword(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.warn("write encrypted file cache failed, namespace={}, key={}, message={}", namespace, key, e.getMessage());
        }
    }

    private void delete(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
        }
    }

    private Path cachePath(String namespace, String key) {
        return cacheDir(namespace).resolve(safeName(key) + ".cache");
    }

    private Path cacheDir(String namespace) {
        return Paths.get(GlobalConfUtils.getTempDataHome(), ROOT_DIR, safeName(namespace));
    }

    private String safeName(String value) {
        if (StringUtils.isBlank(value)) {
            return "_";
        }
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
}
