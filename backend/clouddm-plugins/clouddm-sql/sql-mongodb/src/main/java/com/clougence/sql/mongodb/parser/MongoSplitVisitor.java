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

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.mongodb.parser.antlr.MongoParser;
import com.clougence.sql.mongodb.parser.antlr.MongoParserBaseVisitor;

public class MongoSplitVisitor extends MongoParserBaseVisitor<SplitQueryType> {

    public static final MongoSplitVisitor INSTANCE = new MongoSplitVisitor();

    @Override
    public SplitQueryType visitShowDatabases(MongoParser.ShowDatabasesContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShowCollections(MongoParser.ShowCollectionsContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitReplicaSetFunction(MongoParser.ReplicaSetFunctionContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitShardingFunction(MongoParser.ShardingFunctionContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitDbCreateCollection(MongoParser.DbCreateCollectionContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitDbCreateView(MongoParser.DbCreateViewContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitDbDropDatabase(MongoParser.DbDropDatabaseContext ctx) {
        return SplitQueryType.DROP_SCHEMA;
    }

    @Override
    public SplitQueryType visitUse(MongoParser.UseContext ctx) {
        return SplitQueryType.SWITCH_SCHEMA;
    }

    @Override
    public SplitQueryType visitFind(MongoParser.FindContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitAggregate(MongoParser.AggregateContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitDbAggregate(MongoParser.DbAggregateContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitFindOne(MongoParser.FindOneContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitCount(MongoParser.CountContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitEstimatedDocumentCount(MongoParser.EstimatedDocumentCountContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitCountDocuments(MongoParser.CountDocumentsContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitDistinct(MongoParser.DistinctContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitDataSize(MongoParser.DataSizeContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDbHello(MongoParser.DbHelloContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitGetCollectionNames(MongoParser.GetCollectionNamesContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitGetCollectionInfos(MongoParser.GetCollectionInfosContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitGetIndexes(MongoParser.GetIndexesContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitValidate(MongoParser.ValidateContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitCreateIndex(MongoParser.CreateIndexContext ctx) {
        return SplitQueryType.ADD_INDEX;
    }

    @Override
    public SplitQueryType visitCreateIndexes(MongoParser.CreateIndexesContext ctx) {
        return SplitQueryType.ADD_INDEX;
    }

    @Override
    public SplitQueryType visitInsert(MongoParser.InsertContext ctx) {
        return SplitQueryType.INSERT;
    }

    @Override
    public SplitQueryType visitInsertOne(MongoParser.InsertOneContext ctx) {
        return SplitQueryType.INSERT;
    }

    @Override
    public SplitQueryType visitInsertMany(MongoParser.InsertManyContext ctx) {
        return SplitQueryType.INSERT;
    }

    @Override
    public SplitQueryType visitUpdate(MongoParser.UpdateContext ctx) {
        return hasTrueValue(ctx.option, "upsert") ? SplitQueryType.MERGE : SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitReplaceOne(MongoParser.ReplaceOneContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitFindOneAndReplace(MongoParser.FindOneAndReplaceContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitFindOneAndUpdate(MongoParser.FindOneAndUpdateContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitFindOneAndDelete(MongoParser.FindOneAndDeleteContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitDeleteOne(MongoParser.DeleteOneContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitDeleteMany(MongoParser.DeleteManyContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitDrop(MongoParser.DropContext ctx) {
        return SplitQueryType.DROP_TABLE;
    }

    @Override
    public SplitQueryType visitRenameCollection(MongoParser.RenameCollectionContext ctx) {
        return SplitQueryType.RENAME_TABLE;
    }

    @Override
    public SplitQueryType visitHideIndex(MongoParser.HideIndexContext ctx) {
        return SplitQueryType.ALTER_INDEX;
    }

    @Override
    public SplitQueryType visitDropIndex(MongoParser.DropIndexContext ctx) {
        return SplitQueryType.DROP_INDEX;
    }

    @Override
    public SplitQueryType visitDropIndexes(MongoParser.DropIndexesContext ctx) {
        return SplitQueryType.DROP_INDEX;
    }

    @Override
    public SplitQueryType visitExplain(MongoParser.ExplainContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDbStats(MongoParser.DbStatsContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitSetProfilingLevel(MongoParser.SetProfilingLevelContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitGetProfilingStatus(MongoParser.GetProfilingStatusContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitGetLogComponents(MongoParser.GetLogComponentsContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitHostInfo(MongoParser.HostInfoContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitCurrentOp(MongoParser.CurrentOpContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitKillOp(MongoParser.KillOpContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitFsyncLock(MongoParser.FsyncLockContext ctx) {
        return SplitQueryType.SESSION_LOCK;
    }

    @Override
    public SplitQueryType visitFsyncUnlock(MongoParser.FsyncUnlockContext ctx) {
        return SplitQueryType.SESSION_LOCK;
    }

    @Override
    public SplitQueryType visitDbServerStatus(MongoParser.DbServerStatusContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDbServerBuildInfo(MongoParser.DbServerBuildInfoContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitLatencyStats(MongoParser.LatencyStatsContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitRunCommand(MongoParser.RunCommandContext ctx) {
        return commandType(ctx.obj());
    }

    @Override
    public SplitQueryType visitAdminCommand(MongoParser.AdminCommandContext ctx) {
        return commandType(ctx.obj());
    }

    private static SplitQueryType commandType(MongoParser.ObjContext command) {
        if (command.pair().isEmpty()) {
            return SplitQueryType.UNKNOWN;
        }

        String commandName = keyText(command.pair(0).key());
        return switch (commandName) {
            case "profile" -> SplitQueryType.SYSTEM_SETTING_WRITE;
            case "killOp" -> SplitQueryType.ADMIN;
            case "currentOp", "serverStatus" -> SplitQueryType.PERFORMANCE;
            case "listCollections", "buildInfo", "hello", "hostInfo", "replSetGetStatus", "replSetGetConfig", "listShards", "balancerStatus", "isMaster" -> SplitQueryType.METADATA;
            case "dropDatabase" -> SplitQueryType.DROP_SCHEMA;
            case "create" -> hasKey(command, "viewOn") ? SplitQueryType.CREATE_VIEW : SplitQueryType.CREATE_TABLE;
            default -> SplitQueryType.UNKNOWN;
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
