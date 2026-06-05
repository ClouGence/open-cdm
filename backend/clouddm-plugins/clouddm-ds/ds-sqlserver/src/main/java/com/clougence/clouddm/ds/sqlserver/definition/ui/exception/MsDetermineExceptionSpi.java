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
package com.clougence.clouddm.ds.sqlserver.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.definition.ui.exception.AbstractDetermineExceptionSpi;

public class MsDetermineExceptionSpi extends AbstractDetermineExceptionSpi {

    protected static final List<String> MS_CONNECT_ERROR_MESSAGES;
    protected static final List<String> MS_AUTH_ERROR_MESSAGES;

    static {
        MS_CONNECT_ERROR_MESSAGES = new ArrayList<>(BASIC_CONNECT_ERROR_MESSAGES);
        MS_CONNECT_ERROR_MESSAGES.add("Make sure that TCP connections to the port are not blocked by a firewall.");
        MS_CONNECT_ERROR_MESSAGES.add("TCP/IP 连接失败");
        MS_CONNECT_ERROR_MESSAGES.add("Verify the connection properties");

        MS_AUTH_ERROR_MESSAGES = new ArrayList<>(BASIC_AUTH_ERROR_MESSAGES);
        MS_AUTH_ERROR_MESSAGES.add("登录失败");
        MS_AUTH_ERROR_MESSAGES.add("Login failed for user");
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return MS_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return MS_AUTH_ERROR_MESSAGES; }
}
