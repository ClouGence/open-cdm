package com.clougence.clouddm.ds.polardb.dialect.pormy;

import com.clougence.clouddm.dsfamily.mysql.dialect.MySqlDialect;
import com.clougence.schema.dialect.Dialect;

/**
 * MySQL 的 SqlDialect 实现
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public class PolarDBMyDialect extends MySqlDialect {

    public static Dialect INSTANCE = new PolarDBMyDialect();

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/polar-mysql.keywords";
    }
}
