package com.clougence.clouddm.dsfamily.analysis.resource;

import java.util.*;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.sdk.analysis.secrules.ResAnalysisSpi;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.RdbResObject;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.StringUtils;

public abstract class RdbResAnalysisSpi implements ResAnalysisSpi {

    @Override
    public Map<RuleDomain, List<ResObject>> analysisResource(DataSourceType dsType, CodeInfo codeInfo, ContextInfo contextInfo, Map<String, Object> ctx) {
        List<RuleDomain> domains = this.resolveDomain(dsType, codeInfo, contextInfo);
        Map<RuleDomain, List<ResObject>> result = new LinkedHashMap<>();
        for (RuleDomain domain : domains) {
            List<RdbResObject> convert = convert(dsType, domain, ctx);
            result.put(domain, new ArrayList<>(convert));
        }
        return result;
    }

    private List<ResObject> distinct(List<ResObject> list) {
        List<ResObject> result = new ArrayList<>();

        Map<TargetType, Set<String>> tmp = new HashMap<>();
        for (ResObject resObj : list) {
            Set<String> keys = tmp.computeIfAbsent(resObj.getType(), t -> new HashSet<>());

            String resPath = resObj.toDsResPath().getResPath();
            if (!keys.contains(resPath)) {
                keys.add(resPath);
                result.add(resObj);
            }
        }

        return result;
    }

    protected abstract List<RuleDomain> resolveDomain(DataSourceType dsType, CodeInfo codeInfo, ContextInfo contextInfo);

    protected RdbResObject newObject(RuleDomain domain) {
        RdbResObject resObj = new RdbResObject();
        resObj.setType(domain.getSqlTarget());
        return resObj;
    }

    protected List<RdbResObject> convert(DataSourceType dsType, RuleDomain domain, Map<String, Object> ctx) {
        List<RdbResObject> resObjs = new ArrayList<>();

        // DDL
        if (domain instanceof RdbCatalogDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setCatalog(((RdbCatalogDomain) domain).getCatalog());
            resObjs.add(resObj);
        } else if (domain instanceof RdbSchemaDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setCatalog(((RdbSchemaDomain) domain).getCatalog());
            resObj.setSchema(((RdbSchemaDomain) domain).getSchema());
            resObjs.add(resObj);
        } else if (domain instanceof RdbTableDomain) {
            if (((RdbTableDomain) domain).isVirtual()) {
                return Collections.emptyList();
            }
            switch (domain.getSqlType()) {
                case ALTER_TABLE_RENAME:
                case RENAME_TABLE: {
                    RdbResObject resObj1 = newObject(domain);
                    resObj1.setCatalog(((RdbTableDomain) domain).getCatalog());
                    resObj1.setSchema(((RdbTableDomain) domain).getSchema());
                    resObj1.setTable(((RdbTableDomain) domain).getTable());
                    resObjs.add(resObj1);

                    RdbResObject resObj2 = newObject(domain);
                    resObj2.setCatalog(((RdbTableDomain) domain).getCatalog());
                    resObj2.setSchema(((RdbTableDomain) domain).getSchema());
                    resObj2.setTable(((RdbTableDomain) domain).getNewName());
                    resObjs.add(resObj2);
                    break;
                }
                case SELECT:
                case TRUNCATE: {
                    RdbResObject resObj1 = newObject(domain);
                    resObj1.setCatalog(((RdbTableDomain) domain).getCatalog());
                    resObj1.setSchema(((RdbTableDomain) domain).getSchema());
                    resObj1.setTable(((RdbTableDomain) domain).getTable());
                    resObj1.setType(TargetType.Table);
                    resObjs.add(resObj1);
                    break;
                }
                default: {
                    RdbResObject resObj1 = newObject(domain);
                    resObj1.setCatalog(((RdbTableDomain) domain).getCatalog());
                    resObj1.setSchema(((RdbTableDomain) domain).getSchema());
                    resObj1.setTable(((RdbTableDomain) domain).getTable());
                    resObjs.add(resObj1);

                    if (StringUtils.isNotBlank(((RdbTableDomain) domain).getSourceTable())) {
                        RdbResObject resObj2 = newObject(domain);
                        resObj2.setCatalog(((RdbTableDomain) domain).getCatalog());
                        resObj2.setSchema(((RdbTableDomain) domain).getSourceSchema());
                        resObj2.setTable(((RdbTableDomain) domain).getSourceTable());
                        resObjs.add(resObj2);
                    }
                    break;
                }
            }
        } else if (domain instanceof RdbViewDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setCatalog(((RdbViewDomain) domain).getCatalog());
            resObj.setSchema(((RdbViewDomain) domain).getSchema());
            resObj.setTable(((RdbViewDomain) domain).getView());
            resObjs.add(resObj);
        } else if (domain instanceof RdbColumnDomain) {
            switch (domain.getSqlType()) {
                case ALTER_TABLE_ADD_COLUMN:
                case ALTER_TABLE_ALTER_COLUMN:
                case ALTER_TABLE_DROP_COLUMN: {
                    RdbResObject resObj = newObject(domain);
                    resObj.setCatalog(((RdbColumnDomain) domain).getCatalog());
                    resObj.setSchema(((RdbColumnDomain) domain).getSchema());
                    resObj.setTable(((RdbColumnDomain) domain).getTable());
                    resObj.setType(TargetType.Table);
                    resObjs.add(resObj);
                    break;
                }
            }
            RdbResObject resObj = newObject(domain);
            resObj.setCatalog(((RdbColumnDomain) domain).getCatalog());
            resObj.setSchema(((RdbColumnDomain) domain).getSchema());
            resObj.setTable(((RdbColumnDomain) domain).getTable());
            resObj.setName(((RdbColumnDomain) domain).getColumn());
            resObjs.add(resObj);
        } else if (domain instanceof RdbCallDomain) {
            RdbResObject resObj = newObject(domain);
            if (((RdbCallDomain) domain).isFunc()) {
                resObj.setType(TargetType.Function);
            } else {
                resObj.setType(TargetType.Procedure);
            }
            resObj.setCatalog(((RdbCallDomain) domain).getCatalog());
            resObj.setSchema(((RdbCallDomain) domain).getSchema());
            resObj.setName(((RdbCallDomain) domain).getCallName());
            resObjs.add(resObj);
        } else if (domain instanceof RdbIndexDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setType(TargetType.Table);
            resObj.setCatalog(((RdbIndexDomain) domain).getTableCatalog());
            resObj.setSchema(((RdbIndexDomain) domain).getTableSchema());
            resObj.setTable(((RdbIndexDomain) domain).getTableName());
            resObjs.add(resObj);
        } else if (domain instanceof RdbConstraintDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setType(TargetType.Table);
            resObj.setCatalog(((RdbConstraintDomain) domain).getTableCatalog());
            resObj.setSchema(((RdbConstraintDomain) domain).getTableSchema());
            resObj.setTable(((RdbConstraintDomain) domain).getTableName());
            resObjs.add(resObj);
        }

        // DML
        else if (domain instanceof RdbInsertDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setType(TargetType.Table);
            resObj.setCatalog(((RdbInsertDomain) domain).getCatalog());
            resObj.setSchema(((RdbInsertDomain) domain).getSchema());
            resObj.setTable(((RdbInsertDomain) domain).getTable());
            resObjs.add(resObj);
        } else if (domain instanceof RdbUpdateDomain) {
            if (StringUtils.isBlank(((RdbUpdateDomain) domain).getTable())) {
                return Collections.emptyList();
            }

            RdbResObject resObj = newObject(domain);
            resObj.setType(TargetType.Table);
            resObj.setCatalog(((RdbUpdateDomain) domain).getCatalog());
            resObj.setSchema(((RdbUpdateDomain) domain).getSchema());
            resObj.setTable(((RdbUpdateDomain) domain).getTable());
            resObjs.add(resObj);
        } else if (domain instanceof RdbDeleteDomain) {
            if (StringUtils.isBlank(((RdbDeleteDomain) domain).getTable())) {
                return Collections.emptyList();
            }
            RdbResObject resObj = newObject(domain);
            resObj.setType(TargetType.Table);
            resObj.setCatalog(((RdbDeleteDomain) domain).getCatalog());
            resObj.setSchema(((RdbDeleteDomain) domain).getSchema());
            resObj.setTable(((RdbDeleteDomain) domain).getTable());
            resObjs.add(resObj);
        } else if (domain instanceof RdbSelectDomain) {
            if (((RdbSelectDomain) domain).isVirtual()) {
                return Collections.emptyList();
            }
            if (StringUtils.isNotBlank(((RdbSelectDomain) domain).getTable())) {
                RdbResObject resObj = newObject(domain);
                resObj.setType(TargetType.Table);
                resObj.setCatalog(((RdbSelectDomain) domain).getCatalog());
                resObj.setSchema(((RdbSelectDomain) domain).getSchema());
                resObj.setTable(((RdbSelectDomain) domain).getTable());
                resObjs.add(resObj);
            }
        } else if (domain instanceof RdbUserDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setType(TargetType.UserOrRole);
            resObj.setName(((RdbUserDomain) domain).getUser());
            resObjs.add(resObj);
        } else if (domain instanceof RdbRoleDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setType(TargetType.UserOrRole);
            resObj.setName(((RdbRoleDomain) domain).getRole());
            resObjs.add(resObj);
        } else if (domain instanceof RdbGrantDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setType(TargetType.UserOrRole);
            resObj.setName(((RdbGrantDomain) domain).getName());
            resObjs.add(resObj);
        } else if (domain instanceof RdbRevokeDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setType(TargetType.UserOrRole);
            resObj.setName(((RdbRevokeDomain) domain).getName());
            resObjs.add(resObj);
        } else if (domain instanceof RdbTriggerDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setType(TargetType.Trigger);
            resObj.setCatalog(((RdbTriggerDomain) domain).getCatalog());
            resObj.setSchema(((RdbTriggerDomain) domain).getSchema());
            resObj.setTable(((RdbTriggerDomain) domain).getTable());
            resObjs.add(resObj);
        } else if (domain instanceof RdbFunctionDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setType(TargetType.Function);
            resObj.setCatalog(((RdbFunctionDomain) domain).getCatalog());
            resObj.setSchema(((RdbFunctionDomain) domain).getSchema());
            resObj.setName(((RdbFunctionDomain) domain).getName());
            resObjs.add(resObj);
        } else if (domain instanceof RdbSequenceDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setType(TargetType.Sequence);
            resObj.setCatalog(((RdbSequenceDomain) domain).getCatalog());
            resObj.setSchema(((RdbSequenceDomain) domain).getSchema());
            resObj.setName(((RdbSequenceDomain) domain).getName());
            resObjs.add(resObj);
        } else if (domain instanceof RdbSynonymDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setType(TargetType.Synonym);
            resObj.setCatalog(((RdbSynonymDomain) domain).getCatalog());
            resObj.setSchema(((RdbSynonymDomain) domain).getSchema());
            resObj.setName(((RdbSynonymDomain) domain).getName());
            resObjs.add(resObj);
        } else if (domain instanceof RdbObjectDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setType(TargetType.Object);
            resObj.setCatalog(((RdbObjectDomain) domain).getCatalog());
            resObj.setSchema(((RdbObjectDomain) domain).getSchema());
            resObj.setName(((RdbObjectDomain) domain).getName());
            resObjs.add(resObj);
        } else if (domain instanceof RdbProcedureDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setType(TargetType.Procedure);
            resObj.setCatalog(((RdbProcedureDomain) domain).getCatalog());
            resObj.setSchema(((RdbProcedureDomain) domain).getSchema());
            resObj.setName(((RdbProcedureDomain) domain).getName());
            resObjs.add(resObj);
        } else if (domain instanceof RdbConfigDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setType(TargetType.ConfigKey);
            // for datasource
            //            resObj.setName(((RdbConfigDomain) domain).getConfigKey());
            resObjs.add(resObj);
        } else if (domain instanceof RdbResourceDomain) {
            RdbResObject resObj = new RdbResObject();
            RdbResourceDomain resourceDomain = (RdbResourceDomain) domain;
            resObj.setType(resourceDomain.getTarget());
            resObj.setCatalog(resourceDomain.getCatalog());
            resObj.setSchema(resourceDomain.getSchema());
            resObj.setName(resourceDomain.getName());
            resObjs.add(resObj);
            if (!resourceDomain.isNeedSupply()) {
                return resObjs;
            }
        } else if(domain instanceof RdbEventDomain){
            RdbResObject resObj = newObject(domain);
            resObj.setType(TargetType.Event);
            resObj.setCatalog(((RdbEventDomain) domain).getCatalog());
            resObj.setSchema(((RdbEventDomain) domain).getSchema());
            resObj.setName(((RdbEventDomain) domain).getName());
            resObjs.add(resObj);
        }

        for (RdbResObject res : resObjs) {
            this.fillCtx(ctx, res);
        }
        return resObjs;
    }

    protected Set<TargetType> skipFillCtxTypes = new HashSet<>(Arrays.asList(TargetType.UserOrRole, TargetType.ConfigKey));

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
