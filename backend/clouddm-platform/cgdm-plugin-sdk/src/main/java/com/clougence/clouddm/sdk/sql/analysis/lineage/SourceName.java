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
package com.clougence.clouddm.sdk.sql.analysis.lineage;

import com.clougence.utils.StringUtils;

/**
 * Structured name of a source column.
 *
 * <p>The source range is end-exclusive and uses coordinates in the analyzed SQL.
 * Lines are one-based and columns are zero-based. A zero-valued range means that
 * the lineage implementation cannot provide the source location.</p>
 */
public record SourceName(String catalog, String schema, String table, String column,//
                         int startLine, int startColumn, int endLine, int endColumn) {

    public SourceName(String catalog, String schema, String table, String column){
        this(catalog, schema, table, column, 0, 0, 0, 0);
    }

    public String toDsResPath() {
        StringBuilder resPathLike = new StringBuilder();
        if (StringUtils.isNotBlank(this.catalog())) {
            resPathLike.append("/").append(this.catalog());
        }
        if (StringUtils.isNotBlank(this.schema())) {
            resPathLike.append("/").append(this.schema());
        }
        if (StringUtils.isNotBlank(this.table())) {
            resPathLike.append("/").append(this.table());
        }
        if (StringUtils.isNotBlank(this.column())) {
            resPathLike.append("/").append(this.column());
        }

        resPathLike.append("/");
        return resPathLike.toString();
    }

    public String toLocatedDsResPath() {
        return "(" + this.startLine + ":" + this.startColumn + "~" + this.endLine + ":" + this.endColumn + ") " + this.toDsResPath();
    }
}
