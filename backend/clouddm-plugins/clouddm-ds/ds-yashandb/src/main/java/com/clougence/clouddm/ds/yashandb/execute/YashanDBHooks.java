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
package com.clougence.clouddm.ds.yashandb.execute;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.oracle.execute.OraHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.result.ColReader;

/**
 * 崖山数据库 SessionHook。
 * <p>
 * 除元数据服务需要替换为崖山实现外，其余会话行为沿用 Oracle 体系。
 * </p>
 *
 * @author open-cdm
 */
public class YashanDBHooks extends OraHooks {

    private final String clientCharset;

    public YashanDBHooks(DataSourceConfig config){
        super(config);
        // 父类字段为 private，这里自行解析一遍（解析逻辑与父类一致）
        this.clientCharset = config instanceof com.clougence.clouddm.sdk.execute.dsconf.capability.ClientCharsetExtProperties e ? e.getClientCharset() : null;
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new YashanDBMetaService(session);
    }

    @Override
    public ColReader createColReader() {
        return new YashanDBColReader(this.clientCharset);
    }
}
