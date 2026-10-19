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
package com.clougence.clouddm.ds.etcd.sql;

import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.clougence.clouddm.ds.etcd.command.EtcdCommand;
import com.clougence.clouddm.ds.etcd.command.EtcdCommandParser;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.clouddm.sdk.sql.analysis.behavior.ObjectName;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.schema.umi.struts.UmiTypes;

public class EtcdBehaviorAnalysisSpi implements BehaviorAnalysisSpi {

    @Override
    public Stream<StatementBehavior> analysisBehaviorStream(Reader reader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        List<SplitScript> scripts = EtcdCommandParser.split(reader, List.of(), baseLine, baseColumn);
        return scripts.stream().map(script -> analyze(script, levels));
    }

    private StatementBehavior analyze(SplitScript script, Map<UmiTypes, Object> levels) {
        StatementBehavior behavior = new StatementBehavior();
        BehaviorObject object = new BehaviorObject();
        object.setStartLine(script.getBodyStartCodeLine());
        object.setStartColumn(script.getBodyStartCodeColumn());
        object.setEndLine(script.getBodyEndCodeLine());
        object.setEndColumn(script.getBodyEndCodeColumn());

        String instance = "";
        if (levels != null && levels.get(UmiTypes.Instance) != null) {
            instance = levels.get(UmiTypes.Instance).toString().replaceAll("^/+|/+$", "");
        }
        String path = "/";
        if (!instance.isEmpty()) {
            path = path + instance + "/";
        }

        EtcdCommand command = null;
        try {
            command = EtcdCommandParser.parse(script.getScript());
        } catch (ThirdPartyApiException ignored) {
            command = null;
        }
        if (command == null) {
            behavior.setStatementType(SplitQueryType.UNKNOWN);
            object.setObjectType(TargetType.Instance);
            object.setObjectPath(path);
            object.setObjectName(new ObjectName(null, null, instance));
            BehaviorRelation relation = new BehaviorRelation();
            relation.setSubject(object);
            relation.setAction(BehaviorAction.UNKNOWN);
            behavior.getRelations().add(relation);
            return behavior;
        }

        behavior.setStatementType(command.getKind().queryType());
        object.setObjectType(TargetType.Key);
        object.setObjectPath(path + command.getKey() + "/");
        object.setObjectName(new ObjectName(null, null, command.getKey()));
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(object);
        relation.setAction(command.getKind().action());
        behavior.getRelations().add(relation);
        return behavior;
    }
}
