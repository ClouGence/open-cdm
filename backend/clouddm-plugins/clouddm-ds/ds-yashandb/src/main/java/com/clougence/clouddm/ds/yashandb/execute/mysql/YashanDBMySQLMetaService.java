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
 * 崖山数据库（YashanDB）MySQL 兼容模式元数据服务。
 * <p>
 * 版本号、当前库等均沿用 {@link MyMetaService} 的 MySQL 实现（{@code version()} /
 * {@code select database()}），崖山 MySQL 兼容模式的表现与 MySQL 一致，无需覆写；
 * 仅把 UMI 服务替换为崖山实现。
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
