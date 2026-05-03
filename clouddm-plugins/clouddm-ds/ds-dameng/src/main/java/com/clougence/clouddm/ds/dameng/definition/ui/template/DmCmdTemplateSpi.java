package com.clougence.clouddm.ds.dameng.definition.ui.template;

import static com.clougence.adapter.dameng.DmAttributeNames.OLD_ALIAS;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.dameng.DmAttributeNames;
import com.clougence.clouddm.ds.dameng.dialect.DmDialect;
import com.clougence.clouddm.sdk.ui.template.AbstractCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

public class DmCmdTemplateSpi extends AbstractCmdTemplateSpi {

    @Override
    public String getQuickQuery(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ");
        sb.append(TABLE_PLACEHOLDER);
        if (option.getDefaultLimit() > 0) {
            sb.append(" limit ").append(option.getDefaultLimit());
        }
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
    public String getQuickQueryByProcedure(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("call \"");
        sb.append(SCHEMA_PLACEHOLDER);
        sb.append("\".\"");
        sb.append(TABLE_PLACEHOLDER);
        sb.append("\"(");
        sb.append(PARAM_PLACEHOLDER);
        sb.append(");");
        return sb.toString();
    }

    @Override
    public String getQuickQueryByFunction(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("select \"");
        sb.append(SCHEMA_PLACEHOLDER);
        sb.append("\".\"");
        sb.append(TABLE_PLACEHOLDER);
        sb.append("\"(");
        sb.append(PARAM_PLACEHOLDER);
        sb.append(");");
        return sb.toString();
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
        if (option.isCascade()) {
            sb.append(" cascade");
        }
        if (option.isRestrict()) {
            sb.append(" restrict");
        }
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
        if (option.isRestrict()) {
            sb.append(" restrict");
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
    public List<String> getDropView(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop view ");
        if (option.isUsingExists()) {
            sb.append("if exists ");
        }
        sb.append(fmtName(option, option.getSchema()));
        sb.append(".");
        sb.append(fmtName(option, option.getTargetName()));
        if (option.isCascade()) {
            sb.append(" cascade");
        }
        if (option.isRestrict()) {
            sb.append(" restrict");
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
    public List<String> getCreateView(CmdTemplateOption option) {
        View view = convertToView(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("create view ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, view.getName()));
        sb.append("\nas\n");
        sb.append(view.getSql());
        sb.append(";");

        if (StringUtils.isNotEmpty(view.getComment())) {
            String viewComment = getViewComment(view, option);

            return CollectionUtils.asList(sb.toString(), viewComment);
        }

        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getAlterView(CmdTemplateOption option) {
        View view = convertToView(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("create or replace view ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, view.getName()));
        sb.append("\nas\n");
        sb.append(view.getSql());
        sb.append(";");

        String viewComment = getViewComment(view, option);

        return CollectionUtils.asList(sb.toString(), viewComment);

    }

    private String getViewComment(View view, CmdTemplateOption option) {
        StringBuilder commentSb = new StringBuilder();
        commentSb.append("comment on view ");
        commentSb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, view.getName()));
        commentSb.append(" is ");
        commentSb.append("'");
        commentSb.append(view.getComment());
        commentSb.append("'");
        return commentSb.toString();
    }

    @Override
    public List<String> getCreateTrigger(CmdTemplateOption option) {
        Trigger trigger = convertToTrigger(option.getData());
        Map<String, Object> data = option.getData();
        option.setDelimited(true);
        StringBuilder sb = new StringBuilder();
        sb.append("create ");
        if (option.isReplace()) {
            sb.append("or replace ");
        }
        sb.append("trigger ");
        sb.append(fmtName(option, trigger.getName()));
        sb.append(" \n");
        if (parseBoolean(option, DmAttributeNames.ENCRYPTION)) {
            sb.append("with encryption \n");
        }
        sb.append(trigger.getTime());
        sb.append(" ");
        for (int i = 0; i < trigger.getEventList().size(); i++) {
            if (i != 0) {
                sb.append("or ");
            }
            String event = trigger.getEventList().get(i);
            sb.append(event + " ");
            if (event.equals("update")) {
                List<String> columns = trigger.getColumnList();
                if (columns != null && columns.size() != 0) {
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
        sb.append("\non ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, trigger.getTriggerTable()));
        sb.append("\n");
        // handle features
        if ("row".equalsIgnoreCase(parseString(option, DmAttributeNames.TRIGGER_GRANULARITY))) {
            if (isNotEmpty(option, DmAttributeNames.NEW_ALIAS) || isNotEmpty(option, OLD_ALIAS)) {
                sb.append("referencing");
                if (isNotEmpty(option, OLD_ALIAS)) {
                    sb.append(" old row as ").append(fmtName(option, parseString(option, OLD_ALIAS)));
                }
                if (isNotEmpty(option, DmAttributeNames.NEW_ALIAS)) {
                    sb.append(" new row as ").append(fmtName(option, parseString(option, DmAttributeNames.NEW_ALIAS)));
                }
                sb.append("\n");
            }
        }

        sb.append("for each " + parseString(option, DmAttributeNames.TRIGGER_GRANULARITY) + "\n");

        if ("row".equalsIgnoreCase(parseString(option, DmAttributeNames.TRIGGER_GRANULARITY))) {
            if (isNotEmpty(option, DmAttributeNames.CONDITION)) {
                sb.append("when(").append(data.get(DmAttributeNames.CONDITION.getCodeKey())).append(")\n");
            }
        }

        sb.append(trigger.getSql());
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getCreateProcedure(CmdTemplateOption option) {
        Function procedure = convertToFunction(option.getData());

        StringBuilder sb = new StringBuilder();
        sb.append("create procedure ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, procedure.getName()));
        // start params
        if (CollectionUtils.isNotEmpty(procedure.getParams())) {
            sb.append("(");
            for (int i = 0; i < procedure.getParams().size(); i++) {
                if (i != 0) {
                    sb.append(",");
                }
                Param param = procedure.getParams().get(i);
                sb.append(parseParam(param));
            }
            sb.append(")");
        }

        sb.append("\nas").append("\n");
        sb.append(procedure.getSql());
        sb.append(";");

        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getCreateFunction(CmdTemplateOption option) {
        Function function = convertToFunction(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("create function ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, function.getName()));
        // start params
        sb.append("(");
        for (int i = 0; i < function.getParams().size(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            Param param = function.getParams().get(i);
            sb.append(parseParam(param));
        }
        sb.append(")\n");
        sb.append("return ").append(function.getReturnType().getParamType());
        sb.append("\n").append("as").append("\n");
        sb.append(function.getSql());
        sb.append(";");

        return Collections.singletonList(sb.toString());
    }

    private String parseParam(Param param) {
        StringBuilder sb = new StringBuilder();
        sb.append(param.getName()).append(" ");
        if (StringUtils.isNotBlank(param.getMode())) {
            sb.append(param.getMode()).append(" ");
        }
        sb.append(param.getParamType().toLowerCase());
        if (StringUtils.isNotBlank(param.getMode()) && "IN".equalsIgnoreCase(param.getMode()) && param.getDefaultValue() != null) {
            sb.append(" default ").append("'").append(param.getDefaultValue()).append("'");
        }
        return sb.toString();
    }

    @Override
    public List<String> getDropFunction(CmdTemplateOption option) {
        return Collections.singletonList("drop function " + fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()) + ";");
    }

    @Override
    public List<String> getDropProcedure(CmdTemplateOption option) {
        return Collections.singletonList("drop procedure " + fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()) + ";");
    }

    @Override
    public List<String> getEnableConstraint(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("alter table ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetName()));
        sb.append(" enable constraint ");
        sb.append(fmtName(option, option.getTargetExactName()));
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDisableConstraint(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("alter table ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetName()));
        sb.append(" disable constraint ");
        sb.append(fmtName(option, option.getTargetExactName()));
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropUser(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop user ").append(fmtName(option, option.getTargetName()));
        if (option.isCascade()) {
            sb.append(" cascade");
        }
        return Collections.singletonList(sb.toString());
    }

    @Override
    protected Dialect getDialect() { return DmDialect.INSTANCE; }
}
