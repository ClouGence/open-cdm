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
package com.clougence.sql.mongodb.parser;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.sql.mongodb.parser.antlr.MongoParser;
import com.clougence.sql.mongodb.parser.antlr.MongoParserBaseVisitor;

public class MongoSplitVisitor extends MongoParserBaseVisitor<SecQueryType> {

    public static final MongoSplitVisitor INSTANCE = new MongoSplitVisitor();

    @Override
    public SecQueryType visitShowDatabases(MongoParser.ShowDatabasesContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitShowCollections(MongoParser.ShowCollectionsContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitDbCreateCollection(MongoParser.DbCreateCollectionContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitDbCreateView(MongoParser.DbCreateViewContext ctx) {
        return SecQueryType.CREATE_VIEW;
    }

    @Override
    public SecQueryType visitDbDropDatabase(MongoParser.DbDropDatabaseContext ctx) {
        return SecQueryType.DROP_SCHEMA;
    }

    @Override
    public SecQueryType visitUse(MongoParser.UseContext ctx) {
        return SecQueryType.SWITCH_SCHEMA;
    }

    @Override
    public SecQueryType visitFind(MongoParser.FindContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitAggregate(MongoParser.AggregateContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitDbAggregate(MongoParser.DbAggregateContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitFindOne(MongoParser.FindOneContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitCount(MongoParser.CountContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitEstimatedDocumentCount(MongoParser.EstimatedDocumentCountContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitCountDocuments(MongoParser.CountDocumentsContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitDistinct(MongoParser.DistinctContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitDataSize(MongoParser.DataSizeContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitDbHello(MongoParser.DbHelloContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitGetCollectionNames(MongoParser.GetCollectionNamesContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitGetCollectionInfos(MongoParser.GetCollectionInfosContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitGetIndexes(MongoParser.GetIndexesContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitValidate(MongoParser.ValidateContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitCreateIndex(MongoParser.CreateIndexContext ctx) {
        return SecQueryType.ADD_INDEX;
    }

    @Override
    public SecQueryType visitCreateIndexes(MongoParser.CreateIndexesContext ctx) {
        return SecQueryType.ADD_INDEX;
    }

    @Override
    public SecQueryType visitInsert(MongoParser.InsertContext ctx) {
        return SecQueryType.INSERT;
    }

    @Override
    public SecQueryType visitInsertOne(MongoParser.InsertOneContext ctx) {
        return SecQueryType.INSERT;
    }

    @Override
    public SecQueryType visitInsertMany(MongoParser.InsertManyContext ctx) {
        return SecQueryType.INSERT;
    }

    @Override
    public SecQueryType visitUpdate(MongoParser.UpdateContext ctx) {
        return hasTrueValue(ctx.option, "upsert") ? SecQueryType.MERGE : SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitReplaceOne(MongoParser.ReplaceOneContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitFindOneAndReplace(MongoParser.FindOneAndReplaceContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitFindOneAndUpdate(MongoParser.FindOneAndUpdateContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitFindOneAndDelete(MongoParser.FindOneAndDeleteContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitDeleteOne(MongoParser.DeleteOneContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitDeleteMany(MongoParser.DeleteManyContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitDrop(MongoParser.DropContext ctx) {
        return SecQueryType.DROP_TABLE;
    }

    @Override
    public SecQueryType visitRenameCollection(MongoParser.RenameCollectionContext ctx) {
        return SecQueryType.RENAME_TABLE;
    }

    @Override
    public SecQueryType visitHideIndex(MongoParser.HideIndexContext ctx) {
        return SecQueryType.ALTER_INDEX;
    }

    @Override
    public SecQueryType visitDropIndex(MongoParser.DropIndexContext ctx) {
        return SecQueryType.DROP_INDEX;
    }

    @Override
    public SecQueryType visitDropIndexes(MongoParser.DropIndexesContext ctx) {
        return SecQueryType.DROP_INDEX;
    }

    @Override
    public SecQueryType visitExplain(MongoParser.ExplainContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitDbStats(MongoParser.DbStatsContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitSetProfilingLevel(MongoParser.SetProfilingLevelContext ctx) {
        return SecQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitGetProfilingStatus(MongoParser.GetProfilingStatusContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitGetLogComponents(MongoParser.GetLogComponentsContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitHostInfo(MongoParser.HostInfoContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitCurrentOp(MongoParser.CurrentOpContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitKillOp(MongoParser.KillOpContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitFsyncLock(MongoParser.FsyncLockContext ctx) {
        return SecQueryType.SESSION_LOCK;
    }

    @Override
    public SecQueryType visitFsyncUnlock(MongoParser.FsyncUnlockContext ctx) {
        return SecQueryType.SESSION_LOCK;
    }

    @Override
    public SecQueryType visitDbServerStatus(MongoParser.DbServerStatusContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitDbServerBuildInfo(MongoParser.DbServerBuildInfoContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitLatencyStats(MongoParser.LatencyStatsContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitRunCommand(MongoParser.RunCommandContext ctx) {
        return commandType(ctx.obj());
    }

    @Override
    public SecQueryType visitAdminCommand(MongoParser.AdminCommandContext ctx) {
        return commandType(ctx.obj());
    }

    private static SecQueryType commandType(MongoParser.ObjContext command) {
        if (command.pair().isEmpty()) {
            return SecQueryType.UNKNOWN;
        }

        String commandName = keyText(command.pair(0).key());
        return switch (commandName) {
            case "profile" -> SecQueryType.SYSTEM_SETTING_WRITE;
            case "killOp" -> SecQueryType.ADMIN;
            case "currentOp", "serverStatus" -> SecQueryType.PERFORMANCE;
            case "listCollections", "buildInfo", "hello", "hostInfo" -> SecQueryType.METADATA;
            case "dropDatabase" -> SecQueryType.DROP_SCHEMA;
            case "create" -> hasKey(command, "viewOn") ? SecQueryType.CREATE_VIEW : SecQueryType.CREATE_TABLE;
            default -> SecQueryType.UNKNOWN;
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
