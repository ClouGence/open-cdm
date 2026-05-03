package com.clougence.clouddm.ds.doris.definition.ui.template;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.ds.doris.dialect.DorisDialect;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.template.MyCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.schema.dialect.Dialect;

public class DrCmdTemplateSpi extends MyCmdTemplateSpi {

    @Override
    public List<String> getRenameSchema(CmdTemplateOption option) {
        return Collections.singletonList("alter database " + fmtName(option, option.getSchema()) + " rename " + fmtName(option, option.getTargetNewName()) + ";");
    }

    @Override
    public List<String> getDropTable(CmdTemplateOption option) {
        option.setCascade(false);
        return super.getDropTable(option);
    }

    @Override
    protected Dialect getDialect() { return DorisDialect.INSTANCE; }
}
