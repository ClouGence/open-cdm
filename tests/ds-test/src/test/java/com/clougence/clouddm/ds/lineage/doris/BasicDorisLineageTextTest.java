package com.clougence.clouddm.ds.lineage.doris;

import com.clougence.clouddm.ds.lineage.BasicSingleDataSourceLineageTextTest;

public abstract class BasicDorisLineageTextTest extends BasicSingleDataSourceLineageTextTest {
    protected BasicDorisLineageTextTest(int shardCount, int shardId){
        super("lineage/doris", shardCount, shardId);
    }
}
final class DorisLineageShard1Test extends BasicDorisLineageTextTest { DorisLineageShard1Test(){ super(3, 0); } }
final class DorisLineageShard2Test extends BasicDorisLineageTextTest { DorisLineageShard2Test(){ super(3, 1); } }
final class DorisLineageShard3Test extends BasicDorisLineageTextTest { DorisLineageShard3Test(){ super(3, 2); } }
