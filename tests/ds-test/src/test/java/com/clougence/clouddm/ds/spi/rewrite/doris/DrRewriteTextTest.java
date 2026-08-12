/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.ds.spi.rewrite.doris;

import com.clougence.clouddm.ds.spi.rewrite.RewriteTextCase;
import com.clougence.clouddm.ds.spi.rewrite.RewriteTextTest;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.sql.doris.editor.rewrite.DrRewriteSpi;

public final class DrRewriteTextTest extends RewriteTextTest {

    public DrRewriteTextTest(){
        super("spi/rewrite/doris");
    }

    @Override
    protected RewriteSpi rewriteSpi(RewriteTextCase testCase) {
        return new DrRewriteSpi();
    }
}
