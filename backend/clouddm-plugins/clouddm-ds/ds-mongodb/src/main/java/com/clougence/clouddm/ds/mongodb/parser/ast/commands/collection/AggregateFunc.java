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

import com.clougence.clouddm.ds.mongodb.parser.ast.CommandBuilder;
import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;

import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.CollectionFunc;
import lombok.Setter;

@Setter
public class AggregateFunc extends CollectionFunc {

    private String pipeline = "[]";
    private String cursor   = "{}";

    private String explain;
    private String allowDiskUse;
    private String maxTimeMS;
    private String bypassDocumentValidation;
    private String readConcern;
    private String collation;
    private String hint;
    private String comment;
    private String writeConcern;
    private String let;

    @Override
    public MongoFuncType getFuncType() { return MongoFuncType.AGGREGATE; }

    @Override
    public String toBson() {
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addOption("aggregate", getQuoteName());
        commandBuilder.addOption("pipeline", pipeline);
        commandBuilder.addOption("explain", explain);
        commandBuilder.addOption("allowDiskUse", allowDiskUse);
        commandBuilder.addOption("cursor", cursor);
        commandBuilder.addOption("maxTimeMS", maxTimeMS);
        commandBuilder.addOption("bypassDocumentValidation", bypassDocumentValidation);
        commandBuilder.addOption("readConcern", readConcern);
        commandBuilder.addOption("collation", collation);
        commandBuilder.addOption("hint", hint);
        commandBuilder.addOption("comment", comment);
        commandBuilder.addOption("writeConcern", writeConcern);
        commandBuilder.addOption("let", let);
        return commandBuilder.toString();
    }
}
