package com.clougence.clouddm.ds.lineage.maxcompute;

import com.clougence.clouddm.ds.lineage.BasicSingleDataSourceLineageTextTest;

public abstract class BasicMaxComputeLineageTextTest extends BasicSingleDataSourceLineageTextTest {
    protected BasicMaxComputeLineageTextTest(int shardCount, int shardId){
        super("lineage/maxcompute", shardCount, shardId);
    }
}
final class MaxComputeLineageShard1Test extends BasicMaxComputeLineageTextTest { MaxComputeLineageShard1Test(){ super(3, 0); } }
final class MaxComputeLineageShard2Test extends BasicMaxComputeLineageTextTest { MaxComputeLineageShard2Test(){ super(3, 1); } }
final class MaxComputeLineageShard3Test extends BasicMaxComputeLineageTextTest { MaxComputeLineageShard3Test(){ super(3, 2); } }
