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
package com.clougence.sql.mongodb.parser.ast.commands.db;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.clougence.sql.mongodb.parser.ast.MongoFuncType;
import com.clougence.sql.mongodb.parser.ast.commands.AbstractMongoFunc;

/**
 * Read-only replica-set / sharding shell helpers mapped to runCommand payloads.
 * Write/topology-changing helpers are intentionally unsupported here.
 */
public final class MongoAdminReadCommands {

    private static final String ADMIN = "admin";
    private static final String LOCAL = "local";

    private static final Map<String, AbstractMongoFunc> RS_COMMANDS = new HashMap<>();
    private static final Map<String, AbstractMongoFunc> SH_COMMANDS = new HashMap<>();

    // Completion labels keep shell camelCase; resolve* still accepts any case.
    private static final List<String> RS_METHODS = List.of(
        "status",
        "printSecondaryReplicationInfo",
        "printSlaveReplicationInfo",
        "conf",
        "config",
        "printReplicationInfo",
        "getReplicationInfo",
        "hello",
        "isMaster"
    );
    private static final List<String> SH_METHODS = List.of(
        "status",
        "printShardingStatus",
        "isBalancerRunning",
        "getBalancerState",
        "balancerStatus"
    );

    static {
        MongoReadCommandFunc rsStatus = read(MongoFuncType.RS_STATUS, "{\"replSetGetStatus\":1}", ADMIN);
        alias(RS_COMMANDS, rsStatus, "status", "printSecondaryReplicationInfo", "printSlaveReplicationInfo");

        MongoReadCommandFunc rsConf = read(MongoFuncType.RS_CONF, "{\"replSetGetConfig\":1}", ADMIN);
        alias(RS_COMMANDS, rsConf, "conf", "config");

        MongoReadCommandFunc rsReplicationInfo = read(MongoFuncType.RS_REPLICATION_INFO, "{\"collStats\":\"oplog.rs\"}", LOCAL);
        alias(RS_COMMANDS, rsReplicationInfo, "printReplicationInfo", "getReplicationInfo");

        MongoReadCommandFunc isMaster = read(MongoFuncType.HELLO, "{\"isMaster\":1}", ADMIN);
        alias(RS_COMMANDS, isMaster, "ismaster");

        MongoReadCommandFunc shStatus = read(MongoFuncType.SH_STATUS, "{\"listShards\":1}", ADMIN);
        alias(SH_COMMANDS, shStatus, "status", "printShardingStatus");

        MongoReadCommandFunc balancerStatus = read(MongoFuncType.SH_BALANCER_STATUS, "{\"balancerStatus\":1}", ADMIN);
        alias(SH_COMMANDS, balancerStatus, "isBalancerRunning", "getBalancerState", "balancerStatus");
    }

    private MongoAdminReadCommands(){
    }

    public static List<String> listRsMethods() {
        return RS_METHODS;
    }

    public static List<String> listShMethods() {
        return SH_METHODS;
    }

    public static AbstractMongoFunc resolveRs(String method) {
        if ("hello".equalsIgnoreCase(method)) {
            return new HelloFunc();
        }
        return RS_COMMANDS.get(normalize(method));
    }

    public static AbstractMongoFunc resolveSh(String method) {
        return SH_COMMANDS.get(normalize(method));
    }

    private static MongoReadCommandFunc read(MongoFuncType funcType, String bson, String database) {
        return new MongoReadCommandFunc(funcType, bson, database);
    }

    private static void alias(Map<String, AbstractMongoFunc> target, AbstractMongoFunc command, String... names) {
        for (String name : names) {
            target.put(normalize(name), command);
        }
    }

    private static String normalize(String method) {
        return method.toLowerCase(Locale.ROOT);
    }
}
