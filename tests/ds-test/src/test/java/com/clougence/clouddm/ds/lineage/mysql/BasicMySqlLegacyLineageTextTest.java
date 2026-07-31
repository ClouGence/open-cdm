package com.clougence.clouddm.ds.lineage.mysql;

import java.util.List;

import com.clougence.clouddm.ds.lineage.BasicSingleDataSourceLineageTextTest;
import com.clougence.clouddm.sdk.sql.analysis.lineage.SourceName;

public abstract class BasicMySqlLegacyLineageTextTest extends BasicSingleDataSourceLineageTextTest {

    private static final String RESOURCE_DIRECTORY = "lineage/mysql";
    protected BasicMySqlLegacyLineageTextTest(int shardCount, int shardId){
        super(RESOURCE_DIRECTORY, shardCount, shardId);
    }

    @Override
    protected List<String> fixtureResources() {
        return fixtureResources(path -> !path.substring(RESOURCE_DIRECTORY.length() + 1).contains("/"));
    }

    @Override
    protected String sourcePath(SourceName sourceName) {
        return sourceName.toLocatedDsResPath();
    }
}
final class MySqlLegacyLineageShard1Test extends BasicMySqlLegacyLineageTextTest { MySqlLegacyLineageShard1Test(){ super(3, 0); } }
final class MySqlLegacyLineageShard2Test extends BasicMySqlLegacyLineageTextTest { MySqlLegacyLineageShard2Test(){ super(3, 1); } }
final class MySqlLegacyLineageShard3Test extends BasicMySqlLegacyLineageTextTest { MySqlLegacyLineageShard3Test(){ super(3, 2); } }
