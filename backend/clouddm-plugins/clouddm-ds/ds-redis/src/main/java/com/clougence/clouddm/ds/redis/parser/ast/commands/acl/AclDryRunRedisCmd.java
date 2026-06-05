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
package com.clougence.clouddm.ds.redis.parser.ast.commands.acl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;
import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.token.StrToken;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode
 * @date 2024/1/22 14:56
 */
@Getter
@Setter
public class AclDryRunRedisCmd extends AbstractRedisCmd {

    private List<StrToken> users    = new ArrayList<>();
    private List<StrToken> commands = new ArrayList<>();
    private List<StrToken> args     = new ArrayList<>();

    public void addUser(StrToken user) {
        this.users.add(user);
    }

    public void addCommand(StrToken command) {
        this.commands.add(command);
    }

    public void addArg(StrToken arg) {
        this.args.add(arg);
    }

    @Override
    public RedisCmdType getCmdType() { return RedisCmdType.ACL_DRYRUN; }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        writer.append("ACL DRYRUN");
        for (StrToken user : this.users) {
            writer.append(" ");
            writer.append(fmtString(user));
        }
        for (StrToken command : this.commands) {
            writer.append(" ");
            writer.append(fmtString(command));
        }
        for (StrToken arg : this.args) {
            writer.append(" ");
            writer.append(fmtString(arg));
        }
    }
}
