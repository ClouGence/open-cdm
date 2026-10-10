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
package com.clougence.clouddm.sdk.sql.analysis.behavior;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.schema.umi.struts.UmiTypes;

/** Resolves dynamic selectors before authorization and audit. Permissions are supplied by the console. */
public interface QueryResourceResolver {
    /**
     * Replace selector relations with concrete resources and pin the execution scope on the request.
     * Only enumeration may omit denied resources; mutations must retain every target for authorization.
     */
    void resolve(QueryRequest request, Function<UmiTypes, List<String>> objectNames, Predicate<BehaviorRelation> permitted);
}
