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
package com.clougence.clouddm.dsfamily.db2.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.definition.ui.exception.AbstractDetermineExceptionSpi;

public class Db2DetermineExceptionSpi extends AbstractDetermineExceptionSpi {

    protected static final List<String> DB2_CONNECT_ERROR_MESSAGES;
    protected static final List<String> DB2_AUTH_ERROR_MESSAGES;

    static {
        DB2_CONNECT_ERROR_MESSAGES = new ArrayList<>(BASIC_CONNECT_ERROR_MESSAGES);
        DB2_CONNECT_ERROR_MESSAGES.add("nodename nor servname provided, or not known");
        DB2_CONNECT_ERROR_MESSAGES.add("The application requester cannot establish the connection. (Connection was dropped unexpectedly.)");
        DB2_CONNECT_ERROR_MESSAGES.add("ERRORCODE=-4499");
        DB2_CONNECT_ERROR_MESSAGES.add("ERRORCODE=-4222");

        DB2_AUTH_ERROR_MESSAGES = new ArrayList<>(BASIC_AUTH_ERROR_MESSAGES);
        DB2_AUTH_ERROR_MESSAGES.add("ERRORCODE=-4214, SQLSTATE=28000");
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return DB2_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return DB2_AUTH_ERROR_MESSAGES; }
}
