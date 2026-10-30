/* Copyright 2026 杭州开云集致科技有限公司. Licensed under the Apache License, Version 2.0. */
package com.clougence.clouddm.ds.hologres.sql.parser;

import com.clougence.sql.postgres.parser.PgSplitAnalysisSpi;
import com.clougence.sql.postgres.parser.PgSplitVisitor;
import com.clougence.sql.postgres.parser.PostgresVersion;

public class HgSplitAnalysisSpi extends PgSplitAnalysisSpi {
    public HgSplitAnalysisSpi(PostgresVersion version){
        super(version);
    }

    @Override
    protected PgSplitVisitor newSplitVisitor() {
        return new HgSplitVisitor(version());
    }
}
