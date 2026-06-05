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
package com.clougence.drivers.def;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import com.clougence.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResDef {

    private String        resourceType;
    private String        coordinate;
    private boolean       prepared;
    private List<FileDef> fileDefList;

    public long getFilesIndexId() {
        String resourceTypeValue = StringUtils.trimToEmpty(this.resourceType);
        String coordinateValue = StringUtils.trimToEmpty(this.coordinate);
        byte[] source = (resourceTypeValue + "\n" + coordinateValue).getBytes(StandardCharsets.UTF_8);
        return UUID.nameUUIDFromBytes(source).getMostSignificantBits() & Long.MAX_VALUE;
    }
}
