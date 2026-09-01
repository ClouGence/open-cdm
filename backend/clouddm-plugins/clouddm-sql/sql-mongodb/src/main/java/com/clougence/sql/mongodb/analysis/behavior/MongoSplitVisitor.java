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
package com.clougence.sql.mongodb.analysis.behavior;

import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;
import com.clougence.sql.mongodb.parser.antlr.MongoParser;
import com.clougence.sql.mongodb.parser.antlr.MongoParserBaseVisitor;

public class MongoSplitVisitor extends MongoParserBaseVisitor<StatementType> {

    public static final MongoSplitVisitor INSTANCE = new MongoSplitVisitor();

    @Override
    public StatementType visitShowDatabases(MongoParser.ShowDatabasesContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitShowCollections(MongoParser.ShowCollectionsContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitDbCreateCollection(MongoParser.DbCreateCollectionContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitDbCreateView(MongoParser.DbCreateViewContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitDbDropDatabase(MongoParser.DbDropDatabaseContext ctx) {
        return StatementType.DROP_SCHEMA;
    }

    @Override
    public StatementType visitUse(MongoParser.UseContext ctx) {
        return StatementType.SWITCH_SCHEMA;
    }

    @Override
    public StatementType visitFind(MongoParser.FindContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitAggregate(MongoParser.AggregateContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitDbAggregate(MongoParser.DbAggregateContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitFindOne(MongoParser.FindOneContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitCount(MongoParser.CountContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitEstimatedDocumentCount(MongoParser.EstimatedDocumentCountContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitCountDocuments(MongoParser.CountDocumentsContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitDistinct(MongoParser.DistinctContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitDataSize(MongoParser.DataSizeContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitDbHello(MongoParser.DbHelloContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitGetCollectionNames(MongoParser.GetCollectionNamesContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitGetCollectionInfos(MongoParser.GetCollectionInfosContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitGetIndexes(MongoParser.GetIndexesContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitValidate(MongoParser.ValidateContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitCreateIndex(MongoParser.CreateIndexContext ctx) {
        return StatementType.ADD_INDEX;
    }

    @Override
    public StatementType visitCreateIndexes(MongoParser.CreateIndexesContext ctx) {
        return StatementType.ADD_INDEX;
    }

    @Override
    public StatementType visitInsert(MongoParser.InsertContext ctx) {
        return StatementType.INSERT;
    }

    @Override
    public StatementType visitInsertOne(MongoParser.InsertOneContext ctx) {
        return StatementType.INSERT;
    }

    @Override
    public StatementType visitInsertMany(MongoParser.InsertManyContext ctx) {
        return StatementType.INSERT;
    }

    @Override
    public StatementType visitUpdate(MongoParser.UpdateContext ctx) {
        return hasTrueValue(ctx.option, "upsert") ? StatementType.MERGE : StatementType.UPDATE;
    }

    @Override
    public StatementType visitReplaceOne(MongoParser.ReplaceOneContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitFindOneAndReplace(MongoParser.FindOneAndReplaceContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitFindOneAndUpdate(MongoParser.FindOneAndUpdateContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitFindOneAndDelete(MongoParser.FindOneAndDeleteContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitDeleteOne(MongoParser.DeleteOneContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitDeleteMany(MongoParser.DeleteManyContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitDrop(MongoParser.DropContext ctx) {
        return StatementType.DROP_TABLE;
    }

    @Override
    public StatementType visitRenameCollection(MongoParser.RenameCollectionContext ctx) {
        return StatementType.RENAME_TABLE;
    }

    @Override
    public StatementType visitHideIndex(MongoParser.HideIndexContext ctx) {
        return StatementType.ALTER_INDEX;
    }

    @Override
    public StatementType visitDropIndex(MongoParser.DropIndexContext ctx) {
        return StatementType.DROP_INDEX;
    }

    @Override
    public StatementType visitDropIndexes(MongoParser.DropIndexesContext ctx) {
        return StatementType.DROP_INDEX;
    }

    @Override
    public StatementType visitExplain(MongoParser.ExplainContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitDbStats(MongoParser.DbStatsContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitSetProfilingLevel(MongoParser.SetProfilingLevelContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitGetProfilingStatus(MongoParser.GetProfilingStatusContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitGetLogComponents(MongoParser.GetLogComponentsContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitHostInfo(MongoParser.HostInfoContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitCurrentOp(MongoParser.CurrentOpContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitKillOp(MongoParser.KillOpContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitFsyncLock(MongoParser.FsyncLockContext ctx) {
        return StatementType.SESSION_LOCK;
    }

    @Override
    public StatementType visitFsyncUnlock(MongoParser.FsyncUnlockContext ctx) {
        return StatementType.SESSION_LOCK;
    }

    @Override
    public StatementType visitDbServerStatus(MongoParser.DbServerStatusContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitDbServerBuildInfo(MongoParser.DbServerBuildInfoContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitLatencyStats(MongoParser.LatencyStatsContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitRunCommand(MongoParser.RunCommandContext ctx) {
        return commandType(ctx.obj());
    }

    @Override
    public StatementType visitAdminCommand(MongoParser.AdminCommandContext ctx) {
        return commandType(ctx.obj());
    }

    private static StatementType commandType(MongoParser.ObjContext command) {
        if (command.pair().isEmpty()) {
            return StatementType.UNKNOWN;
        }

        String commandName = keyText(command.pair(0).key());
        return switch (commandName) {
            case "profile" -> StatementType.SYSTEM_SETTING_WRITE;
            case "killOp" -> StatementType.ADMIN;
            case "currentOp", "serverStatus" -> StatementType.PERFORMANCE;
            case "listCollections", "buildInfo", "hello", "hostInfo" -> StatementType.METADATA;
            case "dropDatabase" -> StatementType.DROP_SCHEMA;
            case "create" -> hasKey(command, "viewOn") ? StatementType.CREATE_VIEW : StatementType.CREATE_TABLE;
            default -> StatementType.UNKNOWN;
        };
    }

    private static boolean hasKey(MongoParser.ObjContext object, String expected) {
        return object.pair().stream().anyMatch(pair -> expected.equals(keyText(pair.key())));
    }

    private static boolean hasTrueValue(MongoParser.ObjContext object, String expected) {
        return object != null && object.pair().stream().anyMatch(pair -> expected.equals(keyText(pair.key())) && "true".equals(pair.value().getText()));
    }

    static String keyText(MongoParser.KeyContext key) {
        String text = key.getText();
        if (text.length() >= 2 && (text.charAt(0) == '"' || text.charAt(0) == '\'')) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }
}
