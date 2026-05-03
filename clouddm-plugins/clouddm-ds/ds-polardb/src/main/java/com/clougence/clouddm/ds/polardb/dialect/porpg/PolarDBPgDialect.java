package com.clougence.clouddm.ds.polardb.dialect.porpg;

import com.clougence.clouddm.dsfamily.schema.dialect.AbstractDialect;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.StringUtils;

/**
 * PostgreSQL 对象名有大小写敏感不敏感的问题
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public class PolarDBPgDialect extends AbstractDialect {

    public static Dialect INSTANCE = new PolarDBPgDialect();

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/polar-pg.keywords";
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
