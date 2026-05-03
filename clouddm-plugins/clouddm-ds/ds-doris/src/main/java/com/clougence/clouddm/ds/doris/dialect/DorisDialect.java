package com.clougence.clouddm.ds.doris.dialect;

import com.clougence.clouddm.dsfamily.mysql.dialect.MySqlDialect;
import com.clougence.schema.dialect.Dialect;

/**
 * MySQL 的 SqlDialect 实现
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public class DorisDialect extends MySqlDialect {

    public static Dialect INSTANCE = new DorisDialect();

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/doris.keywords";
    }

}
