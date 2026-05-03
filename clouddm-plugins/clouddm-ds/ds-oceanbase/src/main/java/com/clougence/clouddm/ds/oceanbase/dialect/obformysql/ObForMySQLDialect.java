package com.clougence.clouddm.ds.oceanbase.dialect.obformysql;

import com.clougence.clouddm.dsfamily.mysql.dialect.MySqlDialect;
import com.clougence.schema.dialect.Dialect;

/**
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public class ObForMySQLDialect extends MySqlDialect {

    public static Dialect INSTANCE = new ObForMySQLDialect();

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/obformysql.keywords";
    }

}
