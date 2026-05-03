package com.clougence.clouddm.ds.sqlserver.dialect;

import com.clougence.clouddm.dsfamily.schema.dialect.AbstractDialect;
import com.clougence.utils.StringUtils;

public class SqlServerDialect extends AbstractDialect {

    public static SqlServerDialect INSTANCE = new SqlServerDialect();

    @Override
    public int maxArg() {
        return 2000;
    }

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/sqlserver.keywords";
    }

    public String fmtComment(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("'", "''");
    }

    public String fmtName(boolean useQualifier, String fmtString) {
        if (fmtString == null) {
            return null;
        }
        if (StringUtils.contains(fmtString, "]")) {
            return super.fmtName(true, fmtString.replace("]", "]]"));
        } else {
            return super.fmtName(useQualifier, fmtString);
        }
    }

    @Override
    public String leftQualifier() {
        return "[";
    }

    @Override
    public String rightQualifier() {
        return "]";
    }
}
