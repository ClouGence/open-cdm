/* Copyright 2026 杭州开云集致科技有限公司 */
package com.clougence.clouddm.ds.spi.rewrite.postgres;

import com.clougence.clouddm.ds.spi.rewrite.RewriteTextCase;
import com.clougence.clouddm.ds.spi.rewrite.RewriteTextTest;
import com.clougence.clouddm.sdk.sql.editor.rewrite.RewriteSpi;
import com.clougence.sql.postgres.editor.rewrite.PgRewriteSpi;
import com.clougence.sql.postgres.parser.PostgresVersion;

public final class PgRewriteTextTest extends RewriteTextTest {

    public PgRewriteTextTest(){
        super("spi/rewrite/postgres");
    }

    @Override
    protected RewriteSpi rewriteSpi(RewriteTextCase testCase) {
        String path = testCase.resourcePath();
        for (PostgresVersion version : PostgresVersion.values()) {
            String major = version.name().substring("POSTGRES_".length());
            if (path.contains("/" + major + "/")) {
                return new PgRewriteSpi(version);
            }
        }
        throw new IllegalArgumentException("Unsupported PostgreSQL rewrite fixture: " + path);
    }
}
