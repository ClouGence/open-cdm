package com.clougence.clouddm.ds.hologres.definition.ui.template;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.postgres.definition.ui.template.PgCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;

public class HgCmdTemplateSpi extends PgCmdTemplateSpi {

    @Override
    public List<String> getRenameView(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("alter view ");
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()));
        sb.append(" rename to ");
        sb.append(fmtName(option, option.getTargetNewName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }
}
