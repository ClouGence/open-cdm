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
package com.clougence.clouddm.component.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.clougence.utils.function.EFunction;
import com.clougence.utils.io.FileUtils;

public class LocalCacheComponent {

    private static final LocalCacheComponent INSTANCE    = new LocalCacheComponent();
    private final Map<String, MemoryEntry>   memoryCache = new ConcurrentHashMap<>();
    private final Map<String, FileEntry>     fileCache   = new ConcurrentHashMap<>();

    public static LocalCacheComponent getInstance() { return INSTANCE; }

    public Object getObject(String key) {
        MemoryEntry entry = this.memoryCache.get(key);
        if (entry == null) {
            return null;
        }
        if (entry.expired()) {
            this.memoryCache.remove(key);
            return null;
        }
        return entry.value;
    }

    public Object getObjectIfAbsent(String key, Duration ttl, EFunction<String, Object, Exception> absent) throws Exception {
        Object value = getObject(key);
        if (value != null) {
            return value;
        }
        synchronized (this) {
            value = getObject(key);
            if (value != null) {
                return value;
            }
            value = absent.eApply(key);
            cacheAndReturn(key, value, ttl);
            return value;
        }
    }

    public Object cacheAndReturn(String key, Object value, Duration ttl) {
        this.memoryCache.put(key, new MemoryEntry(value, expireAt(ttl)));
        return value;
    }

    public Object cacheAndReturn(String key, Object value) {
        this.memoryCache.put(key, new MemoryEntry(value, 0));
        return value;
    }

    public void clearMemory() {
        this.memoryCache.clear();
    }

    public String cacheFile(File rootDir, String namespace, String fileName, byte[] fileBytes) throws IOException {
        String key = namespace + ":" + fileName;
        String fileHash = sha256(fileBytes);
        FileEntry entry = this.fileCache.get(key);
        if (valid(entry, fileHash)) {
            return entry.filePath;
        }

        synchronized (this) {
            entry = this.fileCache.get(key);
            if (valid(entry, fileHash)) {
                return entry.filePath;
            }
            File namespaceDir = new File(rootDir, namespace);
            File file = new File(namespaceDir, fileName);
            if (valid(file, fileHash)) {
                this.fileCache.put(key, new FileEntry(fileHash, file.getAbsolutePath()));
                return file.getAbsolutePath();
            }
            if (file.exists()) {
                FileUtils.deleteQuietly(file);
            }
            try (FileOutputStream outputStream = FileUtils.openOutputStream(file)) {
                outputStream.write(fileBytes);
            }
            this.fileCache.put(key, new FileEntry(fileHash, file.getAbsolutePath()));
            return file.getAbsolutePath();
        }
    }

    public void clearFiles() {
        this.fileCache.clear();
    }

    private boolean valid(FileEntry entry, String fileHash) {
        if (entry == null || !fileHash.equals(entry.fileHash)) {
            return false;
        }
        File file = new File(entry.filePath);
        if (file.exists() && file.length() > 0) {
            return true;
        }
        return false;
    }

    private boolean valid(File file, String fileHash) throws IOException {
        return file.exists() && file.length() > 0 && fileHash.equals(sha256(file));
    }

    private long expireAt(Duration ttl) {
        Duration resolvedTtl = ttl == null || ttl.isNegative() || ttl.isZero() ? Duration.ofMinutes(1) : ttl;
        return System.currentTimeMillis() + resolvedTtl.toMillis();
    }

    private String sha256(byte[] data) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(data);
            StringBuilder builder = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not supported", e);
        }
    }

    private String sha256(File file) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            try (FileInputStream inputStream = new FileInputStream(file)) {
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    digest.update(buffer, 0, length);
                }
            }
            byte[] result = digest.digest();
            StringBuilder builder = new StringBuilder(result.length * 2);
            for (byte b : result) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not supported", e);
        }
    }

    private static class MemoryEntry {

        private final Object value;
        private final long   expireAt;

        private MemoryEntry(Object value, long expireAt){
            this.value = value;
            this.expireAt = expireAt;
        }

        private boolean expired() {
            if (this.expireAt <= 0) {
                return false;
            }
            return System.currentTimeMillis() >= this.expireAt;
        }
    }

    private static class FileEntry {

        private final String fileHash;
        private final String filePath;

        private FileEntry(String fileHash, String filePath){
            this.fileHash = fileHash;
            this.filePath = filePath;
        }
    }
}
