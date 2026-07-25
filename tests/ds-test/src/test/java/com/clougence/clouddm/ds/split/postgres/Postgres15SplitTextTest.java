package com.clougence.clouddm.ds.split.postgres;

import com.clougence.sql.postgres.parser.PostgresVersion;

public final class Postgres15SplitTextTest extends PostgresSplitTextTest {
    public Postgres15SplitTextTest(){
        super("15", PostgresVersion.POSTGRES_15);
    }
}
