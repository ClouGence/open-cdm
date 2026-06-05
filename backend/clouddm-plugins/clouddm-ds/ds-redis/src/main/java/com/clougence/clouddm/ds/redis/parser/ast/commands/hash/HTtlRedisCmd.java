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
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mode
 * @Date: 2023-06-30 14:35
 */
@Getter
@Setter
public class HTtlRedisCmd extends AbstractRedisCmd {

    private StrToken       key;
    private IntToken       numFields;
    private List<StrToken> filedNames = new ArrayList<>();

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.HTTL; }

    public void addField(StrToken field) {
        if (field != null) {
            this.filedNames.add(field);
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("HTTL");
        writer.append(" ");
        writer.append(fmtString(this.key));
        writer.append(" FIELDS ");
        writer.append(fmtInt(this.numFields));
        for (StrToken filed : this.filedNames) {
            writer.append(" ");
            writer.append(fmtString(filed));
        }
    }
}
