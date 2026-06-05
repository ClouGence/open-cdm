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
package com.clougence.clouddm.ds.redis.parser.ast.commands.info;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

/**
 * @Author: Ekko
 * @Date: 2023-06-05 11:19
 */
@Getter
public class InfoRedisCmd extends AbstractRedisCmd {

    private final List<InfoType> types = new ArrayList<>();

    public void addType(InfoType type) {
        if (type != null) {
            this.types.add(type);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.INFO; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("INFO");
        if (!this.types.isEmpty()) {
            for (InfoType type : types) {
                writer.append(" ");
                writer.append(type.name().toUpperCase());
            }
        }
    }

    public enum InfoType {
        SERVER,
        THREADS,
        CLUSTER,
        CLIENTS,
        MEMORY,
        PERSISTENCE,
        STATS,
        REPLICATION,
        CPU,
        COMMANDSTATS,
        LATENCYSTATS,
        SENTINEL,
        MODULES,
        KEYSPACE,
        ERRORSTATS,
        ALL,
        DEFAULT,
        EVERYTHING
    }
}
