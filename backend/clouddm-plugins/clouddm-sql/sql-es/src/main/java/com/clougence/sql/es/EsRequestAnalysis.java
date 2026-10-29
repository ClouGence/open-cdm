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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;

public record EsRequestAnalysis(EsOperation operation, EsIndexReference index, List<EsIndexReference> reads) {
    public EsRequestAnalysis{
        reads = List.copyOf(reads);
    }

    public List<String> indices() {
        LinkedHashSet<String> names = new LinkedHashSet<>();
        names.add(index.name());
        reads.forEach(reference -> names.add(reference.name()));
        return List.copyOf(names);
    }

    public void checkResolvedResources(QueryRequest request) {
        checkRelations(request);
        Map<TargetType, List<String>> resources = request.getResolvedResources();
        if (resources == null || resources.size() != 1 || !indices().equals(resources.get(TargetType.Table))) {
            throw new EsParseException(index.range(), EsSqlI18nKeys.ES_RESOURCE_SCOPE_MISMATCH);
        }
    }

    private void checkRelations(QueryRequest request) {
        List<BehaviorRelation> relations = request.getRelations();
        if (relations == null || relations.size() != 1) {
            throw new EsParseException(index().range(), EsSqlI18nKeys.ES_RESOURCE_SCOPE_MISMATCH);
        }
        BehaviorRelation relation = relations.get(0);
        BehaviorObject subject = relation.getSubject();
        if (subject == null || subject.getObjectName() == null || subject.getObjectType() != TargetType.Table || !index().name().equals(subject.getObjectName().getObjectName())
            || relation.getAction() != operation().getAction()) {
            throw new EsParseException(index().range(), EsSqlI18nKeys.ES_RESOURCE_SCOPE_MISMATCH);
        }
        List<String> expected = reads().stream().map(EsIndexReference::name).toList();
        if (relation.getTarget() == null || relation.getTarget().size() != expected.size()) {
            throw new EsParseException(index().range(), EsSqlI18nKeys.ES_RESOURCE_SCOPE_MISMATCH);
        }
        String suffix = index().name() + "/";
        String path = subject.getObjectPath();
        if (path == null || !path.startsWith("/") || !path.endsWith("/" + suffix)) {
            throw new EsParseException(index().range(), EsSqlI18nKeys.ES_RESOURCE_SCOPE_MISMATCH);
        }
        String prefix = path.substring(0, path.length() - suffix.length());
        for (int i = 0; i < expected.size(); i++) {
            BehaviorObject target = relation.getTarget().get(i);
            if (target == null || target.getObjectType() != TargetType.Table || target.getObjectName() == null || !expected.get(i).equals(target.getObjectName().getObjectName())
                || !(prefix + expected.get(i) + "/").equals(target.getObjectPath())) {
                throw new EsParseException(reads().get(i).range(), EsSqlI18nKeys.ES_RESOURCE_SCOPE_MISMATCH);
            }
        }
    }
}
