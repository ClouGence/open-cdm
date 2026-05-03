package com.clougence.clouddm.ds.oracle.dialect;

import com.clougence.clouddm.dsfamily.schema.dialect.AbstractDialect;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.StringUtils;

/**
 * Oracle 的 SqlDialect 实现
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public class OracleDialect extends AbstractDialect {

    public static Dialect INSTANCE = new OracleDialect();

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/oracle.keywords";
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
