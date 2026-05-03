package com.clougence.clouddm.dsfamily.postgres.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.resource.RdbResAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCatalogDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain;
import com.clougence.clouddm.sdk.analysis.secrules.ResAnalysisSpi;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.RdbResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.ObjectUtils;

public class PgResAnalysisSpi extends RdbResAnalysisSpi implements ResAnalysisSpi {

    protected PgSecDomainResolveSpi resolveSpi;

    public PgResAnalysisSpi(MetaService metaService){
        super();
        this.resolveSpi = new PgSecDomainResolveSpi(metaService);
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
        } else if (domain instanceof RdbColumnDomain) {
            List<RdbResObject> list = super.convert(dsType, domain, ctx);
            if (domain.getSqlType() == SecQueryType.COMMENT_COLUMN) {
                RdbResObject resObj = new RdbResObject();
                resObj.setType(domain.getSqlTarget());
                resObj.setCatalog(((RdbColumnDomain) domain).getCatalog());
                resObj.setSchema(((RdbColumnDomain) domain).getSchema());
                resObj.setTable(((RdbColumnDomain) domain).getTable());
                resObj.setType(TargetType.Table);
                list.add(0, resObj);
            }

            for (RdbResObject res : list) {
                this.fillCtx(ctx, res);
            }
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
        } else {
            return super.convert(dsType, domain, ctx);
        }
    }

    @Override
    protected void fillCtx(Map<String, Object> ctx, RdbResObject resObj) {
        if (resObj == null || skipFillCtxTypes.contains(resObj.getType())) {
            return;
        }

        super.fillCtx(ctx, resObj);
    }
}
