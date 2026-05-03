package com.clougence.clouddm.dsfamily.schema.dialect;

import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.StringUtils;

/**
 * 默认 SqlDialect 实现
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public class DefaultDialect extends AbstractDialect {

    public static Dialect DEFAULT = new DefaultDialect();

    @Override
    protected String keyWordsResource() {
        return null;
    }

    public String fmtComment(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("'", "''");
    }
}
