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
package com.clougence.clouddm.ds.redis.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.definition.ui.exception.AbstractDetermineExceptionSpi;

public class RedisDetermineExceptionSpi extends AbstractDetermineExceptionSpi {

    protected static final List<String> REDIS_CONNECT_ERROR_MESSAGES;
    protected static final List<String> REDIS_AUTH_ERROR_MESSAGES;

    static {
        REDIS_CONNECT_ERROR_MESSAGES = new ArrayList<>(BASIC_CONNECT_ERROR_MESSAGES);
        REDIS_CONNECT_ERROR_MESSAGES.add("Failed to create socket");
        REDIS_CONNECT_ERROR_MESSAGES.add("Failed to connect to any host resolved for DNS name");
        REDIS_CONNECT_ERROR_MESSAGES.add("Failed to create socket");

        REDIS_AUTH_ERROR_MESSAGES = new ArrayList<>(BASIC_AUTH_ERROR_MESSAGES);
        REDIS_AUTH_ERROR_MESSAGES.add("WRONGPASS invalid username-password pair or user is disabled");
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return REDIS_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return REDIS_AUTH_ERROR_MESSAGES; }
}
