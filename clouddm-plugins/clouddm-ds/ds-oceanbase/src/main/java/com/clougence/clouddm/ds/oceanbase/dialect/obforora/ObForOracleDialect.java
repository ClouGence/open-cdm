package com.clougence.clouddm.ds.oceanbase.dialect.obforora;

import com.clougence.clouddm.ds.oracle.dialect.OracleDialect;
import com.clougence.schema.dialect.Dialect;

/**
 * @author chunlin create time is 2024/8/27
 */
public class ObForOracleDialect extends OracleDialect {

    public static Dialect INSTANCE = new ObForOracleDialect();

    @Override
    protected String keyWordsResource() {
        return "/META-INF/clougence/db-keywords/obfororacle.keywords";
    }
}
