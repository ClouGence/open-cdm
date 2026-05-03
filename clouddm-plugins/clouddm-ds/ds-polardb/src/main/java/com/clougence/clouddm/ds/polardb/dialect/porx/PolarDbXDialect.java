package com.clougence.clouddm.ds.polardb.dialect.porx;

import com.clougence.clouddm.dsfamily.mysql.dialect.MySqlDialect;
import com.clougence.schema.dialect.Dialect;

/**
 * @author zylicfc
 */
public class PolarDbXDialect extends MySqlDialect {

    public static Dialect INSTANCE = new PolarDbXDialect();

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/polar-x.keywords";
    }

}
