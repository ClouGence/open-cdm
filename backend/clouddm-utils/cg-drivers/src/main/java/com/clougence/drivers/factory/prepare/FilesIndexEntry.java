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
package com.clougence.drivers.factory.prepare;

import java.io.File;

import com.clougence.drivers.def.FileDef;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.FilenameUtils;

final class FilesIndexEntry {

    private static final String RELATIVE_PREFIX = "relative";
    private static final String ABSOLUTE_PREFIX = "absolute";

    private final boolean relative;
    private final String  resourceId;
    private final String  path;

    private FilesIndexEntry(boolean relative, String resourceId, String path) {
        this.relative = relative;
        this.resourceId = resourceId;
        this.path = path;
    }

    static FilesIndexEntry fromFileDef(String resourceId, File versionDir, FileDef fileDef) {
        if (fileDef == null || StringUtils.isBlank(resourceId)) {
            return null;
        }

        String relativePath = StringUtils.trimToNull(fileDef.getRelativePath());
        String absolutePath = StringUtils.trimToNull(fileDef.getAbsolutePath());
        if (relativePath != null) {
            relativePath = FilenameUtils.separatorsToUnix(relativePath);
        }

        if (relativePath != null && isVersionRelativeFile(versionDir, relativePath, absolutePath)) {
            return new FilesIndexEntry(true, resourceId, relativePath);
        }
        if (absolutePath != null) {
            return new FilesIndexEntry(false, resourceId, absolutePath);
        }
        if (relativePath != null) {
            return new FilesIndexEntry(true, resourceId, relativePath);
        }
        return null;
    }

    static FilesIndexEntry parse(String line) {
        String trimmed = StringUtils.trimToNull(line);
        if (trimmed == null) {
            return null;
        }

        int firstSeparator = trimmed.indexOf(' ');
        if (firstSeparator < 0) {
            return null;
        }

        String prefix = StringUtils.trimToNull(trimmed.substring(0, firstSeparator));
        if (prefix == null) {
            return null;
        }

        String remainder = StringUtils.trimToNull(trimmed.substring(firstSeparator + 1));
        if (remainder == null) {
            return null;
        }

        int secondSeparator = remainder.indexOf(' ');
        if (secondSeparator < 0) {
            return null;
        }

        String resourceId = StringUtils.trimToNull(remainder.substring(0, secondSeparator));
        String path = StringUtils.trimToNull(remainder.substring(secondSeparator + 1));
        if (resourceId == null || path == null) {
            return null;
        }

        if (StringUtils.equalsIgnoreCase(RELATIVE_PREFIX, prefix)) {
            return new FilesIndexEntry(true, resourceId, path);
        }
        if (StringUtils.equalsIgnoreCase(ABSOLUTE_PREFIX, prefix)) {
            return new FilesIndexEntry(false, resourceId, path);
        }
        return null;
    }

    boolean matches(String resourceId) {
        return StringUtils.equals(this.resourceId, resourceId);
    }

    FileDef toFileDef(File versionDir) {
        FileDef fileDef = new FileDef();
        if (this.relative) {
            String relativePath = FilenameUtils.separatorsToUnix(this.path);
            fileDef.setRelativePath(relativePath);
            if (versionDir != null) {
                fileDef.setAbsolutePath(new File(versionDir, relativePath).getAbsolutePath());
            }
            return fileDef;
        }

        fileDef.setAbsolutePath(this.path);
        fileDef.setRelativePath(new File(this.path).getName());
        return fileDef;
    }

    String toLine() {
        return (this.relative ? RELATIVE_PREFIX : ABSOLUTE_PREFIX) + " " + this.resourceId + " " + this.path;
    }

    private static boolean isVersionRelativeFile(File versionDir, String relativePath, String absolutePath) {
        if (versionDir == null || relativePath == null) {
            return false;
        }
        if (absolutePath == null) {
            return true;
        }
        return new File(versionDir, relativePath).getAbsolutePath().equals(new File(absolutePath).getAbsolutePath());
    }
}