package com.clougence.clouddm.ds.lineage.dameng;

import com.clougence.clouddm.ds.lineage.BasicSingleDataSourceLineageTextTest;

public abstract class BasicDamengLineageTextTest extends BasicSingleDataSourceLineageTextTest {
    protected BasicDamengLineageTextTest(int shardCount, int shardId){
        super("lineage/dameng/8", shardCount, shardId);
    }
}
final class DamengLineageShard1Test extends BasicDamengLineageTextTest { DamengLineageShard1Test(){ super(3, 0); } }
final class DamengLineageShard2Test extends BasicDamengLineageTextTest { DamengLineageShard2Test(){ super(3, 1); } }
final class DamengLineageShard3Test extends BasicDamengLineageTextTest { DamengLineageShard3Test(){ super(3, 2); } }
