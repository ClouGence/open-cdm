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
package com.clougence.clouddm.ds.redis.parser.ast.commands;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Collections;

import com.clougence.clouddm.ds.redis.parser.ast.AbstractRedisAst;
import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.token.*;
import com.clougence.dslpaser.ast.Statement;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.SneakyThrows;

public abstract class AbstractRedisCmd extends AbstractRedisAst implements Statement {

    public abstract RedisCmdType getCmdType();

    @SneakyThrows
    public String toString() {
        StringWriter writer = new StringWriter();
        this.doFormat(new FmtWriter(writer, Collections.emptyMap()));
        return writer.toString();
    }

    protected String fmtString(StrToken v) {
        if (v.isArg()) {
            return "?";
        }

        if (!v.isQuotes()) {
            return v.getValue();
        }

        String str = v.getValue();
        if (str.contains("'") && !str.contains("\"")) {
            return "\"" + str + "\"";
        } else if (str.contains("\"") && !str.contains("'")) {
            return "'" + str + "'";
        } else {
            return "\"" + str.replace("'", "\'") + "\"";
        }
    }

    protected String fmtString(StrIntToken v) {
        if (v.isArg()) {
            return "?";
        }

        return v.getValue();
    }

    protected String fmtBool(BoolToken v) {
        if (v.isArg()) {
            return "?";
        }
        return v.isBool() ? "1" : "0";
    }

    protected String fmtInt(IntToken v) {
        if (v.isArg()) {
            return "?";
        } else {
            BigInteger bigInt = v.getBigInteger();
            return bigInt.toString(10);
        }
    }

    protected String fmtFloat(FloatToken v) {
        if (v.isArg()) {
            return "?";
        } else {
            return v.getFloatValueStr();
        }
    }
}
