package com.clougence.clouddm.ds.hana.dialect;

import com.clougence.clouddm.dsfamily.schema.dialect.AbstractDialect;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.StringUtils;

/**
 * Hana 的 SqlDialect 实现
 * @version : 2020-10-31
 * @author hanlizhi
 */
public class HanaDialect extends AbstractDialect {

    public static Dialect INSTANCE = new HanaDialect();

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/hana.keywords";
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
