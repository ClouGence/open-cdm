package com.clougence.clouddm.ds.split.adb;

import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicAdbSplitTextTest extends BasicSingleDataSourceSplitTextTest {
    protected BasicAdbSplitTextTest(int shardCount, int shardId){
        super("split/adb", shardCount, shardId);
    }
}

final class AdbSplitShard1Test extends BasicAdbSplitTextTest { AdbSplitShard1Test(){ super(3, 0); } }
final class AdbSplitShard2Test extends BasicAdbSplitTextTest { AdbSplitShard2Test(){ super(3, 1); } }
final class AdbSplitShard3Test extends BasicAdbSplitTextTest { AdbSplitShard3Test(){ super(3, 2); } }
