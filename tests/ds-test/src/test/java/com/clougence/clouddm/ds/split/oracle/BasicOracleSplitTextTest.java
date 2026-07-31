package com.clougence.clouddm.ds.split.oracle;

import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicOracleSplitTextTest extends BasicSingleDataSourceSplitTextTest {
    protected BasicOracleSplitTextTest(int shardCount, int shardId){
        super("split/oracle", shardCount, shardId);
    }
}

final class OracleSplitShard1Test extends BasicOracleSplitTextTest { OracleSplitShard1Test(){ super(3, 0); } }
final class OracleSplitShard2Test extends BasicOracleSplitTextTest { OracleSplitShard2Test(){ super(3, 1); } }
final class OracleSplitShard3Test extends BasicOracleSplitTextTest { OracleSplitShard3Test(){ super(3, 2); } }
