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
package com.clougence.clouddm.dsfamily.execute;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.schema.DsType;
import com.clougence.schema.umi.service.AbstractUmiService;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.StringUtils;
import com.clougence.utils.function.ESupplier;

/**
 * AbstractRdbUmiServiceDm for CloudDM
 * 
 * @author mode 2021/1/8 19:56
 */
public abstract class AbstractRdbUmiService<T extends AbstractMetadataProvider> extends AbstractUmiService<T, SQLException> implements DmRdbUmiService {

    public AbstractRdbUmiService(ESupplier<T, SQLException> metadataSupplier){
        super(metadataSupplier);
    }

    @Override
    public String getVersion() throws SQLException { return this.metadataSupplier.eGet().getVersion(); }

    @Override
    public final DsType getDataSourceType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Value detailLevel(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) throws SQLException {
        UmiTypes leafType = levels.isEmpty() ? null : levels.get(levels.size() - 1);
        String leafName = StringUtils.toString(levelsParam.get(leafType));
        return this.detailLeaf(levelsParam, leafType, leafName);
    }

    @Override
    public Map<String, List<RdbColumn>> loadColumns(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> leafNames) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Value fetchSelectObject(Map<UmiTypes, Object> levelsParam, String leafName) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
