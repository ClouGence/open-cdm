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
package com.clougence.clouddm.sdk.model.analysis.resource;

import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdbResObject extends ResObject {

    private String catalog;
    private String schema;
    private String table;

    public RdbResObject(){
    }

    public RdbResObject(String catalog, String schema, String table){
        this.catalog = catalog;
        this.schema = schema;
        this.table = table;
    }

    @Override
    public String getName() {
        if (this.getType() != null) {
            switch (this.getType()) {
                case Catalog:
                    return this.catalog;
                case Schema:
                    return this.schema;
                case Table:
                case View:
                    return this.table;
                case ConfigKey:
                    return this.name;
                case Column:
                case Index:
                case Constraint:
                case Sequence:
                case Function:
                case Procedure:
                case Trigger:
                case Synonym:
                default:
                    break;
            }
        }

        return super.getName();
    }

    public boolean hasCatalog() {
        return StringUtils.isNotBlank(this.catalog);
    }

    public boolean hasSchema() {
        return StringUtils.isNotBlank(this.schema);
    }

    public boolean hasTable() {
        return StringUtils.isNotBlank(this.table);
    }

    @Override
    public DsResPath toDsResPath() {
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
        if (StringUtils.isNotBlank(this.name)) {
            resPathLike.append("/").append(this.name);
        }

        resPathLike.append("/");
        return new DsResPathObj(resPathLike.toString());
    }
}
