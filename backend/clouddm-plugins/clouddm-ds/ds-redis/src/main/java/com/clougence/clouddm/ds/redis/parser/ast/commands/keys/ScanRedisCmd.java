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
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScanRedisCmd extends AbstractRedisCmd {

    private final IntToken cursor;
    private final StrToken pattern;
    private IntToken       count;
    private StrToken       type;

    public ScanRedisCmd(IntToken cursor, StrToken pattern){
        this.cursor = cursor;
        this.pattern = pattern;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.SCAN; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // SCAN cursor [MATCH pattern] [COUNT count] [TYPE type]

        writer.append("SCAN");
        writer.append(" ");
        writer.append(fmtInt(cursor));
        if (pattern != null) {
            writer.append(" MATCH ");
            writer.append(fmtString(pattern));
        }
        if (count != null) {
            writer.append(" COUNT ");
            writer.append(fmtInt(count));
        }
        if (type != null) {
            writer.append(" TYPE ");
            writer.append(fmtString(type));
        }
    }
}
