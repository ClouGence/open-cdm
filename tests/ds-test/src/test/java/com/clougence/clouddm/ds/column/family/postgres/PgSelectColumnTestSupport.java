package com.clougence.clouddm.ds.column.family.postgres;

import java.util.Map;

import com.clougence.clouddm.dsfamily.postgres.analysis.PgSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class PgSelectColumnTestSupport {

    protected PgSelectColumnAnalysisSpi   spi;
    protected final Map<UmiTypes, Object> levels = CollectionUtils.asMap(//
            UmiTypes.Schema, "schema1", UmiTypes.Catalog, "catalog1");

    protected ContextInfo contextInfo() {
        return ContextInfo.builder().levelsParam(levels).build();
    }
}
