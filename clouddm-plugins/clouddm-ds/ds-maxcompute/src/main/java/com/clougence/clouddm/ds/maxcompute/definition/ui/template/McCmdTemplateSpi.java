package com.clougence.clouddm.ds.maxcompute.definition.ui.template;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.ds.maxcompute.dialect.McDialect;
import com.clougence.clouddm.sdk.ui.template.AbstractCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.schema.dialect.Dialect;

public class McCmdTemplateSpi extends AbstractCmdTemplateSpi {

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
    public List<String> getCreateSchema(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("create schema ");
        sb.append(fmtName(option, option.getSchema()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropSchema(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop schema ");
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
    public List<String> getTruncateTable(CmdTemplateOption option) {
        return Collections.singletonList("truncate table " + fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()) + ";");
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
    public List<String> getDropMaterialized(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop materialized view ");
        if (option.isUsingExists()) {
            sb.append("if exists ");
        }
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()));
        if (option.isPurge()) {
            sb.append(" purge");
        }
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    protected Dialect getDialect() { return McDialect.INSTANCE; }
}
