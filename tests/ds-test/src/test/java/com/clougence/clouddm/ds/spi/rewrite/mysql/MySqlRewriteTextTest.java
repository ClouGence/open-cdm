/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.ds.spi.rewrite.mysql;

import com.clougence.clouddm.ds.spi.rewrite.RewriteTextCase;
import com.clougence.clouddm.ds.spi.rewrite.RewriteTextTest;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.sql.mysql.editor.rewrite.MyRewriteSpi;
import com.clougence.sql.mysql.parser.MySqlParserConfig;

public final class MySqlRewriteTextTest extends RewriteTextTest {

    public MySqlRewriteTextTest(){
        super("spi/rewrite/mysql");
    }

    @Override
    protected RewriteSpi rewriteSpi(RewriteTextCase testCase) {
        String path = testCase.resourcePath();
        if (path.contains("/5.6/")) {
            return new MyRewriteSpi(MySqlParserConfig.unknownSqlMode("5.6.51"));
        }
        if (path.contains("/5.7/")) {
            return new MyRewriteSpi(MySqlParserConfig.unknownSqlMode("5.7.44"));
        }
        if (path.contains("/8.0/")) {
            return new MyRewriteSpi(MySqlParserConfig.unknownSqlMode("8.0.45"));
        }
        if (path.contains("/8.4/")) {
            return new MyRewriteSpi(MySqlParserConfig.unknownSqlMode("8.4.8"));
        }
        if (path.contains("/9.7/")) {
            return new MyRewriteSpi(MySqlParserConfig.unknownSqlMode("9.7.0"));
        }
        throw new IllegalArgumentException("Unsupported MySQL rewrite fixture: " + path);
    }
}
