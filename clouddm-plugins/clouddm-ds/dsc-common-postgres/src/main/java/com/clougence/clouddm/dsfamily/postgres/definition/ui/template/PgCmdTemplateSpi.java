package com.clougence.clouddm.dsfamily.postgres.definition.ui.template;

import static com.clougence.adapter.postgre.PostgreAttributeNames.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.clougence.adapter.postgre.PostgreAttributeNames;
import com.clougence.clouddm.dsfamily.postgres.dialect.PostgreDialect;
import com.clougence.clouddm.sdk.ui.template.AbstractCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.StringUtils;

public class PgCmdTemplateSpi extends AbstractCmdTemplateSpi {

    protected String CONSTRAINT = "constraint";

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
    public String getQuickQueryByMaterialized(CmdTemplateOption option) {
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
        StringBuilder sb = new StringBuilder();
        sb.append("create database ");
        //        sb.append(DB_PLACEHOLDER);
        sb.append(fmtName(option, option.getCatalog()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
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
        StringBuilder sb = new StringBuilder();
        sb.append("alter database ");
        sb.append(fmtName(option, option.getCatalog()));
        sb.append(" rename to ");
        sb.append(fmtName(option, option.getTargetNewName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
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
        if (option.isCascade()) {
            sb.append(" cascade");
        }
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getRenameSchema(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("alter schema ");
        sb.append(fmtName(option, option.getSchema()));
        sb.append(" rename to ");
        sb.append(fmtName(option, option.getTargetNewName()));
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
        StringBuilder sb = new StringBuilder();
        sb.append("delete from ");
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getRenameTable(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("alter table ");
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()));
        sb.append(" rename to ");
        sb.append(fmtName(option, option.getTargetNewName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getTruncateTable(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("truncate table ");
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()));
        if (option.isCascade()) {
            sb.append(" cascade");
        }
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getRenameView(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("alter table ");
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()));
        sb.append(" rename to ");
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
        sb.append(fmtName(option, option.getTargetName()));
        sb.append(" ");
        sb.append("on ").append(fmtName(option, option.getTriggerTable()));
        if (option.isCascade()) {
            sb.append(" cascade");
        }
        //cascade and restrict can only be chosen one
        if (option.isRestrict() && !option.isCascade()) {
            sb.append(" restrict");
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
        sb.append("\n");
        if (parseBoolean(option, VIEW_SECURITY_BARRIER) || isNotEmpty(option, VIEW_CHECK_OPTION)) {
            sb.append("with(");
            if (parseBoolean(option, VIEW_SECURITY_BARRIER)) {
                sb.append("security_barrier");
            }
            if (isNotEmpty(option, VIEW_CHECK_OPTION)) {
                if (parseBoolean(option, VIEW_SECURITY_BARRIER)) {
                    sb.append(",");
                }
                sb.append("check_option = ").append(parseString(option, VIEW_CHECK_OPTION).toLowerCase());
            }

            sb.append(")\n");
        }
        sb.append("as\n");
        sb.append(view.getSql());
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getCreateFunction(CmdTemplateOption option) {
        Function function = convertToFunction(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("create function ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, function.getName()));
        //        sb.append("\n");
        sb.append("(");
        for (int i = 0; i < function.getParams().size(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            Param param = function.getParams().get(i);
            if (StringUtils.isNotBlank(param.getMode())) {
                sb.append(param.getMode()).append(" ");
            }
            sb.append(param.getName()).append(" ");
            sb.append(param.getParamType());
            if (StringUtils.isNotBlank(param.getMode()) && param.getDefaultValue() != null && !"OUT".equalsIgnoreCase(param.getMode())) {
                sb.append(" default '").append(param.getDefaultValue()).append("'");
            }
        }
        sb.append(")");
        sb.append("\n");
        Param returnType = function.getReturnType();
        sb.append("returns ").append(returnType.getParamType());
        sb.append("\n");
        sb.append("language plpgsql\n");
        sb.append(" as $$\n");
        sb.append(function.getSql());
        sb.append("\n$$");
        //        sb.append(parseString(option, OTHER_OPTIONS));
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getCreateProcedure(CmdTemplateOption option) {
        Function procedure = convertToFunction(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("create procedure ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, procedure.getName()));
        sb.append("(");
        for (int i = 0; i < procedure.getParams().size(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            Param param = procedure.getParams().get(i);
            if (StringUtils.isNotBlank(param.getMode())) {
                sb.append(param.getMode()).append(" ");
            }
            sb.append(param.getName()).append(" ");
            sb.append(param.getParamType());
            if (param.getDefaultValue() != null && !"OUT".equals(param.getMode())) {
                sb.append(" default = '").append(param.getDefaultValue()).append("'");
            }
        }
        sb.append(")\n");
        sb.append("language plpgsql");
        sb.append("\nas ");
        sb.append("$$\n");
        sb.append(procedure.getSql());
        sb.append("\n$$");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getCreateTrigger(CmdTemplateOption option) {
        Trigger trigger = convertToTrigger(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("create ");
        //        boolean constraint = parseBoolean(option, CONSTRAINT);
        //        if (constraint) {
        //            sb.append("constraint ");
        //        }
        sb.append("trigger ");
        sb.append(fmtName(option, trigger.getName()));
        sb.append("\n");
        sb.append(trigger.getTime());
        sb.append(" ");
        for (int i = 0; i < trigger.getEventList().size(); i++) {
            if (i != 0) {
                sb.append("or ");
            }
            String event = trigger.getEventList().get(i);
            sb.append(event).append(" ");
            if (event.equals("update")) {
                List<String> columns = trigger.getColumnList();
                if (columns != null && !columns.isEmpty()) {
                    sb.append("of ");
                    for (int j = 0; j < columns.size(); j++) {
                        if (j != 0) {
                            sb.append(",");
                        }
                        sb.append(fmtName(option, columns.get(j)));
                    }
                }
                sb.append(" ");
            }
        }
        sb.append("\non ").append(trigger.getTriggerTable());

        if (isNotEmpty(option, PostgreAttributeNames.NEW_ALIAS) || isNotEmpty(option, PostgreAttributeNames.OLD_ALIAS)) {
            sb.append("\n");
            sb.append("referencing");
            if (isNotEmpty(option, PostgreAttributeNames.NEW_ALIAS)) {
                sb.append(" new table as ").append(parseString(option, PostgreAttributeNames.NEW_ALIAS));
            }
            if (isNotEmpty(option, PostgreAttributeNames.OLD_ALIAS)) {
                sb.append(" old table as ").append(parseString(option, PostgreAttributeNames.OLD_ALIAS));
            }
        }
        if (isNotEmpty(option, PostgreAttributeNames.TRIGGER_GRANULARITY)) {
            sb.append("\n");
            sb.append("for each ").append(parseString(option, PostgreAttributeNames.TRIGGER_GRANULARITY));
        }

        if (isNotEmpty(option, TRIGGER_CONDITION)) {
            sb.append("\n");
            sb.append("when(").append(parseString(option, TRIGGER_CONDITION)).append(")");
        }
        sb.append("\n");
        sb.append("execute function ").append(trigger.getSql()).append("()");
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getAlterView(CmdTemplateOption option) {
        String drop = getDropView(option).get(0);
        String create = getCreateView(option).get(0);
        return Arrays.asList(drop, create);
    }

    @Override
    public List<String> getDropTableIndex(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop index ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetExactName()));
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
    protected Dialect getDialect() { return PostgreDialect.INSTANCE; }
}
