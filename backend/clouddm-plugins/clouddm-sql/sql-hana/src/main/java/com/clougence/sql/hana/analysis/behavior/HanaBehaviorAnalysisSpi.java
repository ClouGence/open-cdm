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
package com.clougence.sql.hana.analysis.behavior;

import java.io.Reader;
import java.util.Map;
import java.util.stream.Stream;

import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.hana.analysis.HanaSqlAnalyzer;
import com.clougence.sql.hana.parser.HanaSplitAnalysisSpi;

public class HanaBehaviorAnalysisSpi implements BehaviorAnalysisSpi {
    private final MetaService metaService;

    public HanaBehaviorAnalysisSpi(){
        this(null);
    }

    public HanaBehaviorAnalysisSpi(MetaService metaService){
        this.metaService = metaService;
    }

    @Override
    public Stream<StatementBehavior> analysisBehaviorWithContextStream(Reader reader, ContextInfo context, int baseLine, int baseColumn) {
        if (metaService == null) {
            return analysisBehaviorStream(reader, context.getLevelsParam(), baseLine, baseColumn);
        }

        return new HanaSplitAnalysisSpi().splitScriptStream(reader, null, baseLine, baseColumn)
            .map(script -> HanaSqlAnalyzer
                .analyze(script, context.getLevelsParam(), (levels, index) -> metaService.fetchIndexedObject(context.getCuid(), context.getDsId(), levels, index))
                .getBehavior());
    }

    @Override
    public Stream<StatementBehavior> analysisBehaviorStream(Reader reader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        return new HanaSplitAnalysisSpi().splitScriptStream(reader, null, baseLine, baseColumn).map(script -> HanaSqlAnalyzer.analyze(script, levels).getBehavior());
    }
}
