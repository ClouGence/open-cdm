package com.clougence.clouddm.console.web.component.detectrule.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.console.web.dal.enumeration.MetaInformationType;
import com.clougence.clouddm.console.web.dal.mapper.DmMetaInformationCacheMapper;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSchemaDomain;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

@Component
public class SchemaHandler implements QueryTypeHandler {

    @Resource
    private DmMetaInformationCacheMapper cacheMapper;

    @Override
    public void handleAfterSqlOperation(RuleDomain ruleDomain, Long dsId, Map<String, String> map, Date execTime) {
        RdbSchemaDomain domain = (RdbSchemaDomain) ruleDomain;
        StringBuilder path = new StringBuilder("/");
        if (domain.getCatalog() != null) {
            path.append(domain.getCatalog()).append("/");
        } else if (map.get(SessionSpi.PARAMS_DEFAULT_DB) != null) {
            path.append(map.get(SessionSpi.PARAMS_DEFAULT_DB)).append("/");
        }

        //        if (domain.getSchema() != null) {
        //            path.append(domain.getSchema()).append("/");
        //        } else if (map.get(SessionSpi.PARAMS_DEFAULT_SCHEMA) != null) {
        //            path.append(map.get(SessionSpi.PARAMS_DEFAULT_SCHEMA)).append("/");
        //        }
        cacheMapper.deleteByPath(dsId, path.toString(), MetaInformationType.SchemaList, execTime);
    }

    @Override
    public List<SecQueryType> canHandleType() {
        return Arrays.asList(SecQueryType.CREATE_SCHEMA, SecQueryType.ALTER_SCHEMA, SecQueryType.RENAME_SCHEMA);
    }
}
