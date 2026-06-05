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
package com.clougence.schema.metadata;

import com.clougence.schema.DsType;
import com.clougence.utils.StringUtils;

/**
 * version info
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public interface MainVersion {

    DsType getDsType();

    String name();

    String getMainVersion();

    default boolean isEq(MainVersion targetVersion) {
        if (targetVersion == null) {
            return false;
        }

        if (this.getDsType() != targetVersion.getDsType()) {
            return false;
        }

        return StringUtils.equals(this.getMainVersion(), targetVersion.getMainVersion());
    }

    default boolean isGe(MainVersion targetVersion) {
        if (targetVersion == null) {
            return false;
        }

        if (this.getDsType() != targetVersion.getDsType()) {
            return false;
        }

        Version source = new Version(this.getDsType(), this.getMainVersion());
        Version target = new Version(targetVersion.getDsType(), targetVersion.getMainVersion());
        return source.compareTo(target) >= 0;
    }

    default boolean isGt(MainVersion targetVersion) {
        if (targetVersion == null) {
            return false;
        }

        if (this.getDsType() != targetVersion.getDsType()) {
            return false;
        }

        Version source = new Version(this.getDsType(), this.getMainVersion());
        Version target = new Version(targetVersion.getDsType(), targetVersion.getMainVersion());
        return source.compareTo(target) > 0;
    }

    default boolean isLe(MainVersion targetVersion) {
        if (targetVersion == null) {
            return false;
        }

        if (this.getDsType() != targetVersion.getDsType()) {
            return false;
        }

        Version source = new Version(this.getDsType(), this.getMainVersion());
        Version target = new Version(targetVersion.getDsType(), targetVersion.getMainVersion());
        return source.compareTo(target) <= 0;
    }

    default boolean isLt(MainVersion targetVersion) {
        if (targetVersion == null) {
            return false;
        }

        if (this.getDsType() != targetVersion.getDsType()) {
            return false;
        }

        Version source = new Version(this.getDsType(), this.getMainVersion());
        Version target = new Version(targetVersion.getDsType(), targetVersion.getMainVersion());
        return source.compareTo(target) < 0;
    }
}
