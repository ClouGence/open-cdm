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

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.mysql.execute.MySession;
import com.clougence.drivers.DsObject;

/**
 * 崖山数据库（YashanDB）MySQL 兼容模式会话。
 * <p>
 * 直接复用 {@link MySession}：本变体使用 MySQL Connector/J 连接崖山的 MySQL 协议端口，
 * 会话行为（含父类 {@code killCurrentQuery()} 对 {@code com.mysql.cj.jdbc.ConnectionImpl}
 * 的强转）与标准 MySQL 完全一致，无需覆写。
 * </p>
 * <p>
 * 对比：KingbaseES 的 MySQL 变体走的是 Kingbase 自己的驱动，就必须覆写
 * {@code killCurrentQuery()} 改用 {@code unwrap(BaseConnection.class)}；崖山若改用
 * {@code jdbc:yasdb://...?compatVector=mysql}（见 {@code YashanDBMySQLDsFactory} 的说明）
 * 会面临同样问题，这也是本变体选择标准 MySQL 驱动的直接原因之一。
 * </p>
 *
 * @author open-cdm
 */
public class YashanDBMySQLSession extends MySession {

    public YashanDBMySQLSession(String newSessionId, DataSourceConfig dsConfig, DsObject<Connection> dsObject){
        super(newSessionId, dsConfig, dsObject, new YashanDBMySQLHooks(dsConfig.getVersion()));
    }
}
