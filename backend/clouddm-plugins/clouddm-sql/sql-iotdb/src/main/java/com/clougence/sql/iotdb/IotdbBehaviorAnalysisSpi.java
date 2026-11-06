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
package com.clougence.sql.iotdb;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.stream.Stream;

import org.antlr.v4.runtime.CharStreams;

import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.iotdb.antlr.IotdbParser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
final class IotdbBehaviorAnalysisSpi implements BehaviorAnalysisSpi {

    private final IotdbDslProvider provider;

    IotdbBehaviorAnalysisSpi(IotdbDslProvider provider){
        this.provider = provider;
    }

    @Override
    public Stream<StatementBehavior> analysisBehaviorStream(Reader reader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        try {
            var lexer = provider.createLexer(CharStreams.fromReader(new IotdbSqlReader(reader)));
            lexer.setLine(Math.max(1, baseLine));
            lexer.setCharPositionInLine(Math.max(0, baseColumn));
            var parser = (IotdbParser) provider.createParser(lexer);
            String database = levels == null ? null : (String) levels.get(UmiTypes.Schema);
            return provider.statements(parser).stream().map(statement -> new IotdbBehaviorVisitor(provider == IotdbDslProvider.TREE, database).analyze(statement));
        } catch (IOException e) {
            log.error("Read IoTDB SQL for behavior analysis failed", e);
            throw ThirdPartyApiException.as().with(e);
        }
    }
}
