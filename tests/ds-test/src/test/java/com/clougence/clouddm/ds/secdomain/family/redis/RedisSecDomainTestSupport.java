package com.clougence.clouddm.ds.secdomain.family.redis;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.clougence.clouddm.ds.redis.analysis.RedisResAnalysisSpi;
import com.clougence.clouddm.ds.redis.analysis.RedisSecDomainResolveSpi;
import com.clougence.clouddm.ds.redis.analysis.RedisSplitAnalysisSpi;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.CollectionUtils;

public abstract class RedisSecDomainTestSupport {

    protected RedisResAnalysisSpi       analysisSpi      = null;
    protected RedisSecDomainResolveSpi  resolveSpi       = null;
    protected DataSourceType            dataSourceType   = null;
    protected RedisSplitAnalysisSpi     splitAnalysisSpi = null;
    protected final Map<String, Object> ctx              = CollectionUtils.asMap(//
            SessionSpi.PARAMS_DEFAULT_DB, "0",//
            SessionSpi.PARAMS_DEFAULT_SCHEMA, "1");

    protected static CodeInfo codeInfo(String sql) {
        return CodeInfo.builder().query(sql).baseLine(1).baseColumn(0).build();
    }

    protected static ContextInfo contextInfo() {
        return ContextInfo.builder().deepParser(false).build();
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
