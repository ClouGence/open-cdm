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
package com.clougence.clouddm.ds.dameng.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.analysis.split.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.utils.StringUtils;

public class DmSplitAnalysisSpi implements SplitAnalysisSpi {

    @Override
    public List<SplitScript> splitScript(String script, List<QueryArg> args, int baseCodeLine, int baseCodeColumn) {

        if (StringUtils.isBlank(script)) {
            return Collections.emptyList();
        } else {
            StringBuilder sb = fillQueryOffset(baseCodeLine, baseCodeColumn);
            String[] split = script.split(";");
            List<SplitScript> result = new ArrayList<>();
            for (String s : split) {
                if (StringUtils.isNotBlank(s)) {
                    SplitScript script1 = new SplitScript();
                    script1.setScript(sb.toString() + s);
                    script1.setType(SecQueryType.UNKNOWN);
                    result.add(script1);
                }
                fillBlankString(sb, s);
            }
            return result;

        }
    }

    private StringBuilder fillQueryOffset(int baseCodeLine, int baseCodeColumn) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < baseCodeLine; i++) {
            sb.append("\n");
        }
        for (int i = 1; i < baseCodeColumn; i++) {
            sb.append(" ");
        }
        return sb;
    }

    private void fillBlankString(StringBuilder sb, String s) {
        for (char c : s.toCharArray()) {
            if (c == '\n') {
                sb.append("\n");
            } else {
                sb.append(" ");
            }
        }
    }
}
