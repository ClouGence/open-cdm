package com.clougence.clouddm.ds.split.postgres;

import java.util.List;

import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.split.SplitTextTest;

/** Keeps root PostgreSQL fixtures covered while they are migrated into version directories. */
public final class PostgresLegacySplitTextTest extends SplitTextTest {

    private static final String RESOURCE_DIRECTORY = "split/postgres";

    @Override
    protected List<String> fixtureResources() {
        return TextCaseSupport.resourceFiles(RESOURCE_DIRECTORY, PostgresLegacySplitTextTest::isRootFixture);
    }

    private static boolean isRootFixture(String path) {
        String relative = path.substring(RESOURCE_DIRECTORY.length() + 1);
        return !relative.contains("/");
    }
}
