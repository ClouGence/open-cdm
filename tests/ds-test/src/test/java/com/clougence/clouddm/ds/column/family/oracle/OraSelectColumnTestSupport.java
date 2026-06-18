package com.clougence.clouddm.ds.column.family.oracle;

import java.util.Map;

import com.clougence.clouddm.sdk.analysis.column.SelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class OraSelectColumnTestSupport {

    protected SelectColumnAnalysisSpi     spi;
    protected final Map<UmiTypes, Object> levels = CollectionUtils.asMap(//
            UmiTypes.Schema, "schema1");

    protected ContextInfo contextInfo() {
        return ContextInfo.builder().levelsParam(levels).build();
    }
}
