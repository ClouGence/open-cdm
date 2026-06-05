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
package com.clougence.clouddm.ds.hana.analysis;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.analysis.split.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.utils.StringUtils;

public class HanaSplitAnalysisSpi implements SplitAnalysisSpi {

    @Override
    public List<SplitScript> splitScript(String script, List<QueryArg> args, int baseCodeLine, int baseCodeColumn) {
        if (StringUtils.isBlank(script)) {
            return Collections.emptyList();
        } else {
            return Arrays.stream(script.split(";")).filter(StringUtils::isNotBlank).map(s -> {
                SplitScript script1 = new SplitScript();
                script1.setScript(s);
                script1.setType(SecQueryType.UNKNOWN);
                return script1;
            }).collect(Collectors.toList());
        }
    }
}
