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
package com.clougence.clouddm.dsfamily.mysql.definition.ui.template;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table.MyTypeUtils;
import com.clougence.clouddm.dsfamily.mysql.dialect.MySqlDialect;
import com.clougence.adapter.mysql.MyUmiAttributeNames;
import com.clougence.clouddm.sdk.ui.template.AbstractCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.domain.EColumn;
import com.clougence.utils.StringUtils;

public class MyCmdTemplateSpi extends AbstractCmdTemplateSpi {

    // features
    protected String DETERMINISTIC = "deterministic";

    @Override
    public String getQuickQuery(CmdTemplateOption option) {
        return "select * from " + TABLE_PLACEHOLDER + ";";
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
    public String getQuickQueryByProcedure(CmdTemplateOption option) {
        return "call " + TABLE_PLACEHOLDER + "(" + PARAM_PLACEHOLDER + ")";
    }

    @Override
    public String getQuickQueryByFunction(CmdTemplateOption option) {
        return "select " + TABLE_PLACEHOLDER + "(" + PARAM_PLACEHOLDER + ")";
    }

    @Override
    public String getQuickQueryByTrigger(CmdTemplateOption option) {
        return TABLE_PLACEHOLDER;
    }

    @Override
    public List<String> getCreateSchema(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("create schema ");
        if (option.isUsingExists()) {
            sb.append("if not exists ");
        }
        sb.append(fmtName(option, option.getSchema()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
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
        if (option.isCascade()) {
            sb.append(" cascade");
        }
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
        sb.append("alter table ");
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()));
        sb.append(" rename ");
        sb.append(fmtName(option, option.getTargetNewName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getTruncateTable(CmdTemplateOption option) {
        return Collections.singletonList("truncate table " + fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()) + ";");
    }

    @Override
    public List<String> getRenameView(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("rename table ");
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()));
        sb.append(" to ");
        sb.append(fmtName(option, option.getTargetNewName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropView(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop view ");
        if (option.isUsingExists()) {
            sb.append("if exists ");
        }
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()));
        if (option.isCascade()) {
            sb.append(" cascade");
        }
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
    public List<String> getDropProcedure(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop procedure ");
        if (option.isUsingExists()) {
            sb.append("if exists ");
        }
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropFunction(CmdTemplateOption option) {

        StringBuilder sb = new StringBuilder();
        sb.append("drop function ");
        if (option.isUsingExists()) {
            sb.append("if exists ");
        }
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getCreateTrigger(CmdTemplateOption option) {
        Trigger trigger = convertToTrigger(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("create trigger ");
        if (option.isUsingExists()) {
            sb.append("if not exists ");
        }
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, trigger.getName()));
        sb.append(" ");
        sb.append(trigger.getTime());
        sb.append(" ");
        sb.append(trigger.getEventList().get(0));
        sb.append(" \non ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, trigger.getTriggerTable()));
        sb.append("\nfor each row \n");
        sb.append(trigger.getSql());
        sb.append(";");

        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getAlterTrigger(CmdTemplateOption option) {
        String dropTriggerSql = getDropTrigger(option).get(0);
        String createTriggerSql = getCreateTrigger(option).get(0);

        return Arrays.asList(dropTriggerSql, createTriggerSql);
    }

    @Override
    public List<String> getAlterView(CmdTemplateOption option) {
        View view = convertToView(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("create or replace view ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(view.getName());
        sb.append("\nas\n");
        sb.append(view.getSql());
        if (isNotEmpty(option, MyUmiAttributeNames.VIEW_CHECK_OPTION)) {
            sb.append("\n");
            sb.append("with ").append(parseString(option, MyUmiAttributeNames.VIEW_CHECK_OPTION).toLowerCase()).append(" check option");
        }
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getCreateView(CmdTemplateOption option) {
        View view = convertToView(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("create view ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, view.getName()));
        sb.append("\nas\n");
        sb.append(view.getSql());
        if (isNotEmpty(option, MyUmiAttributeNames.VIEW_CHECK_OPTION)) {
            sb.append("\n");
            sb.append("with ").append(parseString(option, MyUmiAttributeNames.VIEW_CHECK_OPTION).toLowerCase()).append(" check option");
        }
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getAlterFunction(CmdTemplateOption option) {
        String drop = getDropFunction(option).get(0);
        String create = getCreateFunction(option).get(0);

        return Arrays.asList(drop, create);
    }

    @Override
    public List<String> getAlterProcedure(CmdTemplateOption option) {
        String drop = getDropProcedure(option).get(0);
        String create = getCreateProcedure(option).get(0);

        return Arrays.asList(drop, create);
    }

    @Override
    public List<String> getCreateFunction(CmdTemplateOption option) {
        Function function = convertToFunction(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("create function ");
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, function.getName()));
        List<Param> params = function.getParams();
        // set param
        sb.append("(");
        for (int i = 0; i < params.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            Param param = params.get(i);
            sb.append(fmtName(option, param.getName()) + " ");
            EColumn eColumn = parseColumn(param);
            sb.append(MyTypeUtils.buildColumnType(null, eColumn));
        }
        sb.append(")");
        // set return type
        sb.append("\nreturns ");
        sb.append(buildFuncReturns(function, option));
        sb.append("\n");

        if (parseBoolean(option, MyUmiAttributeNames.DETERMINISTIC)) {
            sb.append(DETERMINISTIC).append("\n");
        }
        if (isNotBlank(option, MyUmiAttributeNames.SQL_DATA_ACCESS)) {
            sb.append(parseString(option, MyUmiAttributeNames.SQL_DATA_ACCESS));
            sb.append("\n");
        }

        sb.append(function.getSql());
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    private static String buildFuncReturns(Function function, CmdTemplateOption option) {
        Param returnType = function.getReturnType();
        EColumn tmp = new EColumn();
        tmp.setDbType(returnType.getParamType());
        tmp.setNumericPrecision(returnType.getNumericPrecision());
        tmp.setNumericScale(returnType.getNumericScale());
        tmp.setLength(returnType.getLength());
        tmp.setDatetimePrecision(returnType.getDatetimePrecision());
        return MyTypeUtils.buildColumnType(null, tmp);
    }

    @Override
    public List<String> getCreateProcedure(CmdTemplateOption option) {
        Function function = convertToFunction(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("create procedure ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, function.getName()));
        sb.append("(");
        // parse params
        List<Param> params = function.getParams();
        for (int i = 0; i < params.size(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            Param param = params.get(i);
            if (StringUtils.isNotBlank(param.getMode())) {
                sb.append(param.getMode()).append(" ");
            }
            sb.append(fmtName(option, param.getName()) + " ");
            EColumn eColumn = parseColumn(param);
            sb.append(MyTypeUtils.buildColumnType(null, eColumn));
        }
        sb.append(")\n");

        if (parseBoolean(option, MyUmiAttributeNames.DETERMINISTIC)) {
            sb.append(DETERMINISTIC).append("\n");
        }
        if (isNotBlank(option, MyUmiAttributeNames.SQL_DATA_ACCESS)) {
            sb.append(parseString(option, MyUmiAttributeNames.SQL_DATA_ACCESS));
            sb.append("\n");
        }
        sb.append(function.getSql());
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropTableIndex(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop index ");
        sb.append(fmtName(option, option.getTargetExactName()));
        sb.append(" on ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropTablePrimaryKey(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("alter table ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetName()));
        sb.append(" drop primary key");
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    protected Dialect getDialect() { return MySqlDialect.INSTANCE; }

}
