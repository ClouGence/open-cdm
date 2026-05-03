package com.clougence.clouddm.ds.secdomain.family.db2;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.clougence.clouddm.dsfamily.db2.analysis.Db2ResAnalysisSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.Db2SecDomainOptionKeys;
import com.clougence.clouddm.dsfamily.db2.analysis.Db2SecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.Db2SplitAnalysisSpi;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.CollectionUtils;

public abstract class Db2SecDomainTestSupport implements Db2SecDomainOptionKeys {

    protected Db2ResAnalysisSpi         analysisSpi;
    protected Db2SecDomainResolveSpi    resolveSpi;
    protected Db2SplitAnalysisSpi       splitAnalysisSpi;
    protected DataSourceType            dataSourceType;
    protected final Map<String, Object> ctx = CollectionUtils.asMap(//
            SessionSpi.PARAMS_DEFAULT_DB, "test_db",//
            SessionSpi.PARAMS_DEFAULT_SCHEMA, "test_schema");

    protected static CodeInfo codeInfo(String sql) {
        return CodeInfo.builder().query(sql).baseLine(1).baseColumn(0).build();
    }

    protected static ContextInfo contextInfo() {
        return ContextInfo.builder().deepParser(false).build();
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
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
