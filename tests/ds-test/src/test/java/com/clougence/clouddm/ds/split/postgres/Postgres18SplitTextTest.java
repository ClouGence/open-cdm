package com.clougence.clouddm.ds.split.postgres;

import com.clougence.sql.postgres.parser.PostgresVersion;

public final class Postgres18SplitTextTest extends PostgresSplitTextTest {
    public Postgres18SplitTextTest(){
        super("18", PostgresVersion.POSTGRES_18);
    }
}
