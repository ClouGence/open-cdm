package com.clougence.clouddm.ds.redis.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.redis.analysis.secrules.RedisKeyDomain;
import com.clougence.clouddm.dsfamily.analysis.resource.RdbResAnalysisSpi;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.RdbResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.StringUtils;

public class RedisResAnalysisSpi extends RdbResAnalysisSpi {

    protected RedisSecDomainResolveSpi resolveSpi;

    public RedisResAnalysisSpi(MetaService metaService){
        super();
        this.resolveSpi = new RedisSecDomainResolveSpi(metaService);
    }

    @Override
    protected List<RuleDomain> resolveDomain(DataSourceType dsType, CodeInfo codeInfo, ContextInfo contextInfo) {
        return this.resolveSpi.resolveDomain(dsType, codeInfo, contextInfo);
    }

    @Override
    protected RdbResObject newObject(RuleDomain domain) {
        RdbResObject resObj = new RdbResObject();

        if (domain instanceof RedisKeyDomain) {
            resObj.setType(TargetType.Key);
        } else {
            resObj.setType(TargetType.Schema);
        }

        return resObj;
    }

    @Override
    protected List<RdbResObject> convert(DataSourceType dsType, RuleDomain domain, Map<String, Object> ctx) {
        List<RdbResObject> resObjs = new ArrayList<>();

        if (domain instanceof RedisKeyDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setName(((RedisKeyDomain) domain).getKey());
            resObjs.add(resObj);
        } else {
            resObjs.add(newObject(domain));
        }

        for (RdbResObject res : resObjs) {
            this.fillCtx(ctx, res);
        }
        return resObjs;
    }

    @Override
    protected void fillCtx(Map<String, Object> ctx, RdbResObject resObj) {
        if (resObj == null || skipFillCtxTypes.contains(resObj.getType())) {
            return;
        }

        if (StringUtils.isBlank(resObj.getSchema())) {
            resObj.setSchema((String) ctx.get(SessionSpi.PARAMS_DEFAULT_SCHEMA));
        }
    }
}
