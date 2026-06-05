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
package com.clougence.clouddm.ds.sqlserver.analysis;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.resource.RdbResAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCatalogDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.sdk.analysis.secrules.ResAnalysisSpi;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.RdbResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.ObjectUtils;

public class MsSqlResAnalysisSpi extends RdbResAnalysisSpi implements ResAnalysisSpi {

    protected MsSqlSecDomainResolveSpi resolveSpi;

    public MsSqlResAnalysisSpi(){
        this.resolveSpi = new MsSqlSecDomainResolveSpi();
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
}
