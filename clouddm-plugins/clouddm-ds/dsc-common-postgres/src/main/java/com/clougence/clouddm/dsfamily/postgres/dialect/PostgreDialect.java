package com.clougence.clouddm.dsfamily.postgres.dialect;

import com.clougence.clouddm.dsfamily.schema.dialect.AbstractDialect;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.StringUtils;

/**
 * PostgreSQL 对象名有大小写敏感不敏感的问题
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public class PostgreDialect extends AbstractDialect {

    public static Dialect INSTANCE = new PostgreDialect();

    public int maxArg() {
        //https://blog.csdn.net/xiaowangbadan0_0/article/details/128663635
        return 32767;
    }

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/postgresql.keywords";
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
