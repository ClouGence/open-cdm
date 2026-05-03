package com.clougence.clouddm.ds.dameng.dialect;

import com.clougence.clouddm.dsfamily.schema.dialect.AbstractDialect;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.StringUtils;

/**
 * 达梦 的 SqlDialect 实现
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public class DmDialect extends AbstractDialect {

    public static Dialect INSTANCE = new DmDialect();

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/dm.keywords";
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

    public String fmtValue(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        value = value.replace("'", "''");
        return "'" + value + "'";
    }
}
