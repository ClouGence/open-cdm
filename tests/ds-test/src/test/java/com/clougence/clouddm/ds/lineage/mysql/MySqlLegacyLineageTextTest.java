package com.clougence.clouddm.ds.lineage.mysql;

import java.util.List;

import com.clougence.clouddm.ds.TextCaseSupport;
import com.clougence.clouddm.ds.lineage.SingleDataSourceLineageTextTest;
import com.clougence.clouddm.sdk.sql.analysis.lineage.SourceName;

public final class MySqlLegacyLineageTextTest extends SingleDataSourceLineageTextTest {

    private static final String RESOURCE_DIRECTORY = "lineage/mysql";
    private static final String RESOURCE_PREFIX    = RESOURCE_DIRECTORY + "/";

    @Override
    protected String datasource() {
        return "mysql";
    }

    @Override
    protected List<String> fixtureResources() {
        return TextCaseSupport.resourceFiles(RESOURCE_DIRECTORY, path -> !path.substring(RESOURCE_PREFIX.length()).contains("/"));
    }

    @Override
    protected String sourcePath(SourceName sourceName) {
        return sourceName.toLocatedDsResPath();
    }
}
