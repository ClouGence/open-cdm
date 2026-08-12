/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.ds.spi.rewrite.clickhouse;

import com.clougence.clouddm.ds.clickhouse.sql.editor.rewrite.ChRewriteSpi;
import com.clougence.clouddm.ds.spi.rewrite.RewriteTextCase;
import com.clougence.clouddm.ds.spi.rewrite.RewriteTextTest;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;

public final class ChRewriteTextTest extends RewriteTextTest {

    public ChRewriteTextTest(){
        super("spi/rewrite/clickhouse");
    }

    @Override
    protected RewriteSpi rewriteSpi(RewriteTextCase testCase) {
        return new ChRewriteSpi();
    }
}
