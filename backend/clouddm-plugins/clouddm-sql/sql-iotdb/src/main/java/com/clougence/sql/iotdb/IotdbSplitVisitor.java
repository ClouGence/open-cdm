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
package com.clougence.sql.iotdb;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.iotdb.antlr.IotdbParserBaseVisitor;
import com.clougence.sql.iotdb.antlr.IotdbParser.*;

final class IotdbSplitVisitor extends IotdbParserBaseVisitor<SplitQueryType> {

    static final IotdbSplitVisitor INSTANCE = new IotdbSplitVisitor();

    @Override
    public SplitQueryType visitTreeSelectStatement(TreeSelectStatementContext context) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitTableSelectStatement(TableSelectStatementContext context) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitTreeInsertStatement(TreeInsertStatementContext context) {
        return SplitQueryType.INSERT;
    }

    @Override
    public SplitQueryType visitTableInsertStatement(TableInsertStatementContext context) {
        return SplitQueryType.INSERT;
    }

    @Override
    public SplitQueryType visitTreeDeleteStatement(TreeDeleteStatementContext context) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitTableDeleteStatement(TableDeleteStatementContext context) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitTableUpdateStatement(TableUpdateStatementContext context) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitTreeCreateSeriesStatement(TreeCreateSeriesStatementContext context) {
        return SplitQueryType.CREATE_TIMESERIES;
    }

    @Override
    public SplitQueryType visitTreeCreateAlignedStatement(TreeCreateAlignedStatementContext context) {
        return SplitQueryType.CREATE_TIMESERIES;
    }

    @Override
    public SplitQueryType visitTreeDropSeriesStatement(TreeDropSeriesStatementContext context) {
        return SplitQueryType.DROP_TIMESERIES;
    }

    @Override
    public SplitQueryType visitTreeCreateDatabaseStatement(TreeCreateDatabaseStatementContext context) {
        return SplitQueryType.CREATE_SCHEMA;
    }

    @Override
    public SplitQueryType visitTreeDropDatabaseStatement(TreeDropDatabaseStatementContext context) {
        return SplitQueryType.DROP_SCHEMA;
    }

    @Override
    public SplitQueryType visitTableCreateDatabaseStatement(TableCreateDatabaseStatementContext context) {
        return SplitQueryType.CREATE_SCHEMA;
    }

    @Override
    public SplitQueryType visitTableDropDatabaseStatement(TableDropDatabaseStatementContext context) {
        return SplitQueryType.DROP_SCHEMA;
    }

    @Override
    public SplitQueryType visitTableCreateStatement(TableCreateStatementContext context) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitTableAddColumnStatement(TableAddColumnStatementContext context) {
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitTableDropColumnStatement(TableDropColumnStatementContext context) {
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitTableDropStatement(TableDropStatementContext context) {
        return SplitQueryType.DROP_TABLE;
    }

    @Override
    public SplitQueryType visitTableDropViewStatement(TableDropViewStatementContext context) {
        return SplitQueryType.DROP_VIEW;
    }

    @Override
    public SplitQueryType visitTreeShowScopeStatement(TreeShowScopeStatementContext context) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitTreeShowPathStatement(TreeShowPathStatementContext context) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitTableShowScopeStatement(TableShowScopeStatementContext context) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitTableShowTablesStatement(TableShowTablesStatementContext context) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitTableDescribeStatement(TableDescribeStatementContext context) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitTableShowCreateStatement(TableShowCreateStatementContext context) {
        return SplitQueryType.METADATA;
    }
}
