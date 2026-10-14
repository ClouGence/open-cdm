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
package com.clougence.clouddm.ds.yashandb.dsconf.mysql;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.yashandb.dsconf.AbstractYashanDBConfig;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 崖山数据库（YashanDB）MySQL 兼容模式连接配置。
 * <p>
 * MySQL 兼容模式由标准 MySQL 协议端口提供服务（实测单机 23.4.1.109 为 {@code 1690}），
 * 连接串形如 {@code jdbc:mysql://host:1690/database}，公共配置项见 {@link AbstractYashanDBConfig}。
 * </p>
 *
 * @author open-cdm
 */
@Serialization(provider = YashanDBMySQLSerializationSpi.PROVIDER_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class YashanDBMySQLConfig extends AbstractYashanDBConfig {

    public YashanDBMySQLConfig(){
        super(DataSourceType.YashanDBMySQL);
    }
}
