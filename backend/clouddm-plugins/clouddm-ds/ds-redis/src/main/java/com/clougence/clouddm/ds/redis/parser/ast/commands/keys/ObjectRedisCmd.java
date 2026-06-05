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
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;

@Getter
public class ObjectRedisCmd extends AbstractRedisCmd {

    private final StrToken   keyName;
    private final OptionType operation;

    public ObjectRedisCmd(StrToken keyName, OptionType operation){
        this.keyName = keyName;
        this.operation = operation;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.OBJECT; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // OBJECT (ENCODING | FREQ | IDLETIME | REFCOUNT) key

        writer.append("OBJECT ");
        writer.append(operation.name().toUpperCase());
        writer.append(" ");
        writer.append(fmtString(keyName));
    }

    public static enum OptionType {
        ENCODING,
        FREQ,
        IDLETIME,
        REFCOUNT
    }

}
