package com.clougence.clouddm.ds.doris.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.doris.analysis.secrules.DrShowDomain;
import com.clougence.clouddm.ds.doris.analysis.secrules.DrUserDomain;
import com.clougence.clouddm.dsfamily.analysis.resource.RdbResAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCatalogDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain;
import com.clougence.clouddm.sdk.analysis.secrules.ResAnalysisSpi;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.RdbResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.ObjectUtils;
import com.clougence.utils.StringUtils;

public class DrResAnalysisSpi extends RdbResAnalysisSpi implements ResAnalysisSpi {

    protected DrSecDomainResolveSpi resolveSpi;

    public DrResAnalysisSpi(MetaService metaService){
        super();
        this.resolveSpi = new DrSecDomainResolveSpi(metaService);
    }

    @Override
    protected List<RuleDomain> resolveDomain(DataSourceType dsType, CodeInfo codeInfo, ContextInfo contextInfo) {
        return this.resolveSpi.resolveDomain(dsType, codeInfo, contextInfo);
    }

    @Override
    protected List<RdbResObject> convert(DataSourceType dsType, RuleDomain domain, Map<String, Object> ctx) {
        if (domain instanceof RdbCatalogDomain) {
            List<RdbResObject> list = super.convert(dsType, domain, ctx);
            ObjectUtils.assertTrue(list.size() == 1, "CatalogDomain multiple results.");
            list.get(0).setSchema(null);
            return list;
        } else if (domain instanceof RdbSelectDomain) {
            if (((RdbSelectDomain) domain).isVirtual()) {
                return Collections.emptyList();
            }

            List<RdbResObject> resObjs = new ArrayList<>();
            for (Map<TargetType, String> map : domain.resolveResource()) {
                RdbResObject resObj = new RdbResObject();
                resObj.setType(TargetType.Table);
                resObj.setCatalog(map.get(TargetType.Catalog));
                resObj.setSchema(map.get(TargetType.Schema));
                resObj.setTable(map.get(TargetType.Table));
                resObjs.add(resObj);
            }
            for (RdbResObject resObj : resObjs) {
                fillCtx(ctx, resObj);
            }
            return resObjs;
        } else if (domain instanceof DrShowDomain) {
            RdbResObject resObj = new RdbResObject();
            resObj.setType(TargetType.Unknown);
            return Collections.singletonList(resObj);
        } else if (domain instanceof DrUserDomain) {
            List<RdbResObject> list = super.convert(dsType, domain, ctx);
            ObjectUtils.assertTrue(list.size() == 1, "MyUserDomain multiple results.");
            list.get(0).setCatalog(null);
            list.get(0).setSchema(null);
            return list;
        } else {
            return super.convert(dsType, domain, ctx);
        }
    }

    @Override
    protected void fillCtx(Map<String, Object> ctx, RdbResObject resObj) {
        // user and role related to instance
        if (resObj == null || skipFillCtxTypes.contains(resObj.getType())) {
            return;
        }

        if (StringUtils.isBlank(resObj.getCatalog())) {
            resObj.setCatalog((String) ctx.get(SessionSpi.PARAMS_DEFAULT_DB));
        }

        if (StringUtils.isBlank(resObj.getSchema())) {
            resObj.setSchema((String) ctx.get(SessionSpi.PARAMS_DEFAULT_SCHEMA));
        }
    }
}
