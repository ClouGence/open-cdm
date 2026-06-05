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
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.IntToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.SortByToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.clouddm.ds.redis.parser.ast.token.TagToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortRORedisCmd extends AbstractRedisCmd {

    private final StrToken keyName;
    private StrToken       byPattern;
    private IntToken       limitOffset;
    private IntToken       limitCount;
    private List<StrToken> getPatterns;
    private SortByToken    sortToken;
    private TagToken       alphaTag;

    public SortRORedisCmd(StrToken keyName){
        this.keyName = keyName;
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.SORT_RO; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        // SORT_RO key [BY pattern] [LIMIT offset count] [GET pattern [GET pattern ...]] [ASC|DESC] [ALPHA]

        writer.append("SORT_RO");
        writer.append(" ");
        writer.append(fmtString(keyName));
        if (byPattern != null) {
            writer.append(" BY ");
            writer.append(fmtString(byPattern));
        }

        if (limitOffset != null && limitCount != null) {
            writer.append(" LIMIT ");
            writer.append(fmtInt(limitOffset));
            writer.append(" ");
            writer.append(fmtInt(limitCount));
        }
        if (getPatterns != null && !getPatterns.isEmpty()) {
            for (StrToken get : getPatterns) {
                writer.append(" GET ");
                writer.append(fmtString(get));
            }
        }
        if (sortToken != null) {
            writer.append(" ");
            writer.append(sortToken.getOptionType().name().toUpperCase());
        }
        if (alphaTag != null) {
            writer.append(" ALPHA");
        }
    }
}
