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
package com.clougence.sql.mongodb.parser.ast.commands.collection;

import java.util.Collections;
import java.util.List;

import com.clougence.sql.mongodb.parser.ast.CommandBuilder;
import com.clougence.sql.mongodb.parser.ast.MongoFilterSubquery;
import com.clougence.sql.mongodb.parser.ast.MongoFilterSubqueryHost;
import com.clougence.sql.mongodb.parser.ast.MongoFuncType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountDocumentsFunc extends CollectionFunc implements MongoFilterSubqueryHost {

    private String                    filter           = "{}";
    /** Nested find expressions rewritten to placeholders inside {@link #filter}. */
    private List<MongoFilterSubquery> filterSubqueries = Collections.emptyList();

    private String                    limit;
    private String                    skip             = "0";
    private String                    hint;
    private String                    maxTimeMS;

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.COUNT_DOCUMENTS; }

    @Override
    public String toBson() {
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addOption("aggregate", getQuoteName());

        String pipeline = "[{\"$match\":" + filter + "},{$skip:" + skip + "},{\"$group\":{\"_id\":null,\"n\":{\"$sum\":1}}}]";

        commandBuilder.addOption("pipeline", pipeline);
        commandBuilder.addOption("maxTimeMS", maxTimeMS);
        commandBuilder.addOption("hint", hint);
        commandBuilder.addOption("cursor", "{}");
        return commandBuilder.toString();
    }
}
