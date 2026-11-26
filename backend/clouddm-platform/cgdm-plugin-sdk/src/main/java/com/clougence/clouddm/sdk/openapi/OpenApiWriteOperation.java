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

import java.util.function.Supplier;

@FunctionalInterface
public interface OpenApiWriteOperation {

    /**
     * Parses the request and checks the caller's current feature and resource permissions as a read-only step.
     * The host calls this on both the initial request and retries, before using any saved result.
     * Throw an {@link OpenApiException} for an invalid request or denied access.
     *
     * @return the write to run synchronously in the host's database transaction. Business preconditions,
     *         database changes and response construction belong in this supplier.
     */
    Supplier<OpenApiResponse> authorize(OpenApiRequest request, OpenApiCaller caller);
}
