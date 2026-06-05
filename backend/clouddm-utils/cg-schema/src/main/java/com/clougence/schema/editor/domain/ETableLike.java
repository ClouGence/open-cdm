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
package com.clougence.schema.editor.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2024/5/9 17:56:00
 */
@Getter
@Setter
public class ETableLike {

    private String catalog;

    private String schema;

    private String table;

    public ETableLike cloneLike() {
        ETableLike l = new ETableLike();
        l.setCatalog(this.catalog);
        l.setSchema(this.schema);
        l.setTable(this.table);
        return l;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ETableLike like = (ETableLike) o;

        if (catalog != null ? !catalog.equals(like.catalog) : like.catalog != null)
            return false;
        if (schema != null ? !schema.equals(like.schema) : like.schema != null)
            return false;
        return table.equals(like.table);
    }

    @Override
    public int hashCode() {
        int result = catalog != null ? catalog.hashCode() : 0;
        result = 31 * result + (schema != null ? schema.hashCode() : 0);
        result = 31 * result + table.hashCode();
        return result;
    }
}
