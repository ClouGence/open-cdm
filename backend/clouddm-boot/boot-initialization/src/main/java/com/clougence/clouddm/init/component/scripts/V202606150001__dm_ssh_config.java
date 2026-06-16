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
package com.clougence.clouddm.init.component.scripts;

import java.util.List;

import com.clougence.clouddm.init.component.flyway.AbstractUpgradeJavaMigration;

public class V202606150001__dm_ssh_config extends AbstractUpgradeJavaMigration {

    @Override
    public List<String> collectScript() {
        return List.of("""
                CREATE TABLE IF NOT EXISTS `dm_ssh_config` (
                  `id` bigint(20) NOT NULL AUTO_INCREMENT,
                  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                  `name` varchar(128) NOT NULL COMMENT 'SSH 配置名称',
                  `host` varchar(512) NOT NULL COMMENT 'SSH 主机',
                  `port` int NOT NULL DEFAULT 22 COMMENT 'SSH 端口',
                  `username` varchar(255) NOT NULL COMMENT 'SSH 用户名',
                  `auth_type` varchar(64) NOT NULL COMMENT 'PASSWORD / PRIVATE_KEY',
                  `password` varchar(512) DEFAULT NULL COMMENT '登录密码或交互式认证密码密文',
                  `private_key_data` longtext DEFAULT NULL COMMENT '私钥数据，使用 URI scheme 区分来源',
                  `private_key_passphrase` varchar(512) DEFAULT NULL COMMENT '私钥 passphrase 密文',
                  `con_features` longtext DEFAULT NULL COMMENT 'JSON map for SSH connection parameters',
                  `proxy_type` varchar(64) NOT NULL DEFAULT 'NONE' COMMENT 'NONE / HTTP / SOCKS4 / SOCKS5',
                  `proxy_features` longtext DEFAULT NULL COMMENT 'JSON map for proxy parameters',
                  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
                  PRIMARY KEY (`id`),
                  KEY `idx_host` (`host`(127)),
                  KEY `idx_proxy_type` (`proxy_type`),
                  KEY `idx_deleted` (`deleted`)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """);
    }
}
