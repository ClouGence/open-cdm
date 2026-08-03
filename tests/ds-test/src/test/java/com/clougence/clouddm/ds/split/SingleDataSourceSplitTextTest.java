package com.clougence.clouddm.ds.split;

public abstract class SingleDataSourceSplitTextTest extends SplitTextTest {

    protected abstract String datasource();

    @Override
    protected final boolean accepts(Fixture fixture) {
        return datasource().equals(fixture.datasource());
    }
}
