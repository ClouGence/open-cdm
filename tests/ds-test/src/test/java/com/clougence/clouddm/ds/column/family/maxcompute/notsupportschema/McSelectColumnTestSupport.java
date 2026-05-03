package com.clougence.clouddm.ds.column.family.maxcompute.notsupportschema;

import java.util.Map;

import com.clougence.clouddm.ds.maxcompute.analysis.McSelectColumnAnalysisSpi;
import com.clougence.clouddm.ds.maxcompute.dsconf.McConfig;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class McSelectColumnTestSupport {

    protected McSelectColumnAnalysisSpi   spi;
    protected final Map<UmiTypes, Object> levels = CollectionUtils.asMap(//
            UmiTypes.Schema, "default", UmiTypes.Catalog, "catalog1");

    protected ContextInfo contextInfo() {
        return ContextInfo.builder().levelsParam(levels).dataSourceConfig(new McConfig()).build();
    }
}
