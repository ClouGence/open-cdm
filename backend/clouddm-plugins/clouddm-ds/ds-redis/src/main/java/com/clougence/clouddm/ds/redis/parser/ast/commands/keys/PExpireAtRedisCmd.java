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
package com.clougence.clouddm.ds.redis.parser.ast.commands.keys;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.KeyOptToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PExpireAtRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private final IntToken unixTimeSeconds;
    private KeyOptToken    keyOpt;

    public PExpireAtRedisCmd(StrToken keyName, IntToken unixTimeSeconds){
        this.keyName = keyName;
        this.unixTimeSeconds = unixTimeSeconds;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.PEXPIREAT; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // PEXPIREAT key unix-time-milliseconds [NX|XX|GT|LT]

        writer.append("PEXPIREAT");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtInt(unixTimeSeconds));
        if (keyOpt != null) {
            writer.append(" ");
            writer.append(keyOpt.getOptionType().name().toUpperCase());
        }
    }
}
