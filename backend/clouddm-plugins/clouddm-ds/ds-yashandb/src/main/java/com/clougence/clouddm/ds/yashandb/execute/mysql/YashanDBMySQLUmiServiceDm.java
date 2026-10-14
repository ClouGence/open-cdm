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
 * 崖山数据库（YashanDB）MySQL 兼容模式 UMI 服务。
 * <p>
 * 层级结构、对象类型分派与 MySQL 完全一致，直接复用 {@link MyUmiServiceDm} 的分派实现，
 * 仅把元数据读取替换为 {@link YashanDBMySQLMetaProviderDm}。
 * </p>
 *
 * @author open-cdm
 */
public class YashanDBMySQLUmiServiceDm extends MyUmiServiceDm {

    public YashanDBMySQLUmiServiceDm(Connection connection){
        super(new YashanDBMySQLMetaProviderDm(connection));
    }
}
