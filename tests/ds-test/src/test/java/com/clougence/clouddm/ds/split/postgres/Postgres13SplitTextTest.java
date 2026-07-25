package com.clougence.clouddm.ds.split.postgres;

import com.clougence.sql.postgres.parser.PostgresVersion;

public final class Postgres13SplitTextTest extends PostgresSplitTextTest {
    public Postgres13SplitTextTest(){
        super("13", PostgresVersion.POSTGRES_13);
    }
}
