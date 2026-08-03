package com.clougence.clouddm.ds.split;

import java.util.List;
import java.util.function.Predicate;

import com.clougence.clouddm.ds.TextResourceShard;

public abstract class BasicSingleDataSourceSplitTextTest extends BasicSplitTextTest {

    private final String            datasource;
    private final TextResourceShard fixtureShard;

    protected BasicSingleDataSourceSplitTextTest(String resourceDirectory, int shardCount, int shardId){
        String prefix = "split/";
        if (!resourceDirectory.startsWith(prefix)) {
            throw new IllegalArgumentException("Split resource parent must start with " + prefix + ": " + resourceDirectory);
        }
        String relative = resourceDirectory.substring(prefix.length());
        int separator = relative.indexOf('/');
        this.datasource = separator < 0 ? relative : relative.substring(0, separator);
        this.fixtureShard = new TextResourceShard(resourceDirectory, shardCount, shardId);
    }

    protected final String datasource() {
        return datasource;
    }

    @Override
    protected List<String> fixtureResources() {
        return fixtureShard.resourceFiles();
    }

    protected final List<String> fixtureResources(Predicate<String> filter) {
        return fixtureShard.resourceFiles(filter);
    }

    @Override
    protected final boolean accepts(Fixture fixture) {
        return datasource().equals(fixture.datasource());
    }
}
