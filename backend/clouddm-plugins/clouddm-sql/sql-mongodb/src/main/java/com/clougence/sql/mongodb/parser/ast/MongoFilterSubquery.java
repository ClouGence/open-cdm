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
package com.clougence.sql.mongodb.parser.ast;

import com.clougence.sql.mongodb.parser.ast.commands.collection.FindFunc;

import lombok.Getter;
import lombok.Setter;

/**
 * Nested find expression embedded in a filter/query value, e.g.
 * {@code $in: db.coll.find(...).sort(...).limit(n).map(d => d._id)}.
 */
@Getter
@Setter
public class MongoFilterSubquery {

    public static final String PLACEHOLDER_PREFIX = "__CDM_SQ_";
    public static final String PLACEHOLDER_SUFFIX = "__";

    private String             collectionName;
    private String             filter;
    private String             projection;
    private String             sort;
    private String             skip;
    private String             limit;
    /** Dotted field path extracted by map, e.g. {@code _id} from {@code d => d._id}. */
    private String             mapField;

    public static String placeholder(int index) {
        return PLACEHOLDER_PREFIX + index + PLACEHOLDER_SUFFIX;
    }

    public FindFunc toFindFunc() {
        FindFunc findFunc = new FindFunc(MongoFuncType.FIND);
        findFunc.setCollectionName(this.collectionName);
        findFunc.setFilter(this.filter);
        findFunc.setProjection(this.projection);
        findFunc.setSort(this.sort);
        findFunc.setSkip(this.skip);
        findFunc.setLimit(this.limit);
        return findFunc;
    }
}
