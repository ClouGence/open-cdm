/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.ds.spi.rewrite.mariadb;

import com.clougence.clouddm.ds.spi.rewrite.RewriteTextCase;
import com.clougence.clouddm.ds.spi.rewrite.RewriteTextTest;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.sql.mysql.editor.rewrite.MyRewriteSpi;
import com.clougence.sql.mysql.parser.MySqlParserConfig;

public final class MarRewriteTextTest extends RewriteTextTest {

    public MarRewriteTextTest(){
        super("spi/rewrite/mariadb");
    }

    @Override
    protected RewriteSpi rewriteSpi(RewriteTextCase testCase) {
        return new MyRewriteSpi(MySqlParserConfig.unknownSqlMode("11.8.5"));
    }
}
