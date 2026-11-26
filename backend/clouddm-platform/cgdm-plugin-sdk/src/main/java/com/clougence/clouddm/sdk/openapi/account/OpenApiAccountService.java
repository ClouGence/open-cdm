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
package com.clougence.clouddm.sdk.openapi.account;

import java.util.List;
import java.util.function.Supplier;

import com.clougence.clouddm.sdk.openapi.OpenApiCaller;
import com.clougence.clouddm.sdk.service.Service;

public interface OpenApiAccountService extends Service {

    List<OpenApiSubAccount> listSubAccounts(OpenApiCaller caller, String prefix, Long roleId, Boolean enabled);

    OpenApiSubAccount getSubAccount(OpenApiCaller caller, String uid);

    List<OpenApiAssignableRole> listAssignableRoles(OpenApiCaller caller);

    Supplier<OpenApiSubAccount> prepareCreateSubAccount(OpenApiCaller caller, String account, String userName, long roleId, String password);

    Runnable prepareUpdateSubAccount(OpenApiCaller caller, String uid, String userName, String email, String phone);

    Runnable prepareSetSubAccountRole(OpenApiCaller caller, String uid, long roleId);

    Runnable prepareSetSubAccountEnabled(OpenApiCaller caller, String uid, boolean enabled);
}
