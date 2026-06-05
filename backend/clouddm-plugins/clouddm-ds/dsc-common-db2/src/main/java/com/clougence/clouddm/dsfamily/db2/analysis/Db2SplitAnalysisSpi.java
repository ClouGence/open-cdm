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
package com.clougence.clouddm.dsfamily.db2.analysis;

import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2CreateSchemaStatement;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2CreateTableStatement;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2DropSchemaStatement;
import com.alibaba.druid.sql.dialect.db2.parser.DB2StatementParser;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.clougence.clouddm.sdk.analysis.split.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class Db2SplitAnalysisSpi implements SplitAnalysisSpi {

    @Override
    public List<SplitScript> splitScript(String script, List<QueryArg> args, int baseCodeLine, int baseCodeColumn) {
        List<SQLStatement> sqlStatements = new DB2StatementParser(script, SQLParserFeature.KeepComments).parseStatementList();
        return sqlStatements.stream().map(this::parseSql).collect(Collectors.toList());
    }

    protected DbType dbType() {
        return DbType.db2;
    }

    private SplitScript parseSql(SQLStatement statement) {
        SplitScript script = new SplitScript();

        // database
        if (statement instanceof DB2CreateSchemaStatement) {
            script.setType(SecQueryType.CREATE_SCHEMA);
            script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
            return script;
        } else if (statement instanceof DB2DropSchemaStatement) {
            script.setType(SecQueryType.DROP_SCHEMA);
            script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
            return script;
        }

        // table
        if (statement instanceof DB2CreateTableStatement) {
            script.setType(SecQueryType.CREATE_TABLE);
            script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
            return script;
        } else if (statement instanceof SQLAlterTableStatement) {
            script.setType(SecQueryType.ALTER_TABLE);
            script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
            return script;
        } else if (statement instanceof SQLDropTableStatement) {
            script.setType(SecQueryType.DROP_TABLE);
            script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
            return script;
        } else if (statement instanceof SQLCommentStatement) {
            if (((SQLCommentStatement) statement).getType() == SQLCommentStatement.Type.TABLE) {
                script.setType(SecQueryType.COMMENT_TABLE);
                script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
                return script;
            } else if (((SQLCommentStatement) statement).getType() == SQLCommentStatement.Type.COLUMN) {
                script.setType(SecQueryType.COMMENT_COLUMN);
                script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
                return script;
            }
        }

        // index
        if (statement instanceof SQLCreateIndexStatement) {
            script.setType(SecQueryType.CREATE_INDEX);
            script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
            return script;
        } else if (statement instanceof SQLDropIndexStatement) {
            script.setType(SecQueryType.DROP_INDEX);
            script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
            return script;
        } else if (statement instanceof SQLAlterIndexStatement) {
            script.setType(SecQueryType.ALTER_INDEX);
            script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
            return script;
        }

        // function
        if (statement instanceof SQLAlterFunctionStatement) {
            script.setType(SecQueryType.ALTER_FUNCTION);
            script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
            return script;
        } else if (statement instanceof SQLCallStatement) {
            script.setType(SecQueryType.CALL);
            script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
            return script;
        }

        // view
        if (statement instanceof SQLCreateViewStatement) {
            script.setType(SecQueryType.CREATE_VIEW);
            script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
            return script;
        } else if (statement instanceof SQLAlterViewStatement) {
            script.setType(SecQueryType.ALTER_VIEW);
            script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
            return script;
        } else if (statement instanceof SQLDropViewStatement) {
            script.setType(SecQueryType.DROP_VIEW);
            script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
            return script;
        }

        // query
        if (statement instanceof SQLSelectStatement) {
            script.setType(SecQueryType.SELECT);
            script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
            return script;
        } else if (statement instanceof SQLUpdateStatement) {
            script.setType(SecQueryType.UPDATE);
            script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
            return script;
        } else if (statement instanceof SQLInsertStatement) {
            script.setType(SecQueryType.INSERT);
            script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
            return script;
        } else if (statement instanceof SQLDeleteStatement) {
            script.setType(SecQueryType.DELETE);
            script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
            return script;
        }

        // other
        script.setType(SecQueryType.UNKNOWN);
        script.setScript(SQLUtils.toSQLString(statement, this.dbType()));
        return script;
    }
}
