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
package com.clougence.clouddm.sdk.openapi.metadata;

import java.util.List;

import com.clougence.schema.umi.struts.UmiTypes;

/**
 * The namespace path is relative to the data source. Page numbers start at one.
 * Use {@link UmiTypes#Table}, {@link UmiTypes#View}, or {@code null} for both object types.
 */
public record OpenApiListTablesQuery(long dataSourceId, List<String> namespacePath, int pageNumber, int pageSize, UmiTypes objectType) {

    public OpenApiListTablesQuery{
        namespacePath = List.copyOf(namespacePath);
    }
}
