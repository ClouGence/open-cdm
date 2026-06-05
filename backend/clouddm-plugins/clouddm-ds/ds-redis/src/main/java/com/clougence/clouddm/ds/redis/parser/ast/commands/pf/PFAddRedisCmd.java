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
package com.clougence.clouddm.ds.redis.parser.ast.commands.pf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PFAddRedisCmd extends AbstractRedisCmd {

    private StrToken       keyName;
    private List<StrToken> element = new ArrayList<>();

    public void addElement(StrToken key) {
        if (key != null) {
            this.element.add(key);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.PFADD; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("PFADD");
        writer.append(" ");
        writer.append(fmtString(this.keyName));
        if (this.element != null) {
            for (StrToken keyName : this.element) {
                writer.append(" ");
                writer.append(fmtString(keyName));
            }
        }
    }
}
