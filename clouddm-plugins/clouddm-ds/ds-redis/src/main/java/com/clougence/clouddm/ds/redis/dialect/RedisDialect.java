package com.clougence.clouddm.ds.redis.dialect;

import com.clougence.clouddm.dsfamily.schema.dialect.AbstractDialect;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.StringUtils;

/**
 * Redis 的 SqlDialect 实现
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public class RedisDialect extends AbstractDialect {

    public static Dialect INSTANCE = new RedisDialect();

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/redis.keywords";
    }

    @Override
    protected String defaultQualifier() {
        return "\"";
    }

    public String fmtComment(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("\"", "\\\"");
    }
}
