package com.clougence.clouddm.ds.ads.definition.ui.template;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.mysql.definition.ui.template.MyCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;

public class AdsMyCmdTemplateSpi extends MyCmdTemplateSpi {

    @Override
    public List<String> getDropSchema(CmdTemplateOption option) {
        return this.getDropCatalog(option);
    }

    @Override
    public List<String> getDropCatalog(CmdTemplateOption option) {
        String sb = "drop database " + fmtName(option, option.getSchema()) + ";";
        return Collections.singletonList(sb);
    }
}
