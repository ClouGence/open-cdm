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

import com.clougence.clouddm.dsfamily.mysql.execute.MyUmiServiceDm;

/**
 * UMI service for the YashanDB MySQL compatibility mode.
 * <p>
 * The hierarchy and object type dispatch are exactly the same as MySQL, so the dispatch implementation of
 * {@link MyUmiServiceDm} is reused directly and only metadata reading is replaced with
 * {@link YashanDBMySQLMetaProviderDm}.
 * </p>
 *
 * @author open-cdm
 */
public class YashanDBMySQLUmiServiceDm extends MyUmiServiceDm {

    public YashanDBMySQLUmiServiceDm(Connection connection){
        super(new YashanDBMySQLMetaProviderDm(connection));
    }
}
