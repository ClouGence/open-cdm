package com.clougence.clouddm.ds.starrocks.definition.ui.template;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.ds.starrocks.dialect.StarRocksDialect;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.template.MyCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.schema.dialect.Dialect;

public class SrCmdTemplateSpi extends MyCmdTemplateSpi {

    @Override
    public List<String> getRenameSchema(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("alter database ");
        sb.append(fmtName(option, option.getSchema()));
        sb.append(" rename ");
        sb.append(fmtName(option, option.getTargetNewName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropTable(CmdTemplateOption option) {
        option.setCascade(false);
        return super.getDropTable(option);
    }

    @Override
    protected Dialect getDialect() { return StarRocksDialect.INSTANCE; }
}
