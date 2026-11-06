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
package com.clougence.clouddm.ds.iotdb.execute;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportLevel;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportSpi;

public class IotdbSupportSpi implements RdbSupportSpi {

    @Override
    public RdbSupportLevel supportChangeCatalog(DataSourceConfig config) {
        return RdbSupportLevel.No;
    }

    @Override
    public RdbSupportLevel supportChangeSchema(DataSourceConfig config) {
        return RdbSupportLevel.No;
    }

    @Override
    public RdbSupportLevel supportChangeIsolation(DataSourceConfig config) {
        return RdbSupportLevel.No;
    }

    @Override
    public RdbSupportLevel supportChangeAutoCommit(DataSourceConfig config) {
        return RdbSupportLevel.No;
    }

    @Override
    public RdbSupportLevel supportChangeReadOnly(DataSourceConfig config) {
        return RdbSupportLevel.No;
    }

    @Override
    public RdbSupportLevel supportCancelQuery(DataSourceConfig config) {
        return RdbSupportLevel.Hint;
    }

    @Override
    public RdbSupportLevel supportExplain(DataSourceConfig config) {
        return RdbSupportLevel.No;
    }

    @Override
    public RdbSupportLevel supportFormat(DataSourceConfig config) {
        return RdbSupportLevel.No;
    }

    @Override
    public RdbSupportLevel supportArgs(DataSourceConfig config) {
        return RdbSupportLevel.No;
    }

    @Override
    public List<RdbIsolation> supportIsolation() {
        return List.of();
    }
}
