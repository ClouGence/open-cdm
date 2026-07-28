package com.clougence.clouddm.ds.split.dameng;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.split.SingleDataSourceSplitTextTest;

public final class DamengSplitTextTest extends SingleDataSourceSplitTextTest {

    @Override
    protected String datasource() {
        return "dameng";
    }

    @Override
    protected List<String> fixtureResources() {
        return TextCaseSupport.resourceFiles("split/dameng", path -> !path.contains("/reject/"));
    }

    @TestFactory
    public Stream<DynamicTest> rejectedScripts() {
        return rejectedDynamicTests(TextCaseSupport.resourceFiles("split/dameng", path -> path.contains("/reject/")), "dameng");
    }
}
