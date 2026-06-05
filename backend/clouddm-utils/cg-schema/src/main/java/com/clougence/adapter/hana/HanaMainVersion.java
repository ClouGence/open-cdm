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
package com.clougence.adapter.hana;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.metadata.Version;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao
 */
@Slf4j
public enum HanaMainVersion implements MainVersion {

    Hana_1_0_SPS00("1.00.00"),
    Hana_1_0_SPS01("1.00.01"),
    Hana_1_0_SPS02("1.00.02"),
    Hana_1_0_SPS03("1.00.03"),
    Hana_1_0_SPS04("1.00.04"),
    Hana_1_0_SPS05("1.00.05"),
    Hana_1_0_SPS06("1.00.06"),
    Hana_1_0_SPS07("1.00.07"),
    Hana_1_0_SPS08("1.00.08"),
    Hana_1_0_SPS09("1.00.09"),
    Hana_1_0_SPS10("1.00.10"),
    Hana_1_0_SPS11("1.00.11"),
    Hana_1_0_SPS12("1.00.12"),
    Hana_2_0_SPS0("2.00.00"),
    Hana_2_0_SPS1("2.00.01"),
    Hana_2_0_SPS2("2.00.02"),
    Hana_2_0_SPS3("2.00.03"),
    Hana_2_0_SPS4("2.00.04"),
    Hana_2_0_SPS5("2.00.05"),
    Hana_2_0_SPS6("2.00.06"),
    Hana_2_0_SPS7("2.00.07"),
    Hana_2_0_SPS8("2.00.08");

    private final MainVersion mainVersion;

    HanaMainVersion(String mainVersion){
        this.mainVersion = new Version(DsType.Hana, mainVersion);
    }

    @Override
    public DsType getDsType() { return mainVersion.getDsType(); }

    @Override
    public String getMainVersion() { return this.mainVersion.getMainVersion(); }

    public MainVersion getVersion() { return this.mainVersion; }

    public static HanaMainVersion parserVersion(String fullVersion) {
        if (StringUtils.isBlank(fullVersion)) {
            return null;
        }

        String version = fixedVersion(fullVersion);

        for (HanaMainVersion ver : values()) {
            if (checkInVersion(version, ver)) {
                return ver;
            }
        }

        if (version.startsWith("2")) {
            return Hana_2_0_SPS8;
        } else {
            return Hana_1_0_SPS12;
        }
    }

    private static String fixedVersion(String fullVersion) {
        Pattern pattern = Pattern.compile("\\d.*");
        Matcher matcher = pattern.matcher(fullVersion);
        if (matcher.find()) {
            return matcher.group();
        }
        return fullVersion;
    }

    private static boolean checkInVersion(String version, MainVersion mainVersion) {
        String refVer = mainVersion.getMainVersion();
        return version.equals(refVer) || StringUtils.startsWithIgnoreCase(version, refVer);
    }
}
