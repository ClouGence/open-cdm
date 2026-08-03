package com.clougence.clouddm.ds.split.dameng;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.ds.split.BasicSingleDataSourceSplitTextTest;

public abstract class BasicDamengSplitTextTest extends BasicSingleDataSourceSplitTextTest {

    protected BasicDamengSplitTextTest(int shardCount, int shardId){
        super("split/dameng/8", shardCount, shardId);
    }

    @Override
    protected List<String> fixtureResources() {
        return fixtureResources(path -> !path.contains("/reject/"));
    }

    @TestFactory
    public Stream<DynamicTest> rejectedScripts() {
        return rejectedDynamicTests(fixtureResources(path -> path.contains("/reject/")), "dameng");
    }
}

final class DamengSplitShard1Test extends BasicDamengSplitTextTest { DamengSplitShard1Test(){ super(3, 0); } }
final class DamengSplitShard2Test extends BasicDamengSplitTextTest { DamengSplitShard2Test(){ super(3, 1); } }
final class DamengSplitShard3Test extends BasicDamengSplitTextTest { DamengSplitShard3Test(){ super(3, 2); } }
