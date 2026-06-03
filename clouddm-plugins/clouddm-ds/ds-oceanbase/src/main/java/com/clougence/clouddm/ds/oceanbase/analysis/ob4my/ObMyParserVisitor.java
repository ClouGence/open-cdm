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
package com.clougence.clouddm.ds.oceanbase.analysis.ob4my;

import org.antlr.v4.runtime.Parser;

import com.clougence.clouddm.ds.oceanbase.parser.ob4my.antlr.ObForMySqlParser;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.MyBuilderFactory;

public class ObMyParserVisitor extends AbstractObSQLParserVisitor {

    public ObMyParserVisitor(MyBuilderFactory builder, Parser parser){
        super(builder, parser);
    }

    @Override
    public Void visitTableOptionCompression(ObForMySqlParser.TableOptionCompressionContext ctx) {
        return null;
    }

    @Override
    public Void visitTableOptionRowFormat(ObForMySqlParser.TableOptionRowFormatContext ctx) {
        return null;
    }

    @Override
    public Void visitObMyTableOption(ObForMySqlParser.ObMyTableOptionContext ctx) {
        return null;
    }

    @Override
    public Void visitSelectIntoTextFile(ObForMySqlParser.SelectIntoTextFileContext ctx) {
        return null;
    }

}
