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
package com.clougence.clouddm.ds.tidb.analysis;

import static com.clougence.clouddm.ds.tidb.parser.antlr.TiDBParser.*;

import org.antlr.v4.runtime.Parser;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.MyBuilderFactory;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.enums.MyAttribute;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyTableDomain;

public class TiParserVisitor extends AbstractTiSQLParserVisitor {

    public TiParserVisitor(MyBuilderFactory builder, Parser parser){
        super(builder, parser);
    }

    @Override
    public Void visitTidbTableOptionTTL(TidbTableOptionTTLContext ctx) {
        if (ctx.parent instanceof AlterByTableOptionContext) {
            MyTableDomain myTableDomain = new MyTableDomain();
            builder.handleDomain(myTableDomain, DomainSource.ALTER_TABLE_ITEM);
        }
        return null;
    }

    @Override
    public Void visitCreateSequence(CreateSequenceContext ctx) {
        builder.enterCreateSequence();
        ctx.sequence_name().accept(this);
        builder.exitCreateSequence();
        return null;
    }

    @Override
    public Void visitDropSequence(DropSequenceContext ctx) {
        for (Sequence_nameContext sequenceNameContext : ctx.sequence_name()) {
            builder.enterDropSequence();
            sequenceNameContext.accept(this);
            builder.exitDropSequence();
        }
        return null;
    }

    @Override
    public Void visitSecquenceCount(SecquenceCountContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTiIntervalPartition(TiIntervalPartitionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSequence_name(Sequence_nameContext ctx) {
        builder.enterObjName();
        visitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitAutoIncrementColumnConstraint(AutoIncrementColumnConstraintContext ctx) {
        if (ctx.AUTO_INCREMENT() != null || ctx.AUTO_RANDOM() != null) {
            builder.addAttr(MyAttribute.AUTO_INCREMENT, true);
        }
        return null;
    }
}
