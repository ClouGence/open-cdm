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
package com.clougence.clouddm.ds.redis.parser.ast.commands.hash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.*;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mode
 * @Date: 2023-06-30 14:35
 */
@Getter
@Setter
public class HSetEXRedisCmd extends AbstractRedisCmd {

    private StrToken                key;
    private HSetExOpt               fxOpt;

    private TtlOptToken             ttlOptToken;
    private TagToken                keepTtlTag;

    private IntToken                numFields;
    private List<KeyAndStringToken> keyValues = new ArrayList<>();

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.HSETEX; }

    public void addKeyValue(KeyAndStringToken kvToken) {
        if (kvToken != null) {
            this.keyValues.add(kvToken);
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("HSETEX");
        writer.append(" ");
        writer.append(fmtString(this.key));

        if (this.fxOpt != null) {
            writer.append(" ");
            writer.append(this.fxOpt.name());
        }

        if (this.ttlOptToken != null) {
            writer.append(" ");
            writer.append(this.ttlOptToken.getTtlType().name().toUpperCase());
            writer.append(" ");
            writer.append(fmtInt(this.ttlOptToken.getValue()));
        } else if (this.keepTtlTag != null) {
            writer.append(" KEEPTTL");
        }

        writer.append(" FIELDS ");
        writer.append(fmtInt(this.numFields));
        for (KeyAndStringToken keyVal : this.keyValues) {
            writer.append(" ");
            writer.append(fmtString(keyVal.getKeyName()));
            writer.append(" ");
            writer.append(fmtString(keyVal.getStringValue()));
        }
    }

    public static enum HSetExOpt {
        FNX,
        FXX
    }
}
