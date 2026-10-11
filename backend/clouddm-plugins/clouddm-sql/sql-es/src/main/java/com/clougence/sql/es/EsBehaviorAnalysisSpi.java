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

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.schema.umi.struts.UmiTypes;

public class EsBehaviorAnalysisSpi implements BehaviorAnalysisSpi {
    private final SqlParserParameters parameters;

    public EsBehaviorAnalysisSpi(SqlParserParameters parameters){
        this.parameters = parameters;
    }

    @Override
    public Stream<StatementBehavior> analysisBehaviorStream(Reader reader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        Stream<SplitScript> scripts = new EsSplitAnalysisSpi(parameters).splitScriptStream(reader, List.of(), baseLine, baseColumn);
        return scripts.map(script -> analyze(script, levels)).onClose(scripts::close);
    }

    private StatementBehavior analyze(SplitScript script, Map<UmiTypes, Object> levels) {
        EsRequest request = new EsRequestParser(parameters).parse(script.getScript());
        EsRequestAnalysis analysis = EsRequestPolicy.analyze(request);
        String instance = "";
        if (levels != null && levels.get(UmiTypes.Instance) != null) {
            instance = levels.get(UmiTypes.Instance).toString();
        }
        String prefix = "/" + instance.replaceAll("^/+|/+$", "");
        if (!prefix.endsWith("/")) {
            prefix += "/";
        }
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(object(analysis.index(), prefix, script));
        relation.setAction(analysis.operation().getAction());
        List<BehaviorObject> targets = new ArrayList<>();
        for (EsIndexReference reference : analysis.reads()) {
            targets.add(object(reference, prefix, script));
        }
        relation.setTarget(targets);
        StatementBehavior behavior = new StatementBehavior();
        behavior.setStatementType(analysis.operation().getQueryType());
        behavior.getRelations().add(relation);
        return behavior;
    }

    private BehaviorObject object(EsIndexReference reference, String prefix, SplitScript script) {
        BehaviorObject object = new BehaviorObject();
        object.setObjectType(TargetType.Table);
        object.setObjectName(new ObjectName(null, null, reference.name()));
        object.setObjectPath(prefix + reference.name() + "/");
        EsSourceRange range = reference.range();
        int startColumn = range.startColumn();
        int endColumn = range.endColumn();
        if (range.startLine() == 1) {
            startColumn += script.getBodyStartCodeColumn();
        }
        if (range.endLine() == 1) {
            endColumn += script.getBodyStartCodeColumn();
        }
        object.setStartLine(script.getBodyStartCodeLine() + range.startLine() - 1);
        object.setStartColumn(startColumn);
        object.setEndLine(script.getBodyStartCodeLine() + range.endLine() - 1);
        object.setEndColumn(endColumn);
        return object;
    }
}
