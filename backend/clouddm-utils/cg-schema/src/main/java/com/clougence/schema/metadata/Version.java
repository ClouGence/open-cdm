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

import java.util.Objects;

import com.clougence.schema.DsType;
import com.clougence.utils.NumberUtils;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.NonNull;

/**
 * version info
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
@Getter
public class Version implements MainVersion, Comparable<MainVersion> {

    private final DsType dsType;
    private final String mainVersion;

    public Version(DsType dsType, String mainVersion){
        this.dsType = Objects.requireNonNull(dsType, "dsType not be null.");
        this.mainVersion = mainVersion;
    }

    @Override
    public int compareTo(@NonNull MainVersion target) {
        String[] sourcePics = this.mainVersion.split("\\.");
        String[] targetPics = target.getMainVersion().split("\\.");
        int x = Math.min(sourcePics.length, targetPics.length);
        for (int i = 0; i < x; i++) {
            if (NumberUtils.isNumber(sourcePics[i]) && NumberUtils.isNumber(targetPics[i])) {
                int s = Integer.parseInt(sourcePics[i]);
                int t = Integer.parseInt(targetPics[i]);
                if (s > t) {
                    return 1;
                } else if (s < t) {
                    return -1;
                }
            } else {
                if (StringUtils.equals(sourcePics[i], targetPics[i])) {
                    return 0;
                } else {
                    return StringUtils.compare(sourcePics[i], targetPics[i]);
                }
            }
        }
        return 0;
    }

    @Override
    public String name() {
        return this.dsType.name() + "-v" + this.mainVersion;
    }
}
