package com.clougence.clouddm.ds.column.family.clickhouse;

import java.util.Map;

import com.clougence.clouddm.ds.clickhouse.sql.column.ChSelectColumnAnalysisSpi;
import com.clougence.sql.mysql.column.MySelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class ChSelectColumnTestSupport {

    protected ChSelectColumnAnalysisSpi spi;
    protected final Map<UmiTypes, Object> levels = CollectionUtils.asMap(//
            UmiTypes.Schema, "schema1");

    protected ContextInfo createContextInfo() {
        return ContextInfo.builder().levelsParam(levels).build();
    }
}
