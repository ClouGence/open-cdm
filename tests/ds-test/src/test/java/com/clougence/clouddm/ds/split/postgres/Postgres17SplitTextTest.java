package com.clougence.clouddm.ds.split.postgres;

import com.clougence.sql.postgres.parser.PostgresVersion;

public final class Postgres17SplitTextTest extends PostgresSplitTextTest {
    public Postgres17SplitTextTest(){
        super("17", PostgresVersion.POSTGRES_17);
    }
}
