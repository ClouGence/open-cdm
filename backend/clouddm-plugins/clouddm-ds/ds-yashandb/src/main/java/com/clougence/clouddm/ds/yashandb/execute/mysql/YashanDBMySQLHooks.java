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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.clougence.clouddm.dsfamily.mysql.execute.MyHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;

import lombok.extern.slf4j.Slf4j;

/**
 * 崖山数据库（YashanDB）MySQL 兼容模式 SessionHook。
 * <p>
 * 事务、隔离级别、fetch size 等会话行为沿用 MySQL 实现（崖山 MySQL 兼容模式在 MySQL 协议端口上
 * 提供标准 MySQL 语义），差异点与元数据服务在崖山实现里替换。
 * </p>
 *
 * @author open-cdm
 */
@Slf4j
public class YashanDBMySQLHooks extends MyHooks {

    /**
     * 崖山 MySQL 兼容模式未实现 {@code CONNECTION_ID()}，取出当前会话标识的等价写法。
     * <p>
     * 实测 23.4.1.109：{@code SELECT CONNECTION_ID()} 报 {@code YAS-04243 invalid identifier}，
     * {@code @@connection_id} / {@code @@pseudo_thread_id} 报 {@code YAS-04607 unknown system variable}；
     * 而 {@code SYS_CONTEXT('USERENV','SID')} 与 {@code SHOW PROCESSLIST} 的首列取值一致
     * （同一会话均为同一编号），因此以此为降级来源。
     * </p>
     */
    private static final String SESSION_ID_SQL = "SELECT SYS_CONTEXT('USERENV','SID') FROM DUAL";

    public YashanDBMySQLHooks(String mainVersion){
        super(mainVersion);
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new YashanDBMySQLMetaService(session);
    }

    /**
     * 会话初始化阶段 {@code DefaultRdbSession} 会调用本方法获取连接级查询标识，
     * 异常会直接导致建连失败，因此这里对 MySQL 原生写法做兼容降级。
     */
    @Override
    public String getQueryID(Connection conn) throws SQLException {
        try {
            return super.getQueryID(conn);
        } catch (SQLException e) {
            log.debug("YashanDB(MySQL mode) does not implement CONNECTION_ID(), fallback to SYS_CONTEXT: {}", e.getMessage());
        }

        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(SESSION_ID_SQL)) {
            return resultSet.next() ? resultSet.getString(1) : null;
        }
    }
}
