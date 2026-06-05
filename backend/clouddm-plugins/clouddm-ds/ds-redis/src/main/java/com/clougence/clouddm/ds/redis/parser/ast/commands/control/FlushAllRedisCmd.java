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
package com.clougence.clouddm.ds.redis.parser.ast.commands.control;

import java.io.IOException;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ekko
 * @date 2024/1/22 14:56
*/
@Getter
@Setter
public class FlushAllRedisCmd extends AbstractRedisCmd {

    private Type type;

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.FLUSHALL; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("FLUSHALL");
        if (type != null) {
            switch (this.type) {
                case ASYNC: {
                    writer.append(" ");
                    writer.append("ASYNC");
                    break;
                }
                case SYNC: {
                    writer.append(" ");
                    writer.append("SYNC");
                    break;
                }
                default: {
                    throw new UnsupportedOperationException("Unsupported RedisCmdType " + this.type);
                }
            }
        }
    }

    public enum Type {
        ASYNC,
        SYNC
    }
}
