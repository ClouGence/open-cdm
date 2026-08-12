/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.ds.spi.rewrite.oracle;

import com.clougence.clouddm.ds.spi.rewrite.RewriteTextCase;
import com.clougence.clouddm.ds.spi.rewrite.RewriteTextTest;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.sql.oracle.editor.rewrite.OraRewriteSpi;

public final class OraRewriteTextTest extends RewriteTextTest {

    public OraRewriteTextTest(){
        super("spi/rewrite/oracle");
    }

    @Override
    protected RewriteSpi rewriteSpi(RewriteTextCase testCase) {
        return new OraRewriteSpi();
    }
}
