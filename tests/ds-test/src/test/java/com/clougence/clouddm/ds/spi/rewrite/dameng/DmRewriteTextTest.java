/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.ds.spi.rewrite.dameng;

import com.clougence.clouddm.ds.dameng.sql.editor.rewrite.DmRewriteSpi;
import com.clougence.clouddm.ds.spi.rewrite.RewriteTextCase;
import com.clougence.clouddm.ds.spi.rewrite.RewriteTextTest;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;

public final class DmRewriteTextTest extends RewriteTextTest {

    public DmRewriteTextTest(){
        super("spi/rewrite/dameng");
    }

    @Override
    protected RewriteSpi rewriteSpi(RewriteTextCase testCase) {
        return new DmRewriteSpi();
    }
}
