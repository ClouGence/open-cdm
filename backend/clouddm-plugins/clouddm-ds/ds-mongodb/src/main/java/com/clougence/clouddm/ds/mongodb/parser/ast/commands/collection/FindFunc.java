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
public class FindFunc extends CollectionFunc {

    private String filter;
    private String sort;
    private String projection;
    private String hint;
    private String skip;
    private String limit;
    //    private String batchSize;
    private String comment;
    private String maxTimeMS;
    private String readConcern;
    private String max;
    private String min;
    private String returnKey;
    private String showRecordId;
    private String allowDiskUse;
    private String allowPartialResults;
    private String collation;
    private String let;
    private String explain;

    public FindFunc(MongoFuncType type){
        super(type);
    }

    @Override
    public MongoFuncType getFuncType() { return this.funcType; }

    @Override
    public String toBson() {
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addOption("find", getQuoteName());
        commandBuilder.addOption("filter", filter);
        commandBuilder.addOption("sort", sort);
        commandBuilder.addOption("projection", projection);
        commandBuilder.addOption("hint", hint);
        commandBuilder.addOption("skip", skip);
        commandBuilder.addOption("limit", limit);
        commandBuilder.addOption("comment", comment);
        commandBuilder.addOption("maxTimeMS", maxTimeMS);
        commandBuilder.addOption("readConcern", readConcern);
        commandBuilder.addOption("max", max);
        commandBuilder.addOption("min", min);
        commandBuilder.addOption("returnKey", returnKey);
        commandBuilder.addOption("showRecordId", showRecordId);
        commandBuilder.addOption("allowDiskUse", allowDiskUse);
        commandBuilder.addOption("allowPartialResults", allowPartialResults);
        commandBuilder.addOption("collation", collation);
        commandBuilder.addOption("let", let);
        commandBuilder.addOption("explain", explain);

        return commandBuilder.toString();
    }
}
