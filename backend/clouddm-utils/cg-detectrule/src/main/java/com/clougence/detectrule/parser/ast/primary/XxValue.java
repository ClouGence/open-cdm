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
package com.clougence.detectrule.parser.ast.primary;

import java.io.IOException;

import com.clougence.detectrule.parser.ast.Value;
import com.clougence.detectrule.parser.format.CaseType;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.token.StringToken;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * Bit or Oct or Hex
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class XxValue extends StringToken implements Value {

    private CaseType symbolCaseType;
    private XxType   type;

    public XxValue(XxType type, String value){
        this(type, CaseType.LowerCase, value);
        this.type = type;
    }

    public XxValue(XxType type, CaseType symbolCaseType, String value){
        super("", value);
        this.symbolCaseType = symbolCaseType;
        this.type = type;
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitXxValue(new VisitorContext<>(this));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        switch (this.type) {
            case Hex:
                writer.write(this.symbolCaseType == CaseType.UpperCase ? "0X" : "0x");
                break;
            case Oct:
                writer.write(this.symbolCaseType == CaseType.UpperCase ? "0O" : "0o");
                break;
            case Bit:
                writer.write(this.symbolCaseType == CaseType.UpperCase ? "0B" : "0b");
                break;
            default:
                throw new IllegalStateException("can't happen.");
        }

        super.doFormat(writer);
    }
}
