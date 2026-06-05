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
package com.clougence.clouddm.onlineddl.ghost;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.rdb.mysql.MySqlConfig;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.model.onlineddl.GhostConfig;

/**
 * @author bucketli 2022/6/1 15:23:51
 */
public class GhostSessionFactory /*implements ToolSessionFactoryOld<MySqlConfig>*/ {

    //@Override
    public GhostSessionOld createSession(DsResourceManager rm, MySqlConfig dsConfig, GhostConfig contextDTO) {
        if (dsConfig.getDataSourceType() != DataSourceType.MySQL) {
            throw new IllegalArgumentException("gh-ost only support MySQL DDL.");
        }

        //        ExportSqlExecType execType = contextDTO.getExportSqlExecType();
        //        if (execType != ExportSqlExecType.GH_OST) {
        //            throw new IllegalArgumentException("execType is not GH_OST.");
        //        }

        GhostSessionOld session = new GhostSessionOld(dsConfig, contextDTO);
        session.init();
        return session;
    }
}
