/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.ds.spi.rewrite.db2;

import com.clougence.clouddm.ds.spi.rewrite.RewriteTextCase;
import com.clougence.clouddm.ds.spi.rewrite.RewriteTextTest;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.sql.db2.editor.rewrite.Db2RewriteSpi;

public final class Db2RewriteTextTest extends RewriteTextTest {

    public Db2RewriteTextTest(){
        super("spi/rewrite/db2");
    }

    @Override
    protected RewriteSpi rewriteSpi(RewriteTextCase testCase) {
        return new Db2RewriteSpi();
    }
}
