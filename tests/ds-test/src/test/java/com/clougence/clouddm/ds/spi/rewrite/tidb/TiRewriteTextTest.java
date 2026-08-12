/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.ds.spi.rewrite.tidb;

import com.clougence.clouddm.ds.spi.rewrite.RewriteTextCase;
import com.clougence.clouddm.ds.spi.rewrite.RewriteTextTest;
import com.clougence.clouddm.ds.tidb.sql.editor.rewrite.TiRewriteSpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;

public final class TiRewriteTextTest extends RewriteTextTest {

    public TiRewriteTextTest(){
        super("spi/rewrite/tidb");
    }

    @Override
    protected RewriteSpi rewriteSpi(RewriteTextCase testCase) {
        return new TiRewriteSpi();
    }
}
