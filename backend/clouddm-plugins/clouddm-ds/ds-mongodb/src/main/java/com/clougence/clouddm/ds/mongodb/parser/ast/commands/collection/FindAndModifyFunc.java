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
public class FindAndModifyFunc extends CollectionFunc {

    private String query;
    private String sort;
    private String remove;
    private String update;
    // new
    private String new_;
    private String fields;
    private String upsert;
    private String bypassDocumentValidation;
    private String writeConcern;
    private String maxTimeMS;
    private String collation;
    private String arrayFilters;
    private String hint;
    private String comment;
    private String let;

    public FindAndModifyFunc(MongoFuncType type){
        super(type);
    }

    @Override
    public MongoFuncType getFuncType() { return this.funcType; }

    @Override
    public String toBson() {
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addOption("findAndModify", getQuoteName());
        commandBuilder.addOption("query", query);
        commandBuilder.addOption("sort", sort);
        commandBuilder.addOption("remove", remove);
        commandBuilder.addOption("update", update);
        commandBuilder.addOption("new", new_);
        commandBuilder.addOption("fields", fields);
        commandBuilder.addOption("upsert", upsert);
        commandBuilder.addOption("bypassDocumentValidation", bypassDocumentValidation);
        commandBuilder.addOption("writeConcern", writeConcern);
        commandBuilder.addOption("maxTimeMS", maxTimeMS);
        commandBuilder.addOption("collation", collation);
        commandBuilder.addOption("arrayFilters", arrayFilters);
        commandBuilder.addOption("hint", hint);
        commandBuilder.addOption("comment", comment);
        commandBuilder.addOption("let", let);
        return commandBuilder.toString();
    }
}
