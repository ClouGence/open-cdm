package com.clougence.clouddm.ds.clickhouse.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChTableDomain;
import com.clougence.clouddm.dsfamily.analysis.resource.RdbResAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain;
import com.clougence.clouddm.sdk.analysis.secrules.ResAnalysisSpi;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.RdbResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.StringUtils;

public class ChResAnalysisSpi extends RdbResAnalysisSpi implements ResAnalysisSpi {

    protected ChSecDomainResolveSpi resolveSpi;

    public ChResAnalysisSpi(MetaService metaService){
        super();
        this.resolveSpi = new ChSecDomainResolveSpi(metaService);
    }

    @Override
    protected List<RuleDomain> resolveDomain(DataSourceType dsType, CodeInfo codeInfo, ContextInfo contextInfo) {
        return this.resolveSpi.resolveDomain(dsType, codeInfo, contextInfo);
    }

    @Override
    protected List<RdbResObject> convert(DataSourceType dsType, RuleDomain domain, Map<String, Object> ctx) {
        if (domain instanceof RdbSelectDomain) {
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
        } else if (domain instanceof ChTableDomain) {
            ChTableDomain myTableDomain = (ChTableDomain) domain;

            if (myTableDomain.getSqlType() == SecQueryType.ALTER_TABLE_RENAME || myTableDomain.getSqlType() == SecQueryType.RENAME_TABLE) {
                List<RdbResObject> resObjs = new ArrayList<>();
                RdbResObject resObj1 = new RdbResObject();
                resObj1.setType(TargetType.Table);
                resObj1.setSchema(myTableDomain.getSchema());
                resObj1.setTable(myTableDomain.getTable());
                resObjs.add(resObj1);

                RdbResObject resObj2 = new RdbResObject();
                resObj2.setType(TargetType.Table);
                resObj2.setSchema(myTableDomain.getNewSchemaName());
                resObj2.setTable(myTableDomain.getNewName());
                resObjs.add(resObj2);

                for (RdbResObject resObj : resObjs) {
                    fillCtx(ctx, resObj);
                }
                return resObjs;
            } else {
                return super.convert(dsType, domain, ctx);
            }
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

        if (StringUtils.isBlank(resObj.getSchema())) {
            resObj.setSchema((String) ctx.get(SessionSpi.PARAMS_DEFAULT_SCHEMA));
        }
    }
}
