package com.clougence.clouddm.dsfamily.db2.dialect;

import com.clougence.clouddm.dsfamily.schema.dialect.AbstractDialect;
import com.clougence.utils.StringUtils;

public class Db2Dialect extends AbstractDialect {

    public static Db2Dialect INSTANCE = new Db2Dialect();

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/db2.keywords";
    }

    public String fmtComment(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("'", "''");
    }

    @Override
    public String leftQualifier() {
        return "\"";
    }

    @Override
    public String rightQualifier() {
        return "\"";
    }
}
