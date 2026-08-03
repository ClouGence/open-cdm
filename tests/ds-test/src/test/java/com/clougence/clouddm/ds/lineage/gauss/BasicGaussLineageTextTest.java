package com.clougence.clouddm.ds.lineage.gauss;

import com.clougence.clouddm.ds.lineage.BasicSingleDataSourceLineageTextTest;

public abstract class BasicGaussLineageTextTest extends BasicSingleDataSourceLineageTextTest {
    protected BasicGaussLineageTextTest(int shardCount, int shardId){
        super("lineage/gauss", shardCount, shardId);
    }
}
final class GaussLineageShard1Test extends BasicGaussLineageTextTest { GaussLineageShard1Test(){ super(3, 0); } }
final class GaussLineageShard2Test extends BasicGaussLineageTextTest { GaussLineageShard2Test(){ super(3, 1); } }
final class GaussLineageShard3Test extends BasicGaussLineageTextTest { GaussLineageShard3Test(){ super(3, 2); } }
