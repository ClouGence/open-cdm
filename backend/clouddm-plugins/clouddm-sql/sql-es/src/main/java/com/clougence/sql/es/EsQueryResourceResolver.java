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

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.schema.umi.struts.UmiTypes;

public class EsQueryResourceResolver implements QueryResourceResolver {
    @Override
    public void resolve(QueryRequest request, Function<UmiTypes, List<String>> objectNames, Predicate<BehaviorRelation> permitted) {
        List<BehaviorRelation> relations = request.getRelations();
        if (relations == null || relations.size() != 1) {
            throw ThirdPartyApiException.as().with(EsSqlI18nKeys.ES_RESOURCE_SCOPE_MISMATCH);
        }
        BehaviorRelation relation = relations.get(0);
        BehaviorObject subject = relation.getSubject();
        List<BehaviorObject> objects = new ArrayList<>();
        objects.add(subject);
        objects.addAll(relation.getTarget());

        // Behavior analysis supplies every reference; live metadata only confirms concrete index identities.
        Set<String> concrete = new HashSet<>(objectNames.apply(UmiTypes.Table));
        Set<String> names = new LinkedHashSet<>();
        for (BehaviorObject object : objects) {
            String name = object.getObjectName().getObjectName();
            names.add(name);
            if (relation.getAction() == BehaviorAction.CREATE && object == subject) {
                continue;
            }
            if (!concrete.contains(name)) {
                throw ThirdPartyApiException.as().with(EsSqlI18nKeys.ES_INDEX_NOT_CONCRETE, name);
            }
        }
        Map<TargetType, List<String>> resources = Map.of(TargetType.Table, List.copyOf(names));
        if (request.getResolvedResources() != null && !resources.equals(request.getResolvedResources())) {
            throw ThirdPartyApiException.as().with(EsSqlI18nKeys.ES_RESOURCE_SCOPE_MISMATCH);
        }
        request.setResolvedResources(resources);
    }
}
