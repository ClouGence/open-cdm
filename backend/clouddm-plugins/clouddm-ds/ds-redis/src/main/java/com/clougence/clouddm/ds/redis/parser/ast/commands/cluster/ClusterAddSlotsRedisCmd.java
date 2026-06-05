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
package com.clougence.clouddm.ds.redis.parser.ast.commands.cluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2023-06-06 14:19
 */
@Getter
@Setter
public class ClusterAddSlotsRedisCmd extends AbstractRedisCmd {

    private final List<IntToken> slots = new ArrayList<>();

    public void addSlot(IntToken slot) {
        this.slots.add(slot);
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.CLUSTER_ADDSLOTS; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("CLUSTER ADDSLOTS");
        for (IntToken slot : this.slots) {
            writer.append(" ");
            writer.append(fmtInt(slot));
        }
    }
}
