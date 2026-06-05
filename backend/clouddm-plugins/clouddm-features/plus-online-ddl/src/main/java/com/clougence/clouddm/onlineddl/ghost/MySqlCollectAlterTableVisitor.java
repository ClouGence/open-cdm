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
package com.clougence.clouddm.onlineddl.ghost;

import java.io.IOException;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;

/**
 * @author bucketli 2022/6/6 11:48:13
 */
public class MySqlCollectAlterTableVisitor extends MySqlOutputVisitor {

    private final StringBuilder tableBuilder;

    private final StringBuilder dbBuilder;

    public MySqlCollectAlterTableVisitor(StringBuilder sqlBuidler, StringBuilder tableBuilder, StringBuilder dbBuilder){
        super(sqlBuidler);
        this.tableBuilder = tableBuilder;
        this.dbBuilder = dbBuilder;
    }

    @Override
    public boolean visit(SQLAlterTableStatement x) {
        collectSchemaAndTable(x);
        return super.visit(x);
    }

    private void collectSchemaAndTable(SQLAlterTableStatement x) {
        SQLName expr = x.getName();

        if (expr instanceof SQLIdentifierExpr) {
            SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr) expr;
            this.tableBuilder.append(identifierExpr.getName());
        } else if (expr instanceof SQLPropertyExpr) {
            SQLPropertyExpr propertyExpr = (SQLPropertyExpr) expr;
            SQLExpr owner = propertyExpr.getOwner();

            if (owner instanceof SQLIdentifierExpr) {
                SQLIdentifierExpr identOwner = (SQLIdentifierExpr) owner;
                dbBuilder.append(identOwner.getName());
            } else {
                throw new IllegalArgumentException("unsupported table owner type:" + owner.getClass().getSimpleName());
            }

            this.tableBuilder.append(propertyExpr.getName());
        } else {
            throw new IllegalArgumentException("unsupported table expr type:" + expr.getClass().getSimpleName());
        }
    }

    @Override
    protected void printName0(String text) {
        if (appender == null || text.length() == 0) {
            return;
        }

        try {
            if (printNameQuote) {
                char c0 = text.charAt(0);
                if (c0 == quote) {
                    this.appender.append(text);
                } else if (c0 == '"' && text.charAt(text.length() - 1) == '"') {
                    this.appender.append("\\").append(quote);
                    this.appender.append(text.substring(1, text.length() - 1));
                    this.appender.append("\\").append(quote);
                } else if (c0 == '`' && text.charAt(text.length() - 1) == '`') {
                    this.appender.append("\\").append(quote);
                    this.appender.append("\\").append(text.substring(1, text.length() - 1));
                    this.appender.append("\\").append(quote);
                } else {
                    this.appender.append("\\").append(quote);
                    this.appender.append(text);
                    this.appender.append("\\").append(quote);
                }
            } else {
                this.appender.append(text);
            }
        } catch (IOException e) {
            throw new RuntimeException("println error", e);
        }
    }
}
