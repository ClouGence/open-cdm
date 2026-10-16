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
package com.clougence.sql.es;

/** Effective hit count; URL size takes precedence over the body, as in the ES REST API. */
public record EsSearchLimit(int size, EsSourceRange range, boolean parameter) {
    public static EsSearchLimit resolve(EsRequest request) {
        EsSearchLimit limit = new EsSearchLimit(10, null, false);
        if (request.body() != null) {
            EsJsonValue value = EsJsonAccess.object(request.body()).get("size");
            if (value != null) {
                if (value instanceof EsJsonNumber number) {
                    try {
                        int size = number.decimalValue().intValueExact();
                        if (size >= 0) {
                            limit = new EsSearchLimit(size, value.range(), false);
                        }
                    } catch (ArithmeticException | NumberFormatException ignored) {
                        // ES sizes are nonnegative integers.
                    }
                }
                if (limit.range() == null) {
                    throw new EsParseException(value.range(), EsSqlI18nKeys.ES_BODY_OUT_OF_SCOPE);
                }
            }
        }
        for (EsQueryParameter parameter : request.parameters()) {
            if (parameter.name().equals("size")) {
                try {
                    int size = Integer.parseInt(parameter.value());
                    if (size == -1) {
                        continue;
                    }
                    if (size >= 0) {
                        return new EsSearchLimit(size, parameter.range(), true);
                    }
                } catch (NumberFormatException ignored) {
                    // Report invalid sizes at their source location instead of silently fixing them.
                }
                throw new EsParseException(parameter.range(), EsSqlI18nKeys.ES_PARAMETER_OUT_OF_SCOPE, "size");
            }
        }
        return limit;
    }
}
