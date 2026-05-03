package com.clougence.clouddm.console.web.component.detectrule.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.console.web.dal.enumeration.MetaInformationType;
import com.clougence.clouddm.console.web.dal.mapper.DmMetaInformationCacheMapper;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

@Component
public class CreateTableHandler implements QueryTypeHandler {

    @Resource
    private DmMetaInformationCacheMapper cacheMapper;

    @Override
    public void handleAfterSqlOperation(RuleDomain ruleDomain, Long dsId, Map<String, String> map, Date execTime) {
        RdbTableDomain tableDomain = (RdbTableDomain) ruleDomain;
        StringBuilder path = new StringBuilder("/");
        if (tableDomain.getCatalog() != null) {
            path.append(tableDomain.getCatalog()).append("/");
        } else if (map.get(SessionSpi.PARAMS_DEFAULT_DB) != null) {
            path.append(map.get(SessionSpi.PARAMS_DEFAULT_DB)).append("/");
        }

        if (tableDomain.getSchema() != null) {
            path.append(tableDomain.getSchema()).append("/");
        } else if (map.get(SessionSpi.PARAMS_DEFAULT_SCHEMA) != null) {
            path.append(map.get(SessionSpi.PARAMS_DEFAULT_SCHEMA)).append("/");
        }
        cacheMapper.deleteByPath(dsId, path.toString(), MetaInformationType.TableList, execTime);
    }

    @Override
    public List<SecQueryType> canHandleType() {
        return Arrays.asList(SecQueryType.CREATE_TABLE, SecQueryType.CREATE_TABLE_LIKE, SecQueryType.CREATE_TABLE_SELECT);
    }
}
