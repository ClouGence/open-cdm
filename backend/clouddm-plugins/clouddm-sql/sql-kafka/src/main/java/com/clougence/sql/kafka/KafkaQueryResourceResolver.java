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
package com.clougence.sql.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.schema.umi.struts.UmiTypes;

public class KafkaQueryResourceResolver implements QueryResourceResolver {
    @Override
    public void resolve(QueryRequest request, Function<UmiTypes, List<String>> objectNames, Predicate<BehaviorRelation> permitted) {
        if (request.getResolvedResources() != null) {
            return;
        }
        KafkaCommand command = new KafkaCommandParser().parse(request.getQueryBody());
        if (command.getType() != KafkaCommandType.TOPICS || command.has("--create") || command.has("--help")) {
            return;
        }
        BehaviorRelation selector = request.getRelations().get(0);
        Pattern pattern = Pattern.compile(command.getOptions().getOrDefault("--topic", List.of(".*")).get(0));
        List<String> names = new ArrayList<>();
        List<BehaviorRelation> relations = new ArrayList<>();
        for (String name : objectNames.apply(UmiTypes.Topic).stream().sorted().toList()) {
            if (!pattern.matcher(name).matches()) {
                continue;
            }
            BehaviorObject source = selector.getSubject();
            BehaviorObject object = new BehaviorObject();
            object.setObjectType(TargetType.Topic);
            object.setObjectPath(source.getObjectPath() + name + "/");
            object.setObjectName(new ObjectName(null, null, name));
            object.setStartLine(source.getStartLine());
            object.setStartColumn(source.getStartColumn());
            object.setEndLine(source.getEndLine());
            object.setEndColumn(source.getEndColumn());
            BehaviorRelation relation = new BehaviorRelation();
            relation.setSubject(object);
            relation.setAction(selector.getAction());
            if (command.has("--list") && !permitted.test(relation)) {
                continue;
            }
            names.add(name);
            relations.add(relation);
        }
        request.setResolvedResources(Map.of(TargetType.Topic, List.copyOf(names)));
        request.setRelations(relations);
    }
}
