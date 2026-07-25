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
package com.clougence.sql.mysql.analysis.behavior;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.mysql.parser.MyDslProvider;
import com.clougence.sql.mysql.parser.MySqlParserConfig;
import com.clougence.sql.mysql.analysis.reference.MySqlResourceRegistry;
import com.clougence.utils.StringUtils;

public class MyBehaviorAnalysisSpi implements BehaviorAnalysisSpi {

    private final MyDslProvider         provider;
    private final MySqlResourceRegistry resources;

    public MyBehaviorAnalysisSpi(MySqlParserConfig config){
        this.provider = new MyDslProvider(config);
        this.resources = MySqlResourceRegistry.instance();
    }

    @Override
    public List<StatementBehavior> analysisBehavior(String query, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        if (StringUtils.isBlank(query)) {
            return Collections.emptyList();
        }

        MyBehaviorParserVisitor[] holder = new MyBehaviorParserVisitor[1];
        DslHelper.doVisitor(provider, query, (lexer, parser) -> {
            holder[0] = new MyBehaviorParserVisitor(parser, provider, levels, baseLine, baseColumn, resources);
            return holder[0];
        });
        return holder[0].behaviors();
    }
}
