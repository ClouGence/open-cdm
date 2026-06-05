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
package com.clougence.adapter.dameng;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.Version;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2020/4/18 11:42
 */
public enum DmMainVersion implements MainVersion {

    DaMeng7("V7"),
    DaMeng8("V8"),;

    private final MainVersion mainVersion;

    DmMainVersion(String mainVersion){
        this.mainVersion = new Version(DsType.Dameng, mainVersion);
    }

    @Override
    public DsType getDsType() { return mainVersion.getDsType(); }

    public String getMainVersion() { return this.mainVersion.getMainVersion(); }

    public MainVersion getVersion() { return this.mainVersion; }



    //for compare,remove prefix
    private String gerVersionWithoutPrefix() {
        return this.mainVersion.getMainVersion().substring(1);
    }

    @Override
    public boolean isGe(MainVersion targetVersion) {
        if (targetVersion == null) {
            return false;
        }

        if (this.getDsType() != targetVersion.getDsType()) {
            return false;
        }

        if (targetVersion instanceof DmMainVersion) {
            Version source = new Version(this.getDsType(), this.gerVersionWithoutPrefix());
            Version target = new Version(targetVersion.getDsType(), ((DmMainVersion) targetVersion).gerVersionWithoutPrefix());
            return source.compareTo(target) >= 0;
        } else {
            return false;
        }

    }

    @Override
    public boolean isGt(MainVersion targetVersion) {
        if (targetVersion == null) {
            return false;
        }

        if (this.getDsType() != targetVersion.getDsType()) {
            return false;
        }
        if (targetVersion instanceof DmMainVersion) {
            Version source = new Version(this.getDsType(), this.gerVersionWithoutPrefix());
            Version target = new Version(targetVersion.getDsType(), ((DmMainVersion) targetVersion).gerVersionWithoutPrefix());
            return source.compareTo(target) > 0;
        } else {
            return false;
        }
    }

    @Override
    public boolean isLe(MainVersion targetVersion) {
        if (targetVersion == null) {
            return false;
        }

        if (this.getDsType() != targetVersion.getDsType()) {
            return false;
        }
        if (targetVersion instanceof DmMainVersion) {
            Version source = new Version(this.getDsType(), this.gerVersionWithoutPrefix());
            Version target = new Version(targetVersion.getDsType(), ((DmMainVersion) targetVersion).gerVersionWithoutPrefix());
            return source.compareTo(target) <= 0;
        } else {
            return false;
        }
    }

    @Override
    public boolean isLt(MainVersion targetVersion) {
        if (targetVersion == null) {
            return false;
        }

        if (this.getDsType() != targetVersion.getDsType()) {
            return false;
        }

        if (targetVersion instanceof DmMainVersion) {
            Version source = new Version(this.getDsType(), this.gerVersionWithoutPrefix());
            Version target = new Version(targetVersion.getDsType(), ((DmMainVersion) targetVersion).gerVersionWithoutPrefix());
            return source.compareTo(target) < 0;
        } else {
            return false;
        }
    }

    public static DmMainVersion parserVersion(String version) {
        if (StringUtils.isBlank(version)) {
            return null;
        }

        if (checkInVersion(version, DaMeng7)) {
            return DaMeng7;
        } else if (checkInVersion(version, DaMeng8)) {
            return DaMeng8;
        } else {
            throw new UnsupportedOperationException("Unsupported Dameng version " + version);
        }
    }

    private static boolean checkInVersion(String version, MainVersion mainVersion) {
        String referenceVersion = mainVersion.getMainVersion();
        return version.equals(referenceVersion) || StringUtils.contains(version, referenceVersion);
    }
}
