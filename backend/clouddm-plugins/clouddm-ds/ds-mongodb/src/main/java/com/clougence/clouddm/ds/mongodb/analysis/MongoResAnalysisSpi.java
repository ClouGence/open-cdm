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
package com.clougence.clouddm.ds.mongodb.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.mongodb.analysis.secrules.MongoCmdDomain;
import com.clougence.clouddm.ds.mongodb.analysis.secrules.MongoCollectionDomain;
import com.clougence.clouddm.dsfamily.analysis.resource.RdbResAnalysisSpi;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.RdbResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.StringUtils;

public class MongoResAnalysisSpi extends RdbResAnalysisSpi {

    protected MongoSecDomainResolveSpi resolveSpi;

    public MongoResAnalysisSpi(MetaService metaService){
        super();
        this.resolveSpi = new MongoSecDomainResolveSpi(metaService);
    }

    @Override
    protected List<RuleDomain> resolveDomain(DataSourceType dsType, CodeInfo codeInfo, ContextInfo contextInfo) {
        return this.resolveSpi.resolveDomain(dsType, codeInfo, contextInfo);
    }

    @Override
    protected List<RdbResObject> convert(DataSourceType dsType, RuleDomain domain, Map<String, Object> ctx) {
        List<RdbResObject> resObjs = new ArrayList<>();

        if (domain instanceof MongoCollectionDomain) {
            MongoCollectionDomain collectionDomain = (MongoCollectionDomain) domain;
            RdbResObject object = new RdbResObject();
            object.setType(TargetType.Table);
            object.setTable(collectionDomain.getCollection());
            resObjs.add(object);
        } else if (domain instanceof MongoCmdDomain) {
            RdbResObject resObj = newObject(domain);
            resObj.setType(TargetType.Schema);
            resObjs.add(resObj);
        } else {
            resObjs.add(newObject(domain));
        }

        for (RdbResObject res : resObjs) {
            this.fillCtx(ctx, res);
        }
        return resObjs;
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
