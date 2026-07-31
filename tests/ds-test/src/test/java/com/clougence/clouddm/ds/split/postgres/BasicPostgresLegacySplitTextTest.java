package com.clougence.clouddm.ds.split.postgres;

import java.util.List;

import com.clougence.clouddm.ds.TextResourceShard;
import com.clougence.clouddm.ds.split.BasicSplitTextTest;

/** Keeps root PostgreSQL fixtures covered while they are migrated into version directories. */
public abstract class BasicPostgresLegacySplitTextTest extends BasicSplitTextTest {

    private static final String RESOURCE_DIRECTORY = "split/postgres";
    private final TextResourceShard fixtureShard;

    protected BasicPostgresLegacySplitTextTest(int shardCount, int shardId){
        this.fixtureShard = new TextResourceShard(RESOURCE_DIRECTORY, shardCount, shardId);
    }

    @Override
    protected List<String> fixtureResources() {
        return fixtureShard.resourceFiles(BasicPostgresLegacySplitTextTest::isRootFixture);
    }

    private static boolean isRootFixture(String path) {
        String relative = path.substring(RESOURCE_DIRECTORY.length() + 1);
        return !relative.contains("/");
    }
}

final class PostgresLegacySplitShard1Test extends BasicPostgresLegacySplitTextTest { PostgresLegacySplitShard1Test(){ super(3, 0); } }
final class PostgresLegacySplitShard2Test extends BasicPostgresLegacySplitTextTest { PostgresLegacySplitShard2Test(){ super(3, 1); } }
final class PostgresLegacySplitShard3Test extends BasicPostgresLegacySplitTextTest { PostgresLegacySplitShard3Test(){ super(3, 2); } }
