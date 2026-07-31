package com.clougence.clouddm.ds.lineage.adb;

import com.clougence.clouddm.ds.lineage.BasicSingleDataSourceLineageTextTest;

public abstract class BasicAdbLineageTextTest extends BasicSingleDataSourceLineageTextTest {
    protected BasicAdbLineageTextTest(int shardCount, int shardId){
        super("lineage/adb", shardCount, shardId);
    }
}
final class AdbLineageShard1Test extends BasicAdbLineageTextTest { AdbLineageShard1Test(){ super(3, 0); } }
final class AdbLineageShard2Test extends BasicAdbLineageTextTest { AdbLineageShard2Test(){ super(3, 1); } }
final class AdbLineageShard3Test extends BasicAdbLineageTextTest { AdbLineageShard3Test(){ super(3, 2); } }
