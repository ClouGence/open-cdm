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
package com.clougence.clouddm.sdk.analysis.column;

import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealColumn {

    private String  column;
    private String  table;
    private String  schema;
    private String  catalog;

    private int     tableId;
    private boolean onlyOneColumn = true;

    private boolean skipDesensitization;

    public String toDsResPath() {
        StringBuilder resPathLike = new StringBuilder();
        if (StringUtils.isNotBlank(this.catalog)) {
            resPathLike.append("/").append(this.catalog);
        }
        if (StringUtils.isNotBlank(this.schema)) {
            resPathLike.append("/").append(this.schema);
        }
        if (StringUtils.isNotBlank(this.table)) {
            resPathLike.append("/").append(this.table);
        }
        if (StringUtils.isNotBlank(this.column)) {
            resPathLike.append("/").append(this.column);
        }

        resPathLike.append("/");
        return resPathLike.toString();
    }
}
