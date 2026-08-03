package com.clougence.clouddm.ds.lineage.oracle;

import com.clougence.clouddm.ds.lineage.BasicSingleDataSourceLineageTextTest;

public abstract class BasicOracleLineageTextTest extends BasicSingleDataSourceLineageTextTest {
    protected BasicOracleLineageTextTest(int shardCount, int shardId){
        super("lineage/oracle", shardCount, shardId);
    }
}
final class OracleLineageShard1Test extends BasicOracleLineageTextTest { OracleLineageShard1Test(){ super(3, 0); } }
final class OracleLineageShard2Test extends BasicOracleLineageTextTest { OracleLineageShard2Test(){ super(3, 1); } }
final class OracleLineageShard3Test extends BasicOracleLineageTextTest { OracleLineageShard3Test(){ super(3, 2); } }
