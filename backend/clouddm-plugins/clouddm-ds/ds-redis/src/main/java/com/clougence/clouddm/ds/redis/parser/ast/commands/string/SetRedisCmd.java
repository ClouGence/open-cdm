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
package com.clougence.clouddm.ds.redis.parser.ast.commands.string;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.KeyOptToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.TagToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.TtlOptToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetRedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private final StrToken value;
    private TtlOptToken    ttlOptToken;
    private TagToken       keepTtlTag;
    private KeyOptToken    optToken;
    private TagToken       getTag;

    public SetRedisCmd(StrToken keyName, StrToken value){
        this.keyName = keyName;
        this.value = value;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.SET; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // SET key value [EX seconds | PX milliseconds | EXAT unix-time-seconds | PXAT unix-time-milliseconds | KEEPTTL] [NX|XX] [GET]

        writer.append("SET");
        writer.append(" ");
        writer.append(fmtString(keyName));
        writer.append(" ");
        writer.append(fmtString(value));
        if (optToken != null) {
            writer.append(" ");
            writer.append(optToken.getOptionType().name());
        }

        if (getTag != null) {
            writer.append(" GET");
        }

        if (keepTtlTag != null) {
            writer.append(" KEEPTTL");
        } else if (ttlOptToken != null) {
            writer.append(" ");
            writer.append(ttlOptToken.getTtlType().name().toUpperCase());
            writer.append(" ");
            writer.append(fmtInt(ttlOptToken.getValue()));
        }
    }
}
