package com.clougence.clouddm.ds.column.family.mysql;

import java.util.Map;

import com.clougence.clouddm.dsfamily.mysql.analysis.MySelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class MySelectColumnTestSupport {

    protected MySelectColumnAnalysisSpi   spi;
    protected final Map<UmiTypes, Object> levels = CollectionUtils.asMap(//
            UmiTypes.Schema, "schema1");

    protected ContextInfo createContextInfo() {
        return ContextInfo.builder().levelsParam(levels).build();
    }
}
