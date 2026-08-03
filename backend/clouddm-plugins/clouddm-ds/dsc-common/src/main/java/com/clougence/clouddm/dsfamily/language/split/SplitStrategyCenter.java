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
package com.clougence.clouddm.dsfamily.language.split;

import java.util.List;

import com.clougence.clouddm.sdk.language.AbstractRequest;
import com.clougence.clouddm.sdk.language.LanguageResult;
import com.clougence.clouddm.sdk.language.split.SplitRequest;
import com.clougence.clouddm.sdk.language.split.SplitResult;
import com.clougence.clouddm.sdk.language.split.SplitSqlStatement;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.ast.location.BlockLocation;
import com.clougence.dslpaser.ast.location.CodeLocation;
import com.clougence.utils.StringUtils;

public class SplitStrategyCenter {

    public SplitResult split(SplitRequest request) {
        SplitResult result = initResult(request, new SplitResult());
        if (request == null || StringUtils.isBlank(request.getSqlText())) {
            return result;
        }

        List<SplitScript> scripts;
        try {
            SplitAnalysisSpi splitSpi = request.getSqlEngine().splitAnalysisSpi(new SqlParserParameters(request.getSqlParameters()));
            scripts = splitSpi.splitScript(request.getSqlText(), null, request.getBasicCodeLine(), request.getBasicCodeColumn());
        } catch (RuntimeException e) {
            return result;
        }

        for (SplitScript script : scripts) {
            SplitSqlStatement statement = new SplitSqlStatement();
            statement.setSql(script.getScript());
            statement.setRange(toRange(script));
            result.getStatements().add(statement);
        }
        return result;
    }

    private BlockLocation toRange(SplitScript script) {
        BlockLocation range = new BlockLocation();
        range.setStartPosition(new CodeLocation(script.getBodyStartCodeLine(), script.getBodyStartCodeColumn()));
        range.setEndPosition(new CodeLocation(script.getBodyEndCodeLine(), script.getBodyEndCodeColumn()));
        return range;
    }

    private static <T extends LanguageResult> T initResult(AbstractRequest request, T result) {
        if (request != null) {
            result.setRequestId(request.getRequestId());
            result.setRequestVersion(request.getRequestVersion());
        }
        return result;
    }
}
