/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.ds.spi.rewrite.sqlserver;

import com.clougence.clouddm.ds.spi.rewrite.RewriteTextCase;
import com.clougence.clouddm.ds.spi.rewrite.RewriteTextTest;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.sql.sqlserver.editor.rewrite.MsSqlRewriteSpi;

public final class MsSqlRewriteTextTest extends RewriteTextTest {

    public MsSqlRewriteTextTest(){
        super("spi/rewrite/sqlserver");
    }

    @Override
    protected RewriteSpi rewriteSpi(RewriteTextCase testCase) {
        return new MsSqlRewriteSpi();
    }
}
