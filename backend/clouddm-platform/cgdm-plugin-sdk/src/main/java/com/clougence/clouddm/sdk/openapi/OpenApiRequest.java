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
package com.clougence.clouddm.sdk.openapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class OpenApiRequest {

    private final String method;
    /** The path starting at the OpenAPI prefix, without the query string. */
    private final String path;
    private final String requestId;
    /** The JSON body decoded as UTF-8 after the host verifies the original request bytes. */
    private final String body;
    /** The verified idempotency key, or {@code null} when absent. */
    private final String idempotencyKey;
}
