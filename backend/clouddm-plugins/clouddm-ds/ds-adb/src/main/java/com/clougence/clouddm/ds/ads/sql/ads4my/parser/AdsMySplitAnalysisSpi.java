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
package com.clougence.clouddm.ds.ads.sql.ads4my.parser;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import com.clougence.clouddm.ds.ads.sql.ads4my.parser.antlr.AdsMyParser;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.mysql.analysis.reference.MySqlResourceRegistry;

public class AdsMySplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    @Override
    protected DslProvider dslProvider() {
        return AdsMyDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return AdsMySplitVisitor.INSTANCE;
    }

    @Override
    protected SplitQueryType additionalType(ParseTree tree) {
        if (tree instanceof AdsMyParser.CommentColumnConstraintContext) {
            return SplitQueryType.COMMENT_COLUMN;
        }
        if (tree instanceof AdsMyParser.TableOptionCommentContext) {
            return SplitQueryType.COMMENT_TABLE;
        }

        if (!(tree instanceof AdsMyParser.UdfFunctionCallContext function)) {
            return null;
        }
        AdsMyParser.FullIdContext fullId = function.customFunctionName().fullId();
        String name = fullId.uid(fullId.uid().size() - 1).getText();
        return MySqlResourceRegistry.instance().isUserDefinedFunction(name, fullId.uid().size() > 1) ? SplitQueryType.CALL_PROG_OBJ : null;
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((AdsMyParser) parser).root();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof AdsMyParser.SqlStatementContext && context.getParent() instanceof AdsMyParser.SqlStatementsContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return ((AdsMyDslProvider) AdsMyDslProvider.INSTANCE).treeParser();
    }
}
