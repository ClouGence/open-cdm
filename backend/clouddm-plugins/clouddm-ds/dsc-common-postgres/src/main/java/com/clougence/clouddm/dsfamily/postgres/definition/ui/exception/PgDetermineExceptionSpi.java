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
package com.clougence.clouddm.dsfamily.postgres.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.definition.ui.exception.AbstractDetermineExceptionSpi;

public class PgDetermineExceptionSpi extends AbstractDetermineExceptionSpi {

    protected static final List<String> FAMILY_CONNECT_ERROR_MESSAGES;
    protected static final List<String> FAMILY_AUTH_ERROR_MESSAGES;

    static {
        FAMILY_CONNECT_ERROR_MESSAGES = new ArrayList<>(BASIC_CONNECT_ERROR_MESSAGES);
        FAMILY_CONNECT_ERROR_MESSAGES.add("Connect timed out");
        FAMILY_CONNECT_ERROR_MESSAGES.add("Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.");

        FAMILY_AUTH_ERROR_MESSAGES = new ArrayList<>(BASIC_AUTH_ERROR_MESSAGES);
        FAMILY_AUTH_ERROR_MESSAGES.add("password authentication failed for user");
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return FAMILY_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return FAMILY_AUTH_ERROR_MESSAGES; }
}
