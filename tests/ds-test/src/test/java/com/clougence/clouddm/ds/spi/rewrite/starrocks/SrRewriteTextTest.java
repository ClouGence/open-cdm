/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.ds.spi.rewrite.starrocks;

import com.clougence.clouddm.ds.spi.rewrite.RewriteTextCase;
import com.clougence.clouddm.ds.spi.rewrite.RewriteTextTest;
import com.clougence.clouddm.ds.starrocks.sql.editor.rewrite.SrRewriteSpi;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;

public final class SrRewriteTextTest extends RewriteTextTest {

    public SrRewriteTextTest(){
        super("spi/rewrite/starrocks");
    }

    @Override
    protected RewriteSpi rewriteSpi(RewriteTextCase testCase) {
        return new SrRewriteSpi();
    }
}
