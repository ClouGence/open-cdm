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
package com.clougence.clouddm.ds.hana.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.definition.ui.exception.AbstractDetermineExceptionSpi;
import com.clougence.clouddm.sdk.ui.exception.ConnectionExceptionType;

public class HanaDetermineExceptionSpi extends AbstractDetermineExceptionSpi {

    protected static final List<String> HANA_CONNECT_ERROR_MESSAGES;
    protected static final List<String> HANA_AUTH_ERROR_MESSAGES;

    static {
        HANA_CONNECT_ERROR_MESSAGES = new ArrayList<>(BASIC_CONNECT_ERROR_MESSAGES);
        HANA_CONNECT_ERROR_MESSAGES.add("UnresolvedAddressException");
        HANA_CONNECT_ERROR_MESSAGES.add("SAP DBTech JDBC: Cannot connect");

        HANA_AUTH_ERROR_MESSAGES = new ArrayList<>(BASIC_AUTH_ERROR_MESSAGES);
        HANA_AUTH_ERROR_MESSAGES.add("authentication failed");
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return HANA_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return HANA_AUTH_ERROR_MESSAGES; }

    @Override
    public ConnectionExceptionType checkExceptionType(Throwable error) {
        ConnectionExceptionType errorType = super.checkExceptionType(error);
        if (errorType != ConnectionExceptionType.Other) {
            return errorType;
        }
        return super.checkExceptionType(error.getCause());
    }
}
