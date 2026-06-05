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
package com.clougence.clouddm.ds.redis.parser.ast.commands.sortedset;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.ScoreLex;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrIntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mode
 * @Date: 2023-07-10 11:15
 */
@Getter
@Setter
public class ZRangeStoreRedisCmd extends AbstractRedisCmd {

    private StrToken    dstKey;
    private StrToken    srcName;
    private StrIntToken min;
    private StrIntToken max;
    private ScoreLex    scoreLex;
    private boolean     rev;
    private IntToken    limitOffset;
    private IntToken    limitCount;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ZRANGESTORE; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ZRANGESTORE");
        writer.append(" ");
        writer.append(fmtString(dstKey));
        writer.append(" ");
        writer.append(fmtString(srcName));
        writer.append(" ");
        writer.append(fmtString(this.min));
        writer.append(" ");
        writer.append(fmtString(this.max));
        if (scoreLex != null) {
            writer.append(" ");
            writer.append(this.scoreLex.name());
        }
        if (rev) {
            writer.append(" REV");
        }
        if (limitOffset != null && limitCount != null) {
            writer.append(" LIMIT ");
            writer.append(fmtInt(this.limitOffset));
            writer.append(" ");
            writer.append(fmtInt(this.limitCount));
        }
    }
}
