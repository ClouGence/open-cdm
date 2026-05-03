package com.clougence.clouddm.dsfamily.db2.analysis;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.resource.RdbResAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCatalogDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.sdk.analysis.secrules.ResAnalysisSpi;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.RdbResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.ObjectUtils;
import com.clougence.utils.StringUtils;

public class Db2ResAnalysisSpi extends RdbResAnalysisSpi implements ResAnalysisSpi {

    protected Db2SecDomainResolveSpi resolveSpi;

    public Db2ResAnalysisSpi(){
        this.resolveSpi = new Db2SecDomainResolveSpi();
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
        } else {
            return super.convert(dsType, domain, ctx);
        }
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
