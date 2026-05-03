package com.clougence.clouddm.ds.column.family.maxcompute.supportschema;

import java.util.Map;

import com.clougence.clouddm.ds.maxcompute.analysis.McSelectColumnAnalysisSpi;
import com.clougence.clouddm.ds.maxcompute.dsconf.McConfig;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class McSelectColumnTestSupport {

    protected McSelectColumnAnalysisSpi   spi;
    protected final Map<UmiTypes, Object> levels = CollectionUtils.asMap(//
            UmiTypes.Schema, "schema1", UmiTypes.Catalog, "catalog1");

    protected ContextInfo contextInfo() {
        McConfig dataSourceConfig = new McConfig();
        dataSourceConfig.setSchemaStyle(true);
        return ContextInfo.builder().levelsParam(levels).dataSourceConfig(dataSourceConfig).build();
    }
}
