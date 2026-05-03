package com.clougence.clouddm.ds.oceanbase.definition.obformysql.ui.template;

import java.util.List;

import com.clougence.clouddm.ds.oceanbase.dialect.obformysql.ObForMySQLDialect;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.template.MyCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.schema.dialect.Dialect;

public class ObCmdTemplateSpi extends MyCmdTemplateSpi {

    @Override
    public List<String> getDropTable(CmdTemplateOption option) {
        option.setCascade(false);

        return super.getDropTable(option);
    }

    @Override
    protected Dialect getDialect() { return ObForMySQLDialect.INSTANCE; }
}
