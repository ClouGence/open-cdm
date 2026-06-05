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
package com.clougence.clouddm.ds.sqlserver.definition.ui.template;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.ds.sqlserver.dialect.SqlServerDialect;
import com.clougence.clouddm.sdk.ui.template.AbstractCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.schema.dialect.Dialect;

public class MsSqlCmdTemplateSpi extends AbstractCmdTemplateSpi {

    @Override
    public String getQuickQuery(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        if (option.getDefaultLimit() > 0) {
            sb.append("top ").append(option.getDefaultLimit());
        }
        sb.append(" * from ");
        sb.append(SCHEMA_PLACEHOLDER + "." + TABLE_PLACEHOLDER);
        sb.append(";");
        return sb.toString();
    }

    @Override
    public String getQuickQueryByTable(CmdTemplateOption option) {
        return getQuickQuery(option);
    }

    @Override
    public String getQuickQueryByView(CmdTemplateOption option) {
        return getQuickQuery(option);
    }

    @Override
    public String getQuickQueryByColumn(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ");
        sb.append(TABLE_PLACEHOLDER);
        sb.append(" where ");
        sb.append(COLUMN_PLACEHOLDER);
        sb.append(" = ");
        return sb.toString();
    }

    @Override
    public List<String> getCreateCatalog(CmdTemplateOption option) {
        return Collections.singletonList("create database " + fmtName(option, option.getCatalog()) + ";");
    }

    @Override
    public List<String> getDropCatalog(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop database ");
        if (option.isUsingExists()) {
            sb.append("if exists ");
        }
        sb.append(fmtName(option, option.getCatalog()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getRenameCatalog(CmdTemplateOption option) {
        return Collections.singletonList("exec sp_rename " + fmtName(option, option.getCatalog()) + ", " + fmtName(option, option.getTargetNewName()) + ", 'DATABASE';");
    }

    @Override
    public List<String> getCreateSchema(CmdTemplateOption option) {
        return Collections.singletonList("create schema " + fmtName(option, option.getSchema()) + ";");
    }

    @Override
    public List<String> getDropSchema(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop schema ");
        if (option.isUsingExists()) {
            sb.append("if exists ");
        }
        sb.append(fmtName(option, option.getSchema()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropTable(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop table ");
        if (option.isUsingExists()) {
            sb.append("if exists ");
        }
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDeleteTable(CmdTemplateOption option) {
        return Collections.singletonList("delete from " + fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()) + ";");
    }

    @Override
    public List<String> getRenameTable(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("exec sp_rename ");
        sb.append(String.format("'%s.%s'", fmtName(option, option.getSchema()), fmtName(option, option.getTargetName())));
        sb.append(", ");
        sb.append(fmtName(option, option.getTargetNewName()));
        sb.append(", 'OBJECT';");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getTruncateTable(CmdTemplateOption option) {
        return Collections.singletonList("truncate table " + fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()) + ";");
    }

    @Override
    public List<String> getRenameView(CmdTemplateOption option) {
        return this.getRenameTable(option);
    }

    @Override
    public List<String> getDropView(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop view ");
        if (option.isUsingExists()) {
            sb.append("if exists ");
        }
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropTrigger(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop trigger ");
        if (option.isUsingExists()) {
            sb.append("if exists ");
        }
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropTablePrimaryKey(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("alter table ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetName()));
        sb.append(" drop constraint ");
        sb.append(fmtName(option, option.getTargetExactName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    protected Dialect getDialect() { return SqlServerDialect.INSTANCE; }
}
