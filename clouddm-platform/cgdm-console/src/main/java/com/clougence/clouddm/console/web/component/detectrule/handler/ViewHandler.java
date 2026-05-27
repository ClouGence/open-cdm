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
package com.clougence.clouddm.console.web.component.detectrule.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbViewDomain;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.model.datasource.MetaInformationType;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

import jakarta.annotation.Resource;

@Component
public class ViewHandler implements QueryTypeHandler {
    @Resource
    private DataSourceDal dsDal;

    @Override
    public void handleAfterSqlOperation(RuleDomain ruleDomain, Long dsId, Map<String, String> map, Date execTime) {
        RdbViewDomain domain = (RdbViewDomain) ruleDomain;
        StringBuilder path = new StringBuilder("/");
        if (domain.getCatalog() != null) {
            path.append(domain.getCatalog()).append("/");
        } else if (map.get(SessionSpi.PARAMS_DEFAULT_DB) != null) {
            path.append(map.get(SessionSpi.PARAMS_DEFAULT_DB)).append("/");
        }

        if (domain.getSchema() != null) {
            path.append(domain.getSchema()).append("/");
        } else if (map.get(SessionSpi.PARAMS_DEFAULT_SCHEMA) != null) {
            path.append(map.get(SessionSpi.PARAMS_DEFAULT_SCHEMA)).append("/");
        }
        dsDal.metaDataMapper().deleteByPath(dsId, path.toString(), MetaInformationType.ViewList, execTime);
        path.append(domain.getView());
        dsDal.metaDataMapper().deleteByPath(dsId, path.toString(), MetaInformationType.ViewDetail, execTime);
    }

    @Override
    public List<SecQueryType> canHandleType() {
        return Arrays.asList(SecQueryType.CREATE_VIEW, SecQueryType.DROP_VIEW, SecQueryType.ALTER_VIEW);
    }
}
