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
package com.clougence.clouddm.ds.clickhouse.analysis.builder;

import java.util.*;

import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChUpdateDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.UpdateBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbUpdateDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class ChUpdateBuilder extends UpdateBuilder {

    public ChUpdateBuilder(Stack<List<WithSelectDomain>> selectStack){
        super(selectStack);
    }

    @Override
    protected RdbUpdateDomain getRdbUpdateDomain() {
        ChUpdateDomain pgUpdateDomain = new ChUpdateDomain();
        pgUpdateDomain.setSetColumns(new ArrayList<>());
        return pgUpdateDomain;
    }

    @Override
    public List<Domain> build() {
        if (!this.selectStack.peek().isEmpty()) {
            this.updateDomain.setHasWith(true);
        }

        if (CollectionUtils.isNotEmpty(nameList)) {
            Map<UmiTypes, String> map = BuilderUtil.parseTableName(nameList);
            this.updateDomain.setTable(map.get(UmiTypes.Table));
            this.updateDomain.setSchema(map.get(UmiTypes.Schema));
            this.updateDomain.setCatalog(map.get(UmiTypes.Catalog));
        }
        updateDomain.setAuditKind(SecQueryKind.DML);
        updateDomain.setSqlType(SecQueryType.UPDATE);
        if (updateDomain.getWhereColumns() == null) {
            updateDomain.setWhereColumns(new ArrayList<>());
        }
        return Collections.singletonList(this.updateDomain);
    }
}
