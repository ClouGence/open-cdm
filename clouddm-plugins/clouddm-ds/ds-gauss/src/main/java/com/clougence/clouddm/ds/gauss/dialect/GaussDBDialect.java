package com.clougence.clouddm.ds.gauss.dialect;

import com.clougence.clouddm.dsfamily.schema.dialect.AbstractDialect;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.StringUtils;

public class GaussDBDialect extends AbstractDialect {

    public static Dialect INSTANCE = new GaussDBDialect();

    public int maxArg() {
        return 32767;
    }

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/gaussdb.keywords";
    }

    @Override
    protected String defaultQualifier() {
        return "\"";
    }

    public String fmtComment(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("'", "''");
    }
}
