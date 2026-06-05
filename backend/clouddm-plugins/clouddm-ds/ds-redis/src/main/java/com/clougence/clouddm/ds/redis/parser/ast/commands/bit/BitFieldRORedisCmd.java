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
package com.clougence.clouddm.ds.redis.parser.ast.commands.bit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.bit.bitfield.GetItem;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;
import com.clougence.utils.CollectionUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BitFieldRORedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private List<GetItem>  items = new ArrayList<>();

    public BitFieldRORedisCmd(StrToken keyName){
        this.keyName = keyName;
    }

    public void addItem(GetItem item) {
        if (item != null) {
            this.items.add(item);
        }
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.BITFIELD_RO; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("BITFIELD_RO");
        writer.append(" ");
        writer.append(fmtString(keyName));
        if (CollectionUtils.isNotEmpty(items)) {
            for (GetItem item : items) {
                writer.append(" GET ");
                writer.append(fmtString(item.getEncoding()));
                writer.append(" ");
                writer.append(fmtString(item.getOffset()));
            }
        }
    }
}
