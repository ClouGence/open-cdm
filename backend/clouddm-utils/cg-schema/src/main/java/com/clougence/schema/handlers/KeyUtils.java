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
package com.clougence.schema.handlers;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.StringUtils;

public class KeyUtils {

    public static Key[] buildMappingKey(DsType srcDsType, DsType dstDsType, MainVersion mainVersion) {
        if (mainVersion == null) {
            return new Key[] { new Key(srcDsType.getTypeName(), dstDsType.getTypeName()) };
        } else {
            return new Key[] { new Key(srcDsType.getTypeName(), dstDsType.getTypeName()), //
                               new Key(srcDsType.getTypeName(), dstDsType.getTypeName(), mainVersion.getMainVersion()) };
        }
    }

    public static Key[] buildTableKey(DsType dsType, MainVersion mainVersion) {
        if (mainVersion == null) {
            return new Key[] { new Key(dsType.getTypeName()) };
        } else {
            return new Key[] { new Key(dsType.getTypeName()), //
                               new Key(dsType.getTypeName(), mainVersion.getMainVersion()) };
        }
    }

    public static Key[] buildColumnKey(DsType dsType, String columnType, MainVersion mainVersion) {
        if (StringUtils.isBlank(columnType)) {
            return new Key[] { new Key(dsType.getTypeName()) };
        } else {
            if (mainVersion == null) {
                return new Key[] { new Key(dsType.getTypeName()), //
                                   new Key(dsType.getTypeName(), columnType) };
            } else {
                return new Key[] { new Key(dsType.getTypeName()), //
                                   new Key(dsType.getTypeName(), columnType), //
                                   new Key(dsType.getTypeName(), columnType, mainVersion.getMainVersion()) };
            }
        }
    }
}
