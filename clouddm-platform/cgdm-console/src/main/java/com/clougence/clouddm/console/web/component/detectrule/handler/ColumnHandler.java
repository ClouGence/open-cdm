package com.clougence.clouddm.console.web.component.detectrule.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.console.web.dal.enumeration.MetaInformationType;
import com.clougence.clouddm.console.web.dal.mapper.DmMetaInformationCacheMapper;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

@Component
public class ColumnHandler implements QueryTypeHandler {

    @Resource
    private DmMetaInformationCacheMapper cacheMapper;

    @Override
    public void handleAfterSqlOperation(RuleDomain ruleDomain, Long dsId, Map<String, String> map, Date execTime) {
        RdbColumnDomain tableDomain = (RdbColumnDomain) ruleDomain;
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
        path.append(tableDomain.getTable());
        cacheMapper.deleteByPath(dsId, path.toString(), MetaInformationType.TableDetail, execTime);
        cacheMapper.deleteByPath(dsId, path.toString(), MetaInformationType.ETable, execTime);
    }

    @Override
    public List<SecQueryType> canHandleType() {
        return Arrays
            .asList(SecQueryType.ALTER_TABLE_ADD_COLUMN, SecQueryType.ALTER_TABLE_DROP_COLUMN, SecQueryType.ALTER_TABLE_ALTER_COLUMN, SecQueryType.ALTER_TABLE_RENAME_COLUMN, SecQueryType.COMMENT_COLUMN);
    }
}
