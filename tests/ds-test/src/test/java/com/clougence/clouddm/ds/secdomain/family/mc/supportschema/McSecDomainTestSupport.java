package com.clougence.clouddm.ds.secdomain.family.mc.supportschema;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.clougence.clouddm.ds.maxcompute.analysis.McResAnalysisSpi;
import com.clougence.clouddm.ds.maxcompute.analysis.McSecDomainResolveSpi;
import com.clougence.clouddm.ds.maxcompute.analysis.McSplitAnalysisSpi;
import com.clougence.clouddm.ds.maxcompute.dsconf.McConfig;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.CollectionUtils;

public abstract class McSecDomainTestSupport {

    protected McResAnalysisSpi          analysisSpi      = null;
    protected McSecDomainResolveSpi     resolveSpi       = null;
    protected DataSourceType            dataSourceType   = null;
    protected McSplitAnalysisSpi        splitAnalysisSpi = null;
    protected final Map<String, Object> ctx              = CollectionUtils.asMap(//
            SessionSpi.PARAMS_DEFAULT_DB, "test_db",//
            SessionSpi.PARAMS_DEFAULT_SCHEMA, "test_schema");

    protected static CodeInfo codeInfo(String sql) {
        return CodeInfo.builder().query(sql).baseLine(1).baseColumn(0).build();
    }

    protected static ContextInfo contextInfo() {
        McConfig dataSourceConfig = new McConfig();
        dataSourceConfig.setSchemaStyle(true);
        return ContextInfo.builder().deepParser(false).dataSourceConfig(dataSourceConfig).build();
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = new HashSet<>();
        return t -> seen.add(keyExtractor.apply(t));
    }

    protected List<ResObject> parserRes(String sql) {
        return this.analysisSpi.analysisResource(dataSourceType, codeInfo(sql), contextInfo(), ctx)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .filter(distinctByKey((resObject) -> {
                return resObject.toDsResPath().getResPath();
            }))
            .collect(Collectors.toList());
    }
}
