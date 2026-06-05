package com.clougence.clouddm.ds.column.family.starrocks;

import java.util.HashMap;
import java.util.Map;

import com.clougence.clouddm.ds.starrocks.analysis.SrSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class SrSelectColumnTestSupport {

    protected SrSelectColumnAnalysisSpi   spi;
    protected final Map<UmiTypes, Object> levels = CollectionUtils.asMap(//
            UmiTypes.Schema, "schema1", UmiTypes.Catalog, "catalog1");

    protected ContextInfo contextInfo() {
        HashMap<UmiTypes, Object> levelsParam = new HashMap<>();
        levelsParam.put(UmiTypes.Schema, "schema1");
        levelsParam.put(UmiTypes.Catalog, "catalog1");
        return ContextInfo.builder().levelsParam(levelsParam).build();
    }
}
