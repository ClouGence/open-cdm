package com.clougence.clouddm.ds.mongodb.definition.ui.template;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.ui.template.AbstractCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.dsfamily.schema.dialect.DefaultDialect;
import com.clougence.schema.dialect.Dialect;

public class MongoCmdTemplateSpi extends AbstractCmdTemplateSpi {

    @Override
    protected Dialect getDialect() { return DefaultDialect.DEFAULT; }

    @Override
    public List<String> getDropTable(CmdTemplateOption option) {
        String result = "db.getCollection(\"" + option.getTargetName() + "\").drop();";
        return Collections.singletonList(result);
    }

    @Override
    public List<String> getDropView(CmdTemplateOption option) {
        return this.getDropTable(option);
    }

    @Override
    public List<String> getCreateSchema(CmdTemplateOption option) {
        String useDatabase = "use " + option.getSchema() + ";";
        String defaultCollection = "db.createCollection(\"test\");";
        return Arrays.asList(useDatabase, defaultCollection);
    }

    @Override
    public String getQuickQuery(CmdTemplateOption option) {
        return "db.getCollection(\"" + TABLE_PLACEHOLDER + "\").find();";
    }

    @Override
    public List<String> getRenameTable(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("db.getCollection(\"").append(option.getTargetName()).append("\").renameCollection(\"").append(option.getTargetNewName()).append("\");");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public String getQuickQueryByTable(CmdTemplateOption option) {
        return this.getQuickQuery(option);
    }

    @Override
    public String getQuickQueryByView(CmdTemplateOption option) {
        return this.getQuickQuery(option);
    }
}
