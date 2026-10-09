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

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.clouddm.sdk.sql.analysis.behavior.ObjectName;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
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
        BehaviorObject object = new BehaviorObject();
        object.setObjectType(TargetType.Topic);
        String name = command.value("--topic");
        if (command.getType() == KafkaCommandType.GROUPS) {
            object.setObjectType(TargetType.ConsumerGroup);
            name = command.value("--group");
        }
        if (command.has("--help")) {
            object.setObjectType(TargetType.Instance);
            name = null;
        }
        // Kafka currently grants at instance scope, including regex selectors and unnamed sets.
        // Keep the selector in the audit name, never interpolate it into an authorization path.
        String instance = "";
        if (levels != null && levels.get(UmiTypes.Instance) != null) {
            instance = levels.get(UmiTypes.Instance).toString();
        }
        instance = instance.replaceAll("^/+|/+$", "");
        String path = "/";
        if (!instance.isEmpty()) {
            path += instance + "/";
        }
        object.setObjectPath(path);
        object.setObjectName(new ObjectName(null, null, name));
        object.setStartLine(script.getBodyStartCodeLine());
        object.setStartColumn(script.getBodyStartCodeColumn());
        object.setEndLine(script.getBodyEndCodeLine());
        object.setEndColumn(script.getBodyEndCodeColumn());

        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(object);
        relation.setAction(BehaviorAction.READ);
        if (!command.has("--help")) {
            if (command.has("--create")) {
                relation.setAction(BehaviorAction.CREATE);
            } else if (command.has("--delete")) {
                relation.setAction(BehaviorAction.DROP);
            }
        }
        StatementBehavior behavior = new StatementBehavior();
        behavior.setStatementType(command.queryType());
        behavior.getRelations().add(relation);
        return behavior;
    }
}
