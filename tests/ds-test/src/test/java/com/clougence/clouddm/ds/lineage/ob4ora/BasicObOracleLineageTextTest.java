package com.clougence.clouddm.ds.lineage.ob4ora;

import com.clougence.clouddm.ds.lineage.BasicSingleDataSourceLineageTextTest;

public abstract class BasicObOracleLineageTextTest extends BasicSingleDataSourceLineageTextTest {
    protected BasicObOracleLineageTextTest(int shardCount, int shardId){
        super("lineage/ob4ora", shardCount, shardId);
    }
}
final class ObOracleLineageShard1Test extends BasicObOracleLineageTextTest { ObOracleLineageShard1Test(){ super(3, 0); } }
final class ObOracleLineageShard2Test extends BasicObOracleLineageTextTest { ObOracleLineageShard2Test(){ super(3, 1); } }
final class ObOracleLineageShard3Test extends BasicObOracleLineageTextTest { ObOracleLineageShard3Test(){ super(3, 2); } }
