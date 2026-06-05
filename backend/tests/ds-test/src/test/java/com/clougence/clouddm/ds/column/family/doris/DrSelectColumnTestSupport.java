package com.clougence.clouddm.ds.column.family.doris;

import java.util.Map;

import com.clougence.clouddm.ds.doris.analysis.DrSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class DrSelectColumnTestSupport {

    protected DrSelectColumnAnalysisSpi   spi;
    protected final Map<UmiTypes, Object> levels = CollectionUtils.asMap(//
            UmiTypes.Schema, "schema1", UmiTypes.Catalog, "catalog1");

    protected ContextInfo createContextInfo() {
        return ContextInfo.builder().levelsParam(levels).build();
    }
}
