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
package com.clougence.clouddm.ds.mongodb.parser;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicLong;

import org.antlr.v4.runtime.TokenStream;

import com.clougence.clouddm.ds.mongodb.parser.antlr.MongoParser;
import com.clougence.clouddm.ds.mongodb.parser.antlr.MongoParserVisitor;
import com.clougence.clouddm.ds.mongodb.parser.ast.MongoConstant;
import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncSet;
import com.clougence.clouddm.ds.mongodb.parser.ast.MongoFuncType;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.AbstractMongoFunc;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.client.UserFunc;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.collection.*;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.db.*;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.info.ShowCollectionsFunc;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.info.ShowDatabasesFunc;
import com.clougence.dslpaser.antlr.AbstractLocationParseTreeVisitor;

public class DefaultMongoParserVisitor extends AbstractLocationParseTreeVisitor<Object> implements MongoParserVisitor<Object> {

    private final Stack<Object> instStack = new Stack<>();
    private final AtomicLong    counter   = new AtomicLong(0);
    private final TokenStream   tokenStream;

    public DefaultMongoParserVisitor(TokenStream tokenStream){
        this.tokenStream = tokenStream;
    }

    @Override
    public Object visitRoot(MongoParser.RootContext ctx) {
        this.instStack.push(code(new MongoFuncSet(), ctx));
        this.visitChildren(ctx);
        return this.instStack.pop();
    }

    @Override
    public Object visitCommand(MongoParser.CommandContext ctx) {
        visitChildren(ctx);
        AbstractMongoFunc instSet = (AbstractMongoFunc) this.instStack.pop();
        MongoFuncSet rootBlockSet = (MongoFuncSet) this.instStack.peek();
        rootBlockSet.add(instSet);

        return null;
    }

    @Override
    public Object visitFunctionCommand(MongoParser.FunctionCommandContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Object visitShowDatabases(MongoParser.ShowDatabasesContext ctx) {
        this.instStack.push(new ShowDatabasesFunc());
        return null;
    }

    @Override
    public Object visitShowCollections(MongoParser.ShowCollectionsContext ctx) {
        this.instStack.push(new ShowCollectionsFunc());
        return null;
    }

    @Override
    public Object visitExplain(MongoParser.ExplainContext ctx) {
        ctx.explainMethod().accept(this);
        CollectionFunc func = (CollectionFunc) this.instStack.pop();
        ExplainFunc explainFunc = new ExplainFunc(func);
        if (ctx.verbosity != null) {
            explainFunc.setVerbosity(ctx.verbosity.getText());
        }
        this.instStack.push(explainFunc);
        return null;
    }

    @Override
    public Object visitExplainMethod(MongoParser.ExplainMethodContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Object visitDbAggregate(MongoParser.DbAggregateContext ctx) {
        AggregateFunc func = new DbAggregateFunc();
        func.setCollectionName("1");
        if (ctx.arr() != null) {
            func.setPipeline(ctx.arr().getText());
        }
        if (ctx.options != null) {
            for (MongoParser.PairContext pair : ctx.options.pair()) {
                String key = getString(pair.key().getText());
                if (MongoConstant.HINT.equals(key)) {
                    func.setHint(pair.value().getText());
                } else if (MongoConstant.READ_CONCERN.equals(key)) {
                    func.setReadConcern(pair.value().getText());
                } else if (MongoConstant.MAX_TIME_MS.equals(key)) {
                    func.setMaxTimeMS(pair.value().getText());
                } else if (MongoConstant.COLLATION.equals(key)) {
                    func.setCollation(pair.value().getText());
                } else if (MongoConstant.LET.equals(key)) {
                    func.setLet(pair.value().getText());
                } else if (MongoConstant.ALLOW_DISK_USE.equals(key)) {
                    func.setAllowDiskUse(pair.value().getText());
                } else if (MongoConstant.COMMENT.equals(key)) {
                    func.setComment(pair.value().getText());
                } else if (MongoConstant.WRITE_CONCERN.equals(key)) {
                    func.setWriteConcern(pair.value().getText());
                } else if (MongoConstant.BYPASS_DOCUMENT_VALIDATION.equals(key)) {
                    func.setBypassDocumentValidation(pair.value().getText());
                } else if (MongoConstant.EXPLAIN.equals(key)) {
                    func.setExplain(pair.value().getText());
                }
            }
        }

        this.instStack.push(code(func, ctx));
        return null;
    }

    @Override
    public Object visitAggregate(MongoParser.AggregateContext ctx) {
        AggregateFunc func = new AggregateFunc();
        if (ctx.arr() != null) {
            func.setPipeline(ctx.arr().getText());
        }
        if (ctx.options != null) {
            for (MongoParser.PairContext pair : ctx.options.pair()) {
                String key = getString(pair.key().getText());
                if (MongoConstant.HINT.equals(key)) {
                    func.setHint(pair.value().getText());
                } else if (MongoConstant.READ_CONCERN.equals(key)) {
                    func.setReadConcern(pair.value().getText());
                } else if (MongoConstant.MAX_TIME_MS.equals(key)) {
                    func.setMaxTimeMS(pair.value().getText());
                } else if (MongoConstant.COLLATION.equals(key)) {
                    func.setCollation(pair.value().getText());
                } else if (MongoConstant.LET.equals(key)) {
                    func.setLet(pair.value().getText());
                } else if (MongoConstant.ALLOW_DISK_USE.equals(key)) {
                    func.setAllowDiskUse(pair.value().getText());
                } else if (MongoConstant.COMMENT.equals(key)) {
                    func.setComment(pair.value().getText());
                } else if (MongoConstant.WRITE_CONCERN.equals(key)) {
                    func.setWriteConcern(pair.value().getText());
                } else if (MongoConstant.BYPASS_DOCUMENT_VALIDATION.equals(key)) {
                    func.setBypassDocumentValidation(pair.value().getText());
                } else if (MongoConstant.EXPLAIN.equals(key)) {
                    func.setExplain(pair.value().getText());
                }
            }
        }

        this.instStack.push(code(func, ctx));
        return null;
    }

    @Override
    public Object visitCreateIndexes(MongoParser.CreateIndexesContext ctx) {
        CreateIndexFunc func = new CreateIndexFunc(MongoFuncType.CREATE_INDEXES);

        if (ctx.keys.value().isEmpty()) {
            throw new RuntimeException("CreateIndexFunc must have keys");
        }

        for (MongoParser.ValueContext valueContext : ctx.keys.value()) {
            if (valueContext.obj() == null) {
                throw new RuntimeException("Keys must have be OBJ type");
            }

            LinkedHashMap<String, String> columns = new LinkedHashMap<>();
            for (MongoParser.PairContext context : valueContext.obj().pair()) {
                String key = context.key().getText();
                String value = context.value().getText();
                columns.put(key, value);
            }
            func.getKeys().add(columns);
        }

        if (ctx.options != null) {
            for (MongoParser.PairContext pair : ctx.options.pair()) {
                func.getOptions().put(getString(pair.key().getText()), pair.value().getText());
            }
        }

        if (ctx.commitQuorum != null) {
            func.setCommitQuorum(ctx.commitQuorum.getText());
        }

        this.instStack.push(code(func, ctx));
        return null;
    }

    @Override
    public Object visitDistinct(MongoParser.DistinctContext ctx) {
        DistinctFunc distFunc = new DistinctFunc();
        distFunc.setField(ctx.field.getText());
        if (ctx.option != null) {
            for (MongoParser.PairContext pairContext : ctx.option.pair()) {
                String text = pairContext.key().getText();
                text = getString(text);
                if (text.equals(MongoConstant.COLLATION)) {
                    distFunc.setCollation(pairContext.value().getText());
                }
            }
        }

        if (ctx.query != null) {
            distFunc.setQuery(ctx.query.getText());
        }
        this.instStack.push(code(distFunc, ctx));
        return null;
    }

    @Override
    public Object visitCreateIndex(MongoParser.CreateIndexContext ctx) {
        CreateIndexFunc func = new CreateIndexFunc(MongoFuncType.CREATE_INDEX);

        if (ctx.keys.pair().isEmpty()) {
            throw new RuntimeException("CreateIndexFunc must have keys");
        }

        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        for (MongoParser.PairContext pairContext : ctx.keys.pair()) {
            String key = pairContext.key().getText();
            String value = pairContext.value().getText();
            columns.put(key, value);

        }

        func.getKeys().add(columns);
        if (ctx.options != null) {
            for (MongoParser.PairContext pair : ctx.options.pair()) {
                func.getOptions().put(getString(pair.key().getText()), pair.value().getText());
            }
        }

        if (ctx.commitQuorum != null) {
            func.setCommitQuorum(ctx.commitQuorum.getText());
        }

        this.instStack.push(code(func, ctx));
        return null;
    }

    @Override
    public Object visitCountDocuments(MongoParser.CountDocumentsContext ctx) {
        CountDocumentsFunc func = new CountDocumentsFunc();
        if (ctx.filter != null) {
            func.setFilter(ctx.filter.getText());
        }
        if (ctx.option != null) {
            for (MongoParser.PairContext pairContext : ctx.option.pair()) {
                String key = getString(pairContext.key().getText());
                if (MongoConstant.HINT.equals(key)) {
                    func.setHint(pairContext.value().getText());
                } else if (MongoConstant.MAX_TIME_MS.equals(key)) {
                    func.setMaxTimeMS(pairContext.value().getText());
                } else if (MongoConstant.SKIP.equals(key)) {
                    func.setSkip(pairContext.value().getText());
                } else if (MongoConstant.LIMIT.equals(key)) {
                    func.setLimit(pairContext.value().getText());
                }
            }
        }

        this.instStack.push(code(func, ctx));
        return null;
    }

    @Override
    public Object visitDbServerBuildInfo(MongoParser.DbServerBuildInfoContext ctx) {
        BuildInfoFunc buildInfoFunc = new BuildInfoFunc();
        this.instStack.push(buildInfoFunc);
        return null;
    }

    @Override
    public Object visitUse(MongoParser.UseContext ctx) {
        UserFunc userFunc = new UserFunc();
        userFunc.setDatabase(ctx.ID().getText());
        this.instStack.push(userFunc);
        return null;
    }

    @Override
    public Object visitDbHello(MongoParser.DbHelloContext ctx) {
        this.instStack.push(new HelloFunc());
        return null;
    }

    @Override
    public Object visitGetCollectionNames(MongoParser.GetCollectionNamesContext ctx) {
        ListCollectionsFunc listCollectionsFunc = new ListCollectionsFunc();
        listCollectionsFunc.addOption("authorizedCollections", "true");
        listCollectionsFunc.addOption("nameOnly", "true");
        this.instStack.push(listCollectionsFunc);
        return null;
    }

    @Override
    public Object visitGetLogComponents(MongoParser.GetLogComponentsContext ctx) {
        this.instStack.push(new GetLogComponentsFunc());
        return null;
    }

    @Override
    public Object visitGetCollectionInfos(MongoParser.GetCollectionInfosContext ctx) {
        ListCollectionsFunc listCollectionsFunc = new ListCollectionsFunc();
        if (ctx.filter != null) {
            listCollectionsFunc.setFilter(ctx.filter.getText());
        }

        if (ctx.options != null) {
            ctx.options.pair().forEach(pair -> {
                listCollectionsFunc.addOption(getString(pair.key().getText()), pair.value().getText());
            });
        }
        this.instStack.push(listCollectionsFunc);
        return null;
    }

    @Override
    public Object visitFsyncUnlock(MongoParser.FsyncUnlockContext ctx) {
        this.instStack.push(new FsyncUnlockFunc());
        return null;
    }

    @Override
    public Object visitFsyncLock(MongoParser.FsyncLockContext ctx) {
        this.instStack.push(new FsyncLockFunc());
        return null;
    }

    @Override
    public Object visitDbDropDatabase(MongoParser.DbDropDatabaseContext ctx) {
        this.instStack.push(new DropDatabaseFunc());
        return null;
    }

    @Override
    public Object visitCurrentOp(MongoParser.CurrentOpContext ctx) {
        CurrentOpFunc currentOpFunc = new CurrentOpFunc();
        if (ctx.options != null) {
            for (MongoParser.PairContext pair : ctx.options.pair()) {
                currentOpFunc.addOption(getString(pair.key().getText()), pair.value().getText());
            }
        }
        this.instStack.push(currentOpFunc);
        return null;
    }

    @Override
    public Object visitDbServerStatus(MongoParser.DbServerStatusContext ctx) {
        this.instStack.push(new ServerStatusFunc());
        return null;
    }

    @Override
    public Object visitCollectionFunction(MongoParser.CollectionFunctionContext ctx) {

        ctx.collectionMethod().accept(this);
        CollectionFunc func = (CollectionFunc) this.instStack.peek();
        String name = (String) ctx.collectionName().accept(this);

        func.setCollectionName(getString(name));
        return null;
    }

    @Override
    public Object visitCollectionMethod(MongoParser.CollectionMethodContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public Object visitInsert(MongoParser.InsertContext ctx) {
        InsertFunc func = new InsertFunc(MongoFuncType.INSERT);

        if (ctx.document != null) {
            func.setDocuments("[" + ctx.document.getText() + "]");
        } else {
            func.setDocuments(ctx.documents.getText());
        }

        if (ctx.option != null) {
            ctx.option.pair().forEach((pairContext -> {
                String key = getString(pairContext.key().getText());
                if (MongoConstant.WRITE_CONCERN.equals(key)) {
                    func.setWriteConcern(pairContext.value().getText());
                } else if (MongoConstant.ORDERED.equals(key)) {
                    func.setOrdered(ctx.option.getText());
                }
            }));
        }

        this.instStack.push(func);
        return null;
    }

    @Override
    public Object visitInsertOne(MongoParser.InsertOneContext ctx) {
        InsertFunc insertOneFunc = new InsertFunc(MongoFuncType.INSERT_ONE);

        insertOneFunc.setDocuments("[" + ctx.document.getText() + "]");
        if (ctx.option != null) {
            ctx.option.pair().forEach((pairContext -> {
                String key = getString(pairContext.key().getText());
                if (MongoConstant.WRITE_CONCERN.equals(key)) {
                    insertOneFunc.setWriteConcern(pairContext.value().getText());
                }
            }));
        }

        this.instStack.push(insertOneFunc);
        return null;
    }

    @Override
    public Object visitInsertMany(MongoParser.InsertManyContext ctx) {
        InsertFunc func = new InsertFunc(MongoFuncType.INSERT_MANY);

        func.setDocuments(ctx.documents.getText());

        if (ctx.option != null) {
            ctx.option.pair().forEach((pairContext -> {
                String key = getString(pairContext.key().getText());
                if (MongoConstant.WRITE_CONCERN.equals(key)) {
                    func.setWriteConcern(pairContext.value().getText());
                } else if (MongoConstant.ORDERED.equals(key)) {
                    func.setOrdered(ctx.option.getText());
                }
            }));
        }

        this.instStack.push(func);
        return null;
    }

    private String getString(String text) {
        if (text.startsWith("\"") || text.startsWith("'")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }

    @Override
    public Object visitDropIndex(MongoParser.DropIndexContext ctx) {
        DropIndexesFunc dropIndexesFunc = new DropIndexesFunc(MongoFuncType.DROP_INDEX);
        if (ctx.STRING() != null) {
            dropIndexesFunc.setIndex(ctx.STRING().getText());
        } else {
            dropIndexesFunc.setIndex(ctx.obj().getText());
        }

        this.instStack.push(code(dropIndexesFunc, ctx));
        return null;
    }

    @Override
    public Object visitCount(MongoParser.CountContext ctx) {
        CountFunc func = new CountFunc();
        if (ctx.filter != null) {
            func.setQuery(ctx.filter.getText());
        }
        if (ctx.option != null) {
            for (MongoParser.PairContext pair : ctx.option.pair()) {
                String key = getString(pair.key().getText());
                if (MongoConstant.LIMIT.equals(key)) {
                    func.setLimit(pair.value().getText());
                } else if (MongoConstant.SKIP.equals(key)) {
                    func.setSkip(pair.value().getText());
                } else if (MongoConstant.HINT.equals(key)) {
                    func.setHint(pair.value().getText());
                } else if (MongoConstant.READ_CONCERN.equals(key)) {
                    func.setReadConcern(pair.value().getText());
                } else if (MongoConstant.MAX_TIME_MS.equals(key)) {
                    func.setMaxTimeMS(pair.value().getText());
                } else if (MongoConstant.COLLATION.equals(key)) {
                    func.setCollation(pair.value().getText());
                }
            }
        }

        this.instStack.push(code(func, ctx));
        return null;
    }

    @Override
    public Object visitDropIndexes(MongoParser.DropIndexesContext ctx) {
        DropIndexesFunc dropIndexesFunc = new DropIndexesFunc(MongoFuncType.DROP_INDEXES);
        if (ctx.jsonString() != null) {
            dropIndexesFunc.setIndex(ctx.jsonString().getText());
        }
        this.instStack.push(code(dropIndexesFunc, ctx));
        return null;
    }

    @Override
    public Object visitDrop(MongoParser.DropContext ctx) {
        DropCollectionFunction func = new DropCollectionFunction();
        if (ctx.option != null) {
            for (MongoParser.PairContext pair : ctx.option.pair()) {
                String text = pair.key().getText();
                text = getString(text);
                func.getOptions().put(text, pair.value().getText());
            }
        }

        this.instStack.push(func);
        return null;
    }

    @Override
    public Object visitDbCreateCollection(MongoParser.DbCreateCollectionContext ctx) {
        CreateFunc createFunc = new CreateFunc(ctx.STRING().getText(), MongoFuncType.CREATE_COLLECTION);
        if (ctx.options != null) {
            for (MongoParser.PairContext pair : ctx.options.pair()) {
                createFunc.addOption(getString(pair.key().getText()), pair.value().getText());
            }
        }
        this.instStack.push(createFunc);
        return null;
    }

    @Override
    public Object visitDbCreateView(MongoParser.DbCreateViewContext ctx) {
        CreateFunc createFunc = new CreateFunc(ctx.viewName.getText(), MongoFuncType.CREATE_VIEW);
        if (ctx.options != null) {
            for (MongoParser.PairContext pair : ctx.options.pair()) {
                createFunc.addOption(getString(pair.key().getText()), pair.value().getText());
            }
        }
        createFunc.addOption("pipeline", ctx.pipeline.getText());
        createFunc.addOption("viewOn", ctx.sourceName.getText());
        this.instStack.push(createFunc);
        return null;
    }

    @Override
    public Object visitLatencyStats(MongoParser.LatencyStatsContext ctx) {
        LatencyStatsFunc latencyStatsFunc = new LatencyStatsFunc();
        if (ctx.obj() != null) {
            latencyStatsFunc.setOption(ctx.obj().getText());
        }
        this.instStack.push(code(latencyStatsFunc, ctx));
        return null;
    }

    @Override
    public Object visitRenameCollection(MongoParser.RenameCollectionContext ctx) {
        RenameCollectionFunc func = new RenameCollectionFunc();
        func.setTo(getString(ctx.STRING().getText()));
        if (ctx.dropTarget != null) {
            func.setDropTarget(Boolean.parseBoolean(ctx.dropTarget.getText()));
        }
        this.instStack.push(code(func, ctx));
        return null;
    }

    @Override
    public Object visitHideIndex(MongoParser.HideIndexContext ctx) {
        AlterIndexFunc alterIndexFunc = new AlterIndexFunc();
        if (ctx.name != null) {
            alterIndexFunc.setName(ctx.name.getText());
        } else {
            alterIndexFunc.setKeyPattern(ctx.partten.getText());
        }
        if (ctx.HIDE_INDEX() != null) {
            alterIndexFunc.setHidden(true);
        } else {
            alterIndexFunc.setHidden(false);
        }
        this.instStack.push(code(alterIndexFunc, ctx));
        return null;
    }

    @Override
    public Object visitGetIndexes(MongoParser.GetIndexesContext ctx) {
        this.instStack.push(new ListIndexesFunc());
        return null;
    }

    @Override
    public Object visitCollectionName(MongoParser.CollectionNameContext ctx) {
        if (ctx.ID() != null) {
            return ctx.ID().getText();
        } else {
            return ctx.STRING().getText();
        }
    }

    @Override
    public Object visitFind(MongoParser.FindContext ctx) {
        FindFunc func = new FindFunc(MongoFuncType.FIND);
        if (ctx.filter != null) {
            func.setFilter(ctx.filter.getText());
        }
        if (ctx.projection != null) {
            func.setProjection(ctx.projection.getText());
        }
        if (ctx.options != null) {
            for (MongoParser.PairContext pair : ctx.options.pair()) {
                String key = getString(pair.key().getText());
                if (MongoConstant.LIMIT.equals(key)) {
                    func.setLimit(pair.value().getText());
                } else if (MongoConstant.SKIP.equals(key)) {
                    func.setSkip(pair.value().getText());
                } else if (MongoConstant.HINT.equals(key)) {
                    func.setHint(pair.value().getText());
                } else if (MongoConstant.READ_CONCERN.equals(key)) {
                    func.setReadConcern(pair.value().getText());
                } else if (MongoConstant.MAX_TIME_MS.equals(key)) {
                    func.setMaxTimeMS(pair.value().getText());
                } else if (MongoConstant.COLLATION.equals(key)) {
                    func.setCollation(pair.value().getText());
                } else if (MongoConstant.MAX.equals(key)) {
                    func.setMax(pair.value().getText());
                } else if (MongoConstant.MIN.equals(key)) {
                    func.setMin(pair.value().getText());
                } else if (MongoConstant.RETURN_KEY.equals(key)) {
                    func.setReturnKey(pair.value().getText());
                } else if (MongoConstant.SHOW_RECORD_ID.equals(key)) {
                    func.setShowRecordId(pair.value().getText());
                } else if (MongoConstant.SORT.equals(key)) {
                    func.setSort(pair.value().getText());
                } else if (MongoConstant.LET.equals(key)) {
                    func.setLet(pair.value().getText());
                } else if (MongoConstant.PROJECTION.equals(key)) {
                    func.setProjection(pair.value().getText());
                } else if (MongoConstant.ALLOW_DISK_USE.equals(key)) {
                    func.setAllowDiskUse(pair.value().getText());
                } else if (MongoConstant.ALLOW_PARTIAL_RESULTS.equals(key)) {
                    func.setAllowPartialResults(pair.value().getText());
                } else if (MongoConstant.COMMENT.equals(key)) {
                    func.setComment(pair.value().getText());
                } else if (MongoConstant.EXPLAIN.equals(key)) {
                    func.setExplain(pair.value().getText());
                }
            }
        }
        this.instStack.push(code(func, ctx));
        for (MongoParser.FindConstarintContext context : ctx.findConstarint()) {
            context.accept(this);
        }

        return null;
    }

    @Override
    public Object visitFindCommentConstraint(MongoParser.FindCommentConstraintContext ctx) {
        FindFunc func = (FindFunc) this.instStack.peek();
        func.setComment(ctx.jsonString().getText());
        return null;
    }

    @Override
    public Object visitExplainConstraint(MongoParser.ExplainConstraintContext ctx) {
        FindFunc func = (FindFunc) this.instStack.peek();
        if (ctx.verbosity != null) {
            func.setExplain(ctx.verbosity.getText());
        } else {
            func.setExplain("true");
        }
        return null;
    }

    @Override
    public Object visitDataSize(MongoParser.DataSizeContext ctx) {
        this.instStack.push(new DataSizeFunc());
        return null;
    }

    @Override
    public Object visitFindMaxTimeMSConstriant(MongoParser.FindMaxTimeMSConstriantContext ctx) {
        FindFunc func = (FindFunc) this.instStack.peek();
        func.setMaxTimeMS(ctx.NUMBER().getText());
        return null;
    }

    @Override
    public Object visitFindCollationConstraint(MongoParser.FindCollationConstraintContext ctx) {
        FindFunc func = (FindFunc) this.instStack.peek();
        func.setCollation(ctx.jsonString().getText());
        return null;
    }

    @Override
    public Object visitFindAllowDiskUseConstraint(MongoParser.FindAllowDiskUseConstraintContext ctx) {
        FindFunc func = (FindFunc) this.instStack.peek();
        func.setAllowDiskUse(ctx.bool.getText());
        return null;

    }

    @Override
    public Object visitFindMaxConstraint(MongoParser.FindMaxConstraintContext ctx) {
        FindFunc func = (FindFunc) this.instStack.peek();
        func.setMax(ctx.jsonString().getText());
        return null;
    }

    @Override
    public Object visitFindMinConstraint(MongoParser.FindMinConstraintContext ctx) {
        FindFunc func = (FindFunc) this.instStack.peek();
        func.setMin(ctx.jsonString().getText());
        return null;
    }

    @Override
    public Object visitFindReturnKeyConstraint(MongoParser.FindReturnKeyConstraintContext ctx) {
        FindFunc func = (FindFunc) this.instStack.peek();
        func.setReturnKey(ctx.bool.getText());
        return null;
    }

    @Override
    public Object visitFindShowRecordIdConstraint(MongoParser.FindShowRecordIdConstraintContext ctx) {
        FindFunc func = (FindFunc) this.instStack.peek();
        func.setShowRecordId(ctx.bool.getText());
        return null;
    }

    @Override
    public Object visitFindLetConstraint(MongoParser.FindLetConstraintContext ctx) {
        FindFunc func = (FindFunc) this.instStack.peek();
        func.setLet(ctx.jsonString().getText());
        return null;
    }

    @Override
    public Object visitFindSortConstraint(MongoParser.FindSortConstraintContext ctx) {
        FindFunc func = (FindFunc) this.instStack.peek();
        func.setSort(ctx.jsonString().getText());
        return null;
    }

    @Override
    public Object visitFindLimitConstraint(MongoParser.FindLimitConstraintContext ctx) {
        FindFunc func = (FindFunc) this.instStack.peek();
        func.setLimit(ctx.NUMBER().getText());
        return null;
    }

    @Override
    public Object visitFindSkipConstraint(MongoParser.FindSkipConstraintContext ctx) {
        FindFunc func = (FindFunc) this.instStack.peek();
        func.setSkip(ctx.NUMBER().getText());
        return null;
    }

    @Override
    public Object visitFindOne(MongoParser.FindOneContext ctx) {
        FindFunc func = new FindFunc(MongoFuncType.FIND_ONE);
        func.setLimit("1");
        if (ctx.filter != null) {
            func.setFilter(ctx.filter.getText());
        }
        if (ctx.projection != null) {
            func.setProjection(ctx.projection.getText());
        }
        if (ctx.options != null) {
            for (MongoParser.PairContext pair : ctx.options.pair()) {
                String key = getString(pair.key().getText());
                if (MongoConstant.SKIP.equals(key)) {
                    func.setSkip(pair.value().getText());
                } else if (MongoConstant.HINT.equals(key)) {
                    func.setHint(pair.value().getText());
                } else if (MongoConstant.READ_CONCERN.equals(key)) {
                    func.setReadConcern(pair.value().getText());
                } else if (MongoConstant.MAX_TIME_MS.equals(key)) {
                    func.setMaxTimeMS(pair.value().getText());
                } else if (MongoConstant.COLLATION.equals(key)) {
                    func.setCollation(pair.value().getText());
                } else if (MongoConstant.MAX.equals(key)) {
                    func.setMax(pair.value().getText());
                } else if (MongoConstant.MIN.equals(key)) {
                    func.setMin(pair.value().getText());
                } else if (MongoConstant.RETURN_KEY.equals(key)) {
                    func.setReturnKey(pair.value().getText());
                } else if (MongoConstant.SHOW_RECORD_ID.equals(key)) {
                    func.setShowRecordId(pair.value().getText());
                } else if (MongoConstant.SORT.equals(key)) {
                    func.setSort(pair.value().getText());
                } else if (MongoConstant.LET.equals(key)) {
                    func.setLet(pair.value().getText());
                } else if (MongoConstant.PROJECTION.equals(key)) {
                    func.setProjection(pair.value().getText());
                }
            }
        }
        this.instStack.push(code(func, ctx));
        //        for (MongoParser.FindConstarintContext context : ctx.findConstarint()) {
        //            context.accept(this);
        //        }

        return null;
    }

    @Override
    public Object visitReplaceOne(MongoParser.ReplaceOneContext ctx) {
        boolean anyMatch = ctx.replacement.pair().stream().map(MongoParser.PairContext::key).anyMatch(k -> {
            return getString(k.getText()).startsWith("$");
        });
        if (anyMatch) {
            throw new UnsupportedOperationException("Replacement document must not contain atomic operators");
        }
        UpdateFunc func = new UpdateFunc(MongoFuncType.REPLACE_ONE, false);
        func.setFilter(ctx.filter.getText());
        func.setUpdate(ctx.replacement.getText());

        if (ctx.options != null) {
            for (MongoParser.PairContext pair : ctx.options.pair()) {
                String key = getString(pair.key().getText());
                if (MongoConstant.HINT.equals(key)) {
                    func.setHint(pair.value().getText());
                } else if (MongoConstant.COLLATION.equals(key)) {
                    func.setCollation(pair.value().getText());
                } else if (MongoConstant.WRITE_CONCERN.equals(key)) {
                    func.setWriteConcern(pair.value().getText());
                } else if (MongoConstant.UPSERT.equals(key)) {
                    func.setUpsert(pair.value().getText());
                } else if (MongoConstant.SORT.equals(key)) {
                    func.setSort(pair.value().getText());
                }
            }
        }

        this.instStack.push(code(func, ctx));
        return null;
    }

    @Override
    public Object visitUpdate(MongoParser.UpdateContext ctx) {
        UpdateFunc func = null;
        if (ctx.UPDATE_ONE() != null) {
            func = new UpdateFunc(MongoFuncType.UPDATE_ONE, false);
        } else if (ctx.UPDATE_MANY() != null) {
            func = new UpdateFunc(MongoFuncType.UPDATE_MANY, true);
        } else {
            func = new UpdateFunc(MongoFuncType.UPDATE, false);
        }

        func.setFilter(ctx.filter.getText());
        if (ctx.objUpdate != null) {
            boolean anyMatch = ctx.objUpdate.pair().stream().map(MongoParser.PairContext::key).anyMatch(k -> {
                return !getString(k.getText()).startsWith("$");
            });
            if (anyMatch) {
                throw new UnsupportedOperationException("Update document requires atomic operators");
            }
            func.setUpdate(ctx.objUpdate.getText());
        } else {
            boolean anyMatch = ctx.arrUpdate.value().stream().map(MongoParser.ValueContext::obj).map(MongoParser.ObjContext::pair).flatMap(Collection::stream).anyMatch(k -> {
                return !getString(k.getText()).startsWith("$");
            });
            if (anyMatch) {
                throw new UnsupportedOperationException("Update document requires atomic operators");
            }
            func.setUpdate(ctx.arrUpdate.getText());
        }

        if (ctx.option != null) {
            for (MongoParser.PairContext pair : ctx.option.pair()) {
                String key = getString(pair.key().getText());
                if (MongoConstant.HINT.equals(key)) {
                    func.setHint(pair.value().getText());
                } else if (MongoConstant.MAX_TIME_MS.equals(key)) {
                    func.setMaxTimeMS(pair.value().getText());
                } else if (MongoConstant.COLLATION.equals(key)) {
                    func.setCollation(pair.value().getText());
                } else if (MongoConstant.LET.equals(key)) {
                    func.setLet(pair.value().getText());
                } else if (MongoConstant.WRITE_CONCERN.equals(key)) {
                    func.setWriteConcern(pair.value().getText());
                } else if (MongoConstant.UPSERT.equals(key)) {
                    func.setUpsert(pair.value().getText());
                } else if (MongoConstant.ARRAY_FILTERS.equals(key)) {
                    func.setArrayFilters(pair.value().getText());
                } else if (MongoConstant.BYPASS_DOCUMENT_VALIDATION.equals(key)) {
                    func.setBypassDocumentValidation(pair.value().getText());
                } else if (MongoConstant.MULTI.equals(key) && ctx.UPDATE() != null) {
                    func.setMulti(Boolean.parseBoolean(pair.value().getText()));
                } else if (MongoConstant.SORT.equals(key)) {
                    func.setSort(pair.value().getText());
                }
            }
        }

        this.instStack.push(func);
        return null;
    }

    @Override
    public Object visitGetProfilingStatus(MongoParser.GetProfilingStatusContext ctx) {
        ProfileFunc func = new ProfileFunc();
        func.setLevel("-1");
        this.instStack.push(code(func, ctx));
        return null;
    }

    @Override
    public Object visitKillOp(MongoParser.KillOpContext ctx) {
        this.instStack.push(new KillOpFunc(ctx.NUMBER().getText()));
        return null;
    }

    @Override
    public Object visitHostInfo(MongoParser.HostInfoContext ctx) {
        this.instStack.push(new HostInfoFunc());
        return null;
    }

    @Override
    public Object visitSetProfilingLevel(MongoParser.SetProfilingLevelContext ctx) {
        ProfileFunc func = new ProfileFunc();
        func.setLevel(ctx.NUMBER().getText());
        if (ctx.options != null) {
            for (MongoParser.PairContext pair : ctx.obj().pair()) {
                func.addOption(pair.key().getText(), pair.value().getText());
            }
        }
        this.instStack.push(func);
        return null;
    }

    @Override
    public Object visitAdminCommand(MongoParser.AdminCommandContext ctx) {
        AdminCommandFunc adminCommandFunc = new AdminCommandFunc();
        adminCommandFunc.setCommand(ctx.obj().getText());

        this.instStack.push(adminCommandFunc);
        return null;
    }

    @Override
    public Object visitRunCommand(MongoParser.RunCommandContext ctx) {
        RunCommandFunc runCommandFunc = new RunCommandFunc();
        runCommandFunc.setCommand(ctx.obj().getText());
        this.instStack.push(runCommandFunc);
        return null;
    }

    @Override
    public Object visitDbStats(MongoParser.DbStatsContext ctx) {
        DbStatsFunc dbStatsFunc = new DbStatsFunc();
        if (ctx.obj() != null) {
            for (MongoParser.PairContext pair : ctx.obj().pair()) {
                dbStatsFunc.addOption(pair.key().getText(), pair.value().getText());
            }
        } else if (ctx.NUMBER() != null) {
            dbStatsFunc.addOption("scale", ctx.NUMBER().getText());
        }
        this.instStack.push(dbStatsFunc);
        return null;
    }

    @Override
    public Object visitValidate(MongoParser.ValidateContext ctx) {
        ValidateFunc validateFunc = new ValidateFunc();

        if (ctx.options != null) {
            for (MongoParser.PairContext pair : ctx.options.pair()) {
                String key = getString(pair.key().getText());
                if (MongoConstant.FULL.equals(key)) {
                    validateFunc.setFull(pair.value().getText());
                } else if (MongoConstant.REPAIR.equals(key)) {
                    validateFunc.setRepair(pair.value().getText());
                } else if (MongoConstant.CHECK_BSON_CONFORMANCE.equals(key)) {
                    validateFunc.setCheckBSONConformance(pair.value().getText());
                }
            }
        }

        this.instStack.push(validateFunc);
        return null;
    }

    @Override
    public Object visitEstimatedDocumentCount(MongoParser.EstimatedDocumentCountContext ctx) {
        CountFunc func = new CountFunc();
        if (ctx.option != null) {
            for (MongoParser.PairContext pair : ctx.option.pair()) {
                String key = getString(pair.key().getText());
                if (MongoConstant.MAX_TIME_MS.equals(key)) {
                    func.setMaxTimeMS(pair.value().getText());
                }
            }
        }
        this.instStack.push(code(func, ctx));
        return null;
    }

    @Override
    public Object visitFindOneAndReplace(MongoParser.FindOneAndReplaceContext ctx) {
        FindAndModifyFunc func = new FindAndModifyFunc(MongoFuncType.FIND_ONE_AND_REPLACE);
        if (ctx.filter != null) {
            func.setQuery(ctx.filter.getText());
        }
        if (ctx.replace != null) {
            ctx.replace.pair().stream().map(MongoParser.PairContext::key).forEach((keyContext -> {
                String key = getString(keyContext.getText());
                if (key.startsWith("$")) {
                    throw new UnsupportedOperationException("Replacement document must not contain atomic operators");
                }
            }));
            func.setUpdate(ctx.replace.getText());
        } else {
            func.setUpdate("{}");
        }

        if (ctx.options != null) {
            boolean hasReturnDocument = ctx.options.pair().stream().map(MongoParser.PairContext::key).anyMatch((k) -> {
                String string = getString(k.getText());
                return MongoConstant.RETURN_DOCUMENT.equals(string);
            });
            for (MongoParser.PairContext pair : ctx.options.pair()) {
                String key = getString(pair.key().getText());
                String value = pair.value().getText();
                if (MongoConstant.PROJECTION.equals(key)) {
                    func.setFields(value);
                } else if (MongoConstant.MAX_TIME_MS.equals(key)) {
                    func.setMaxTimeMS(value);
                } else if (MongoConstant.SORT.equals(key)) {
                    func.setSort(value);
                } else if (MongoConstant.WRITE_CONCERN.equals(key)) {
                    func.setWriteConcern(value);
                } else if (MongoConstant.COLLATION.equals(key)) {
                    func.setCollation(value);
                } else if (MongoConstant.UPSERT.equals(key)) {
                    func.setUpsert(value);
                } else if (MongoConstant.RETURN_DOCUMENT.equals(key)) {
                    String string = getString(value);
                    if (string.equals("after")) {
                        func.setNew_("true");
                    } else if (string.equals("before")) {
                        func.setNew_("false");
                    } else {
                        throw new UnsupportedOperationException("returnDocument needs to be either 'before' or 'after'");
                    }
                } else if (MongoConstant.RETURN_NEW_DOCUMENT.equals(key)) {
                    if (!hasReturnDocument) {
                        func.setNew_(value);
                    }
                }
            }
        }
        this.instStack.push(func);
        return null;
    }

    @Override
    public Object visitFindOneAndUpdate(MongoParser.FindOneAndUpdateContext ctx) {
        FindAndModifyFunc func = new FindAndModifyFunc(MongoFuncType.FIND_ONE_AND_UPDATE);
        if (ctx.filter != null) {
            func.setQuery(ctx.filter.getText());
        }

        if (ctx.objUpdate != null) {
            List<MongoParser.PairContext> pair = ctx.objUpdate.pair();
            if (pair.isEmpty()) {
                throw new UnsupportedOperationException("Update document requires atomic operators");
            }
            pair.stream().map(MongoParser.PairContext::key).forEach((keyContext -> {
                String key = getString(keyContext.getText());
                if (!key.startsWith("$")) {
                    throw new UnsupportedOperationException("Update document requires atomic operators");
                }
            }));
            func.setUpdate(ctx.objUpdate.getText());
        } else if (ctx.arrUpdate != null) {
            if (ctx.arrUpdate.value().isEmpty()) {
                throw new UnsupportedOperationException("Update document requires atomic operators");
            }
            ctx.arrUpdate.value().stream().map(val -> {
                if (val.obj() == null) {
                    throw new UnsupportedOperationException("Update document requires atomic operators");
                } else {
                    return val.obj().pair();
                }
            }).flatMap(List::stream).map(MongoParser.PairContext::key).forEach((keyContext -> {
                String key = getString(keyContext.getText());
                if (!key.startsWith("$")) {
                    throw new UnsupportedOperationException("Update document requires atomic operators");
                }
            }));
            func.setUpdate(ctx.arrUpdate.getText());
        } else {
            throw new UnsupportedOperationException("Update document requires atomic operators");
        }

        if (ctx.options != null) {
            boolean hasReturnDocument = ctx.options.pair().stream().map(MongoParser.PairContext::key).anyMatch((k) -> {
                String string = getString(k.getText());
                return MongoConstant.RETURN_DOCUMENT.equals(string);
            });
            for (MongoParser.PairContext pair : ctx.options.pair()) {
                String key = getString(pair.key().getText());
                String value = pair.value().getText();
                if (MongoConstant.PROJECTION.equals(key)) {
                    func.setFields(value);
                } else if (MongoConstant.MAX_TIME_MS.equals(key)) {
                    func.setMaxTimeMS(value);
                } else if (MongoConstant.SORT.equals(key)) {
                    func.setSort(value);
                } else if (MongoConstant.WRITE_CONCERN.equals(key)) {
                    func.setWriteConcern(value);
                } else if (MongoConstant.COLLATION.equals(key)) {
                    func.setCollation(value);
                } else if (MongoConstant.UPSERT.equals(key)) {
                    func.setUpsert(value);
                } else if (MongoConstant.RETURN_DOCUMENT.equals(key)) {
                    String string = getString(value);
                    if (string.equals("after")) {
                        func.setNew_("true");
                    } else if (string.equals("before")) {
                        func.setNew_("false");
                    } else {
                        throw new UnsupportedOperationException("returnDocument needs to be either 'before' or 'after'");
                    }
                } else if (MongoConstant.RETURN_NEW_DOCUMENT.equals(key)) {
                    if (!hasReturnDocument) {
                        func.setNew_(value);
                    }
                } else if (MongoConstant.ARRAY_FILTERS.equals(key)) {
                    func.setArrayFilters(value);
                }
            }
        }
        this.instStack.push(func);
        return null;
    }

    @Override
    public Object visitFindOneAndDelete(MongoParser.FindOneAndDeleteContext ctx) {
        FindAndModifyFunc func = new FindAndModifyFunc(MongoFuncType.FIND_ONE_AND_DELETE);
        if (ctx.filter != null) {
            func.setQuery(ctx.filter.getText());
        }
        func.setRemove("true");

        if (ctx.options != null) {
            for (MongoParser.PairContext pair : ctx.options.pair()) {
                String key = getString(pair.key().getText());
                if (MongoConstant.PROJECTION.equals(key)) {
                    func.setFields(pair.value().getText());
                } else if (MongoConstant.MAX_TIME_MS.equals(key)) {
                    func.setMaxTimeMS(pair.value().getText());
                } else if (MongoConstant.SORT.equals(key)) {
                    func.setSort(pair.value().getText());
                } else if (MongoConstant.WRITE_CONCERN.equals(key)) {
                    func.setWriteConcern(pair.value().getText());
                } else if (MongoConstant.COLLATION.equals(key)) {
                    func.setCollation(pair.value().getText());
                }
            }
        }
        this.instStack.push(func);
        return null;
    }

    @Override
    public Object visitDeleteOne(MongoParser.DeleteOneContext ctx) {
        DeleteOneFunc deleteOneFunc = new DeleteOneFunc();
        if (ctx.filter != null) {
            deleteOneFunc.setFilter(ctx.filter.getText());
        }

        if (ctx.option != null) {
            String text = tokenStream.getText(ctx.option);
            for (MongoParser.PairContext pairContext : ctx.option.pair()) {
                String text1 = tokenStream.getText(pairContext.key());
                if (text1.startsWith("\"") || text1.endsWith("\'")) {
                    text1 = text1.substring(1, text1.length() - 1);
                }
                if (text1.equals(MongoConstant.COLLATION)) {
                    deleteOneFunc.setCollation(pairContext.value().getText());
                } else if (text1.equals(MongoConstant.WRITE_CONCERN)) {
                    deleteOneFunc.setWriteConcern(pairContext.value().getText());
                } else if (text1.equals(MongoConstant.HINT)) {
                    deleteOneFunc.setHint(pairContext.value().getText());
                } else if (text1.equals(MongoConstant.MAX_TIME_MS)) {
                    deleteOneFunc.setMaxTimeMS(pairContext.value().getText());
                } else if (text.equals(MongoConstant.LET)) {
                    deleteOneFunc.setLet(pairContext.value().getText());
                }
            }

        }
        this.instStack.push(deleteOneFunc);
        return null;
    }

    @Override
    public Object visitDeleteMany(MongoParser.DeleteManyContext ctx) {
        DeleteManyFunc func = new DeleteManyFunc();
        if (ctx.filter != null) {
            func.setFilter(ctx.filter.getText());
        }

        if (ctx.option != null) {
            for (MongoParser.PairContext pair : ctx.option.pair()) {
                String key = getString(pair.key().getText());
                if (MongoConstant.HINT.equals(key)) {
                    func.setHint(pair.value().getText());
                } else if (MongoConstant.MAX_TIME_MS.equals(key)) {
                    func.setMaxTimeMS(pair.value().getText());
                } else if (MongoConstant.COLLATION.equals(key)) {
                    func.setCollation(pair.value().getText());
                } else if (MongoConstant.LET.equals(key)) {
                    func.setLet(pair.value().getText());
                } else if (MongoConstant.WRITE_CONCERN.equals(key)) {
                    func.setWriteConcern(pair.value().getText());
                }
            }
        }
        this.instStack.push(func);
        return null;
    }

    @Override
    public Object visitKey(MongoParser.KeyContext ctx) {
        return null;
    }

    // json
    @Override
    public Object visitJsonString(MongoParser.JsonStringContext ctx) {
        return null;
    }

    @Override
    public Object visitObj(MongoParser.ObjContext ctx) {
        return null;
    }

    @Override
    public Object visitPair(MongoParser.PairContext ctx) {
        return null;
    }

    @Override
    public Object visitArr(MongoParser.ArrContext ctx) {
        return null;
    }

    @Override
    public Object visitValue(MongoParser.ValueContext ctx) {
        return null;
    }

    @Override
    public Object visitKeyWordId(MongoParser.KeyWordIdContext ctx) {
        return null;
    }
}
