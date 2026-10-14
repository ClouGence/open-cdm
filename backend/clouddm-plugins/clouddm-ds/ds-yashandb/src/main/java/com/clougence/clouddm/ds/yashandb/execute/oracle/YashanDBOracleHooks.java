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
package com.clougence.clouddm.ds.yashandb.execute.oracle;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.oracle.execute.OraHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;

/**
 * Session hook for YashanDB.
 * <p>
 * Apart from the metadata service, which is replaced with the YashanDB implementation, all other session
 * behaviour follows the Oracle family.
 * </p>
 *
 * @author open-cdm
 */
public class YashanDBOracleHooks extends OraHooks {

    private final String clientCharset;

    public YashanDBOracleHooks(DataSourceConfig config){
        super(config);
        // The parent field is private, so parse it again here (the parsing logic is the same as the parent's)
        this.clientCharset = config instanceof com.clougence.clouddm.sdk.execute.dsconf.capability.ClientCharsetExtProperties e ? e.getClientCharset() : null;
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new YashanDBOracleMetaService(session);
    }

    @Override
    public ColReader createColReader() {
        return new YashanDBOracleColReader(this.clientCharset);
    }
}
