/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.component.execute.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.sqlaudit.SqlExecNotifyDTO;
import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.api.console.sqlaudit.Type;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.detectrule.handler.QueryTypeHandler;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.component.execute.AfterSqlExecuteService;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.secrules.SecDomainResolveSpi;
import com.clougence.schema.umi.struts.UmiTypes;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AfterSqlExecuteServiceImpl implements AfterSqlExecuteService {
    @Resource
    private DataSourceDal                             dsDal;
    @Resource
    private DmDsConfigService                         dmDsConfigService;

    private final Map<SecQueryType, QueryTypeHandler> handlerMap = new HashMap<>();

    public AfterSqlExecuteServiceImpl(List<QueryTypeHandler> handlers){
        for (QueryTypeHandler handler : handlers) {
            for (SecQueryType secQueryType : handler.canHandleType()) {
                if (handlerMap.containsKey(secQueryType)) {
                    throw new UnsupportedOperationException("QueryTypeHandler " + handler.canHandleType() + " already exists");
                }
                handlerMap.put(secQueryType, handler);
            }
        }
    }

    @Override
    public void handleAfterSqlSuccess(List<SqlExecNotifyDTO> audits) {

        for (SqlExecNotifyDTO dto : audits) {
            if (dto.getType() != Type.SQL_END && dto.getSqlStatus() != SqlStatus.SUCCESS) {
                continue;
            }
            try {
                this.handleAfterSqlSuccess(dto.getDsId(), dto.getLevels(), dto.getSql(), dto.getTime());
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }

    }

    public void handleAfterSqlSuccess(Long dsId, List<String> dsLevels, String sql, Date execTime) {
        DmDsDO rdpDataSourceDO = dsDal.dsMapper().queryDsIdentityById(dsId);
        DataSourceConfig dsConfig1 = dmDsConfigService.fetchDsConfigFromExists(dsId);
        SqlEngineSpi sqlEngine = PluginManager.findParserSpi(rdpDataSourceDO.getDataSourceType(), dsConfig1.getSqlEngine());
        SecDomainResolveSpi resolveSpi = sqlEngine.secDomainResolveSpi();

        CodeInfo codeInfo = CodeInfo.builder().baseLine(1).baseColumn(0).query(sql).build();
        ContextInfo contextInfo = ContextInfo.builder()//
            .dataSourceConfig(dsConfig1)
            .deepParser(false)
            .build();
        List<RuleDomain> list = resolveSpi.resolveDomain(rdpDataSourceDO.getDataSourceType(), codeInfo, contextInfo);
        DsConfig dsConfig2 = dmDsConfigService.dsConstantSettings(rdpDataSourceDO.getDataSourceType());
        List<String> levels = dsConfig2.getCategories().getLevels();
        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < dsLevels.size(); i++) {
            UmiTypes umiTypes = UmiTypes.valueOfCode(levels.get(i + 2));
            if (umiTypes == UmiTypes.Catalog) {
                map.put(SessionSpi.PARAMS_DEFAULT_DB, dsLevels.get(i));
            } else {
                map.put(SessionSpi.PARAMS_DEFAULT_SCHEMA, dsLevels.get(i));
            }
        }
        for (RuleDomain domain : list) {
            QueryTypeHandler handler = handlerMap.get(domain.getSqlType());
            if (handler == null) {
                continue;
            }
            handler.handleAfterSqlOperation(domain, dsId, map, execTime);
        }
    }
}
