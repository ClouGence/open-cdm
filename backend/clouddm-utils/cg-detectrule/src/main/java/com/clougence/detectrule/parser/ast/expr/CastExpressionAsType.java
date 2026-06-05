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
package com.clougence.detectrule.parser.ast.expr;

import java.io.IOException;
import java.util.Objects;

import com.clougence.detectrule.parser.ast.Fragment;
import com.clougence.detectrule.parser.ast.token.CastType;
import com.clougence.detectrule.parser.ast.token.CastTypeDtFmtToken;
import com.clougence.detectrule.parser.ast.token.CastTypeNumFmtToken;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.ast.token.StringToken;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.ast.visitor.VisitorTree;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * cast as type
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class CastExpressionAsType extends AstFragment implements Fragment, VisitorTree {

    private StringToken         symbol;
    private CastType            castType;
    private CastTypeDtFmtToken  dtFmtToken;
    private CastTypeNumFmtToken numFmtToken;

    public CastExpressionAsType(StringToken symbol, CastType castType){
        this.symbol = symbol;
        this.castType = Objects.requireNonNull(castType, "castType is null.");
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitCastExpressionAsType(new VisitorContext<>(this));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        switch (this.castType) {
            case Bool:
            case String:
            case Date:
            case Time:
            case DateTime:
                this.symbol.doFormat(writer);
                break;
            case Float:
            case Decimal:
            case Integer:
                this.symbol.doFormat(writer);
                if (this.numFmtToken != null) {
                    this.numFmtToken.doFormat(writer);
                }
                break;
            case DateTimeFormat:
                this.dtFmtToken.doFormat(writer);
                break;
        }
    }
}
