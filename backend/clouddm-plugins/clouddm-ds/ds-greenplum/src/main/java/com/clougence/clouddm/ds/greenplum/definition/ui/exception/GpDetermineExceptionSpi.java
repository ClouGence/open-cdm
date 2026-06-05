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
package com.clougence.clouddm.ds.greenplum.definition.ui.exception;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.postgres.definition.ui.exception.PgDetermineExceptionSpi;

public class GpDetermineExceptionSpi extends PgDetermineExceptionSpi {

    protected static final List<String> GP_CONNECT_ERROR_MESSAGES;
    protected static final List<String> GP_AUTH_ERROR_MESSAGES;

    static {
        GP_CONNECT_ERROR_MESSAGES = new ArrayList<>(FAMILY_CONNECT_ERROR_MESSAGES);
        GP_CONNECT_ERROR_MESSAGES.add("尝试连线已失败");

        GP_AUTH_ERROR_MESSAGES = new ArrayList<>(FAMILY_AUTH_ERROR_MESSAGES);
        GP_AUTH_ERROR_MESSAGES.add("FATAL: role");
    }

    @Override
    protected List<String> getConnectionExceptionMessage() { return GP_CONNECT_ERROR_MESSAGES; }

    @Override
    protected List<String> getAuthenticationExceptionMessage() { return GP_AUTH_ERROR_MESSAGES; }
}
