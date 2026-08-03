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
package com.clougence.sql.redis.parser.ast.commands.control;

import java.io.IOException;

import com.clougence.dslpaser.foramt.FmtWriter;
import com.clougence.sql.redis.parser.ast.RedisCmdType;
import com.clougence.sql.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.sql.redis.parser.ast.token.IntToken;

import lombok.Getter;

@Getter
public class SwapDbRedisCmd extends AbstractRedisCmd {

    private final IntToken index1;
    private final IntToken index2;

    public SwapDbRedisCmd(IntToken index1, IntToken index2){
        this.index1 = index1;
        this.index2 = index2;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.SWAPDB; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("SWAPDB ");
        writer.append(fmtInt(this.index1));
        writer.append(" ");
        writer.append(fmtInt(this.index2));
    }
}
