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

import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.schema.umi.struts.UmiTypes;

public class KafkaBehaviorAnalysisSpi implements BehaviorAnalysisSpi {
    @Override
    public Stream<StatementBehavior> analysisBehaviorStream(Reader reader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        Stream<SplitScript> scripts = new KafkaSplitAnalysisSpi().splitScriptStream(reader, List.of(), baseLine, baseColumn);
        return scripts.map(script -> analyze(script, levels)).onClose(scripts::close);
    }

    private StatementBehavior analyze(SplitScript script, Map<UmiTypes, Object> levels) {
        KafkaCommand command = new KafkaCommandParser().parse(script.getScript());
        SplitQueryType statementType = command.queryType();
        TargetType targetType = TargetType.Topic;
        String name = command.value("--topic");
        if (command.has("--help")) {
            targetType = TargetType.Instance;
            name = null;
        }
        // Topic creation uses instance permissions. Regex selectors
        // remain at instance scope until the resource resolver expands them.
        String instance = "";
        if (levels != null && levels.get(UmiTypes.Instance) != null) {
            instance = levels.get(UmiTypes.Instance).toString();
        }

        instance = instance.replaceAll("^/+|/+$", "");
        String instancePath = "/";
        if (!instance.isEmpty()) {
            instancePath += instance + "/";
        }

        String path = instancePath;
        if (statementType == SplitQueryType.LOG_READ || statementType == SplitQueryType.INSERT) {
            path += name + "/";
        }

        BehaviorAction action = switch (statementType) {
            case INSERT -> BehaviorAction.INSERT;
            case CREATE_PUB_SUB -> BehaviorAction.CREATE;
            case DROP_PUB_SUB -> BehaviorAction.DROP;
            default -> BehaviorAction.READ;
        };

        StatementBehavior behavior = new StatementBehavior();
        behavior.setStatementType(statementType);
        addRelation(behavior, script, targetType, name, path, action);
        if (statementType == SplitQueryType.LOG_READ && command.has("--group")) {
            // Joining can rebalance members of the same group across topics.
            addRelation(behavior, script, TargetType.Instance, null, instancePath, BehaviorAction.CONFIGURE);
        }
        return behavior;
    }

    private void addRelation(StatementBehavior behavior, SplitScript script, TargetType targetType, String name, String path, BehaviorAction action) {
        BehaviorObject object = new BehaviorObject();
        object.setObjectType(targetType);
        object.setObjectPath(path);
        object.setObjectName(new ObjectName(null, null, name));
        object.setStartLine(script.getBodyStartCodeLine());
        object.setStartColumn(script.getBodyStartCodeColumn());
        object.setEndLine(script.getBodyEndCodeLine());
        object.setEndColumn(script.getBodyEndCodeColumn());

        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(object);
        relation.setAction(action);
        behavior.getRelations().add(relation);
    }
}
