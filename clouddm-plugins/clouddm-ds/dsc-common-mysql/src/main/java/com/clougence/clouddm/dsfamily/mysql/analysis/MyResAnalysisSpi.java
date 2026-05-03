package com.clougence.clouddm.dsfamily.mysql.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.resource.RdbResAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCatalogDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbRoleDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSchemaDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.*;
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
import com.clougence.utils.ObjectUtils;
import com.clougence.utils.StringUtils;

public class MyResAnalysisSpi extends RdbResAnalysisSpi implements ResAnalysisSpi {

    protected MySecDomainResolveSpi resolveSpi;

    public MyResAnalysisSpi(MetaService metaService){
        super();
        this.resolveSpi = new MySecDomainResolveSpi(metaService);
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
            list.get(0).setType(TargetType.Schema);
            return list;
        } else if (domain instanceof RdbSchemaDomain) {
            List<RdbResObject> list = super.convert(dsType, domain, ctx);
            ObjectUtils.assertTrue(list.size() == 1, "SchemaDomain multiple results.");
            list.get(0).setType(TargetType.Schema);
            return list;
        } else if (domain instanceof MyShowDomain) {
            return this.convertShowDomain((MyShowDomain) domain, ctx);
        } else if (domain instanceof MyFlushDomain) {
            return this.convertFlushDomain((MyFlushDomain) domain, ctx);
        } else if (domain instanceof MyUserDomain) {
            List<RdbResObject> list = super.convert(dsType, domain, ctx);
            ObjectUtils.assertTrue(list.size() == 1, "MyUserDomain multiple results.");
            list.get(0).setCatalog(null);
            list.get(0).setSchema(null);
            return list;
        } else if (domain instanceof RdbRoleDomain) {
            List<RdbResObject> list = super.convert(dsType, domain, ctx);
            ObjectUtils.assertTrue(list.size() == 1, "RdbRoleDomain multiple results.");
            list.get(0).setCatalog(null);
            list.get(0).setSchema(null);
            return list;
        } else if (domain instanceof MyRevokeDomain) {
            List<RdbResObject> list = super.convert(dsType, domain, ctx);
            ObjectUtils.assertTrue(list.size() == 1, "MyRevokeDomain multiple results.");
            list.get(0).setCatalog(null);
            list.get(0).setSchema(null);
            return list;
        } else if (domain instanceof MyGrantDomain) {
            List<RdbResObject> list = super.convert(dsType, domain, ctx);
            ObjectUtils.assertTrue(list.size() == 1, "MyGrantDomain multiple results.");
            list.get(0).setCatalog(null);
            list.get(0).setSchema(null);
            return list;
        } else if (domain instanceof RdbSelectDomain) {
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
        } else if (domain instanceof MyTableDomain) {
            MyTableDomain myTableDomain = (MyTableDomain) domain;

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
        } else if (domain instanceof MyUpdateDomain) {
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
        } else if(domain instanceof MyReplaceDomain){
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

    private List<RdbResObject> convertFlushDomain(MyFlushDomain domain, Map<String, Object> ctx) {
        RdbResObject resObj = new RdbResObject();
        resObj.setType(TargetType.Unknown);

        return Collections.singletonList(resObj);
    }

    protected List<RdbResObject> convertShowDomain(MyShowDomain domain, Map<String, Object> ctx) {
        RdbResObject resObj = new RdbResObject();
        resObj.setType(domain.getSqlTarget());
        resObj.setCatalog(domain.getCatalog());
        resObj.setSchema(domain.getSchema());

        switch (domain.getShowType()) {
            case CREATE_DATABASE: {
                resObj.setType(TargetType.Schema);
                break;
            }
            case CREATE_EVENT: {
                resObj.setType(TargetType.Event);
                resObj.setName(domain.getEvent());
                break;
            }
            case CREATE_FUNCTION: {
                resObj.setType(TargetType.Function);
                resObj.setName(domain.getFunc());
                break;
            }
            case CREATE_PROCEDURE: {
                resObj.setType(TargetType.Procedure);
                resObj.setName(domain.getProc());
                break;
            }
            case CREATE_TABLE: {
                resObj.setType(TargetType.Table);
                resObj.setTable(domain.getTable());
                //
                break;
            }
            case CREATE_TRIGGER: {
                resObj.setType(TargetType.Trigger);
                resObj.setName(domain.getTrigger());
                break;
            }
            case CREATE_VIEW: {
                resObj.setType(TargetType.View);
                resObj.setTable(domain.getView());
                break;
            }
            case COLUMNS: {
                // `show columns from xxx` or `desc xxx`
                resObj.setType(TargetType.Table);
                resObj.setTable(domain.getTable());
                break;
            }
            case TABLES: {
                resObj.setType(TargetType.Schema);
                break;
            }
            case TRIGGERS: {
                if (StringUtils.isNotBlank(domain.getTrigger())) {
                    resObj.setType(TargetType.Trigger);
                    resObj.setName(domain.getTrigger());
                    break;
                } else {
                    resObj.setType(TargetType.Schema);
                    break;
                }
            }
            case EVENTS: {
                if (StringUtils.isNotBlank(domain.getEvent())) {
                    resObj.setType(TargetType.Event);
                    resObj.setName(domain.getEvent());
                    break;
                } else {
                    resObj.setType(TargetType.Schema);
                    break;
                }
            }
            case INDEX: {
                resObj.setType(TargetType.Table);
                resObj.setTable(domain.getTable());
                break;
            }
            case FUNCTION_CODE: {
                resObj.setType(TargetType.Function);
                resObj.setName(domain.getFunc());
                break;
            }
            case PROCEDURE_CODE: {
                resObj.setType(TargetType.Procedure);
                resObj.setName(domain.getProc());
                break;
            }
            case TABLE_STATUS:
            case FUNCTION_STATUS:
            case PROCEDURE_STATUS:
            case ENGINES:
            case ENGINE: {
                resObj.setType(TargetType.Schema);
                break;
            }
            case OPEN_TABLES: {
                resObj.setType(TargetType.Schema);
                break;
            }
            default:
                return Collections.emptyList();
        }

        this.fillCtx(ctx, resObj);
        return Collections.singletonList(resObj);
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
