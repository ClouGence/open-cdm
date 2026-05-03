package com.clougence.clouddm.ds.hana.definition.ui.template;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.ds.hana.dialect.HanaDialect;
import com.clougence.clouddm.sdk.ui.template.AbstractCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.schema.dialect.Dialect;

public class HanaCmdTemplateSpi extends AbstractCmdTemplateSpi {

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
    protected Dialect getDialect() { return HanaDialect.INSTANCE; }

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
    public String getQuickQueryByTrigger(CmdTemplateOption option) {
        return TABLE_PLACEHOLDER;
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
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getCreateTrigger(CmdTemplateOption option) {
        Trigger trigger = convertToTrigger(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TRIGGER ");
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, trigger.getName()));
        sb.append("\n");
        sb.append(trigger.getTime());
        sb.append(" ");
        List<String> triggerEvent = trigger.getEventList();
        if (triggerEvent != null && !triggerEvent.isEmpty()) {
            sb.append(triggerEvent.get(0).toUpperCase());
            for (int i = 1; i < triggerEvent.size(); i++) {
                String event = triggerEvent.get(i).toUpperCase();
                sb.append(" OR ").append(event);
                if (event.equals("UPDATE")) {
                    List<String> triggerColumns = trigger.getColumnList();
                    if (triggerColumns != null && !triggerColumns.isEmpty()) {
                        sb.append(" OF ");
                        for (int i1 = 0; i1 < triggerColumns.size(); i1++) {
                            if (i1 > 0) {
                                sb.append(",");
                            }
                            sb.append(fmtName(option, triggerColumns.get(i1)));
                        }
                    }
                }
            }
        }
        sb.append(" \nON ");
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, trigger.getTriggerTable()));
        sb.append("\nFOR EACH ROW \n");
        sb.append(trigger.getSql());
        sb.append(";");

        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropTrigger(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TRIGGER ");
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()));
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
    public List<String> getCreateSchema(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE SCHEMA ");
        sb.append(fmtName(option, option.getSchema()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropSchema(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP SCHEMA ");
        sb.append(fmtName(option, option.getSchema()));
        if (option.isCascade()) {
            sb.append(" CASCADE");
        }
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getRenameSchema(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("RENAME SCHEMA ");
        sb.append(fmtName(option, option.getSchema()));
        sb.append(" TO ");
        sb.append(fmtName(option, option.getTargetNewName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropTable(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetName()));
        if (option.isCascade()) {
            sb.append(" CASCADE");
        }
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDeleteTable(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getRenameTable(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("RENAME TABLE ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetName()));
        sb.append(" TO ");
        sb.append(fmtName(option, option.getTargetNewName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getTruncateTable(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("TRUNCATE TABLE ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropView(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP VIEW ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetName()));
        if (option.isCascade()) {
            sb.append(" CASCADE");
        }
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }
}
