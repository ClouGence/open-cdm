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
package com.clougence.clouddm.console.web.service.sdk;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.sdk.openapi.OpenApiCaller;
import com.clougence.clouddm.sdk.openapi.metadata.OpenApiListTablesQuery;
import com.clougence.clouddm.sdk.openapi.metadata.OpenApiMetadataService;
import com.clougence.clouddm.sdk.openapi.metadata.OpenApiTablePage;

@Service
public class ConsoleOpenApiMetadataService implements OpenApiMetadataService {

    @Override
    public OpenApiTablePage listTables(OpenApiCaller caller, OpenApiListTablesQuery query) {
        throw new UnsupportedOperationException("OpenAPI table listing is not available yet.");
    }
}
