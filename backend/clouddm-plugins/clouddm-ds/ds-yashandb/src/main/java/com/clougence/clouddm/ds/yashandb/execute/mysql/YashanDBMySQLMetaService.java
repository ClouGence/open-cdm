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
package com.clougence.clouddm.ds.yashandb.execute.mysql;

import java.sql.Connection;

import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;

/**
 * Metadata service for the YashanDB MySQL compatibility mode.
 * <p>
 * Version and current database both reuse the MySQL implementation of {@link MyMetaService}
 * ({@code version()} / {@code select database()}), since the YashanDB MySQL compatibility mode behaves the same
 * as MySQL and needs no override; only the UMI service is replaced with the YashanDB implementation.
 * </p>
 *
 * @author open-cdm
 */
public class YashanDBMySQLMetaService extends MyMetaService {

    public YashanDBMySQLMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new YashanDBMySQLUmiServiceDm(con);
    }
}
