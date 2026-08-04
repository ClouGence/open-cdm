package com.clougence.clouddm.ds.split.postgres;

import com.clougence.sql.postgres.parser.PostgresVersion;

public final class Postgres16SplitTextTest extends PostgresSplitTextTest {
    public Postgres16SplitTextTest(){
        super("16", PostgresVersion.POSTGRES_16);
    }
}
