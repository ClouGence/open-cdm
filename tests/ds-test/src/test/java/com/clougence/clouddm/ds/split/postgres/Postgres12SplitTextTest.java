package com.clougence.clouddm.ds.split.postgres;

import com.clougence.sql.postgres.parser.PostgresVersion;

public final class Postgres12SplitTextTest extends PostgresSplitTextTest {
    public Postgres12SplitTextTest(){
        super("12", PostgresVersion.POSTGRES_12);
    }
}
