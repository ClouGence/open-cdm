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
package com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection;

import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountFunc extends CollectionFunc {

    private String query;
    private String limit;
    private String skip;
    private String hint;
    private String readConcern;
    private String maxTimeMS;
    private String collation;

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.COUNT; }

    @Override
    public String toBson() {
        StringBuilder sb = new StringBuilder();

        sb.append("{\"count\":\"").append(collectionName).append("\"");
        if (this.query != null) {
            sb.append(",\"query\":").append(query);
        }
        if (this.limit != null) {
            sb.append(",\"limit\":").append(limit);
        }
        if (this.skip != null) {
            sb.append(",\"skip\":").append(skip);
        }
        if (this.hint != null) {
            sb.append(",\"hint\":").append(hint);
        }
        if (this.readConcern != null) {
            sb.append(",\"readConcern\":").append(readConcern);
        }
        if (this.maxTimeMS != null) {
            sb.append(",\"maxTimeMS\":").append(maxTimeMS);
        }
        if (this.collation != null) {
            sb.append(",\"collation\":").append(collation);
        }
        sb.append("}");
        return sb.toString();
    }
}
