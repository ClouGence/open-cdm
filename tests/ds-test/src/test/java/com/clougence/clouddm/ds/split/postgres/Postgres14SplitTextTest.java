package com.clougence.clouddm.ds.split.postgres;

import com.clougence.sql.postgres.parser.PostgresVersion;

public final class Postgres14SplitTextTest extends PostgresSplitTextTest {
    public Postgres14SplitTextTest(){
        super("14", PostgresVersion.POSTGRES_14);
    }
}
