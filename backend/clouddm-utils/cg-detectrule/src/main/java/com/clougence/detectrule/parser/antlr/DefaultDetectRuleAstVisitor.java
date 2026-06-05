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
package com.clougence.detectrule.parser.antlr;

import com.clougence.detectrule.parser.ast.expr.*;
import com.clougence.detectrule.parser.ast.primary.*;
import com.clougence.detectrule.parser.ast.statement.*;
import com.clougence.dslpaser.ast.visitor.VisitorContext;

public class DefaultDetectRuleAstVisitor implements DetectRuleAstVisitor {

    @Override
    public void visitStatementList(VisitorContext<StatementList> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitDefineStatement(VisitorContext<DefineStatement> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitAssertStatement(VisitorContext<AssertStatement> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitReturnStatement(VisitorContext<ReturnStatement> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitSwitchStatement(VisitorContext<SwitchStatement> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitSwitchThenStatement(VisitorContext<SwitchThenStatement> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitSwitchElseStatement(VisitorContext<SwitchElseStatement> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitAssignStatement(VisitorContext<AssignStatement> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitThrowStatement(VisitorContext<ThrowStatement> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitParamExpression(VisitorContext<ParamExpression> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitDetectExpression(VisitorContext<DetectExpression> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitFunctionExpression(VisitorContext<FunctionExpression> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitFunctionParameters(VisitorContext<FunctionParameters> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitUnaryExpression(VisitorContext<UnaryExpression> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitDyadicExpression(VisitorContext<DyadicExpression> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitTernaryExpression(VisitorContext<TernaryExpression> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitPrivilegeExpression(VisitorContext<PrivilegeExpression> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitIdentifyNameExpression(VisitorContext<IdentifyNameExpression> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitIdentifyGroupExpression(VisitorContext<IdentifyGroupExpression> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitIdentifySubExpression(VisitorContext<IdentifySubExpression> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitBooleanValue(VisitorContext<BooleanValue> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitDataTimeValue(VisitorContext<DataTimeValue> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitNullValue(VisitorContext<NullValue> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitNumberValue(VisitorContext<NumberValue> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitStringValue(VisitorContext<StringValue> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitXxValue(VisitorContext<XxValue> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitListValue(VisitorContext<ListValue> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitObjectValue(VisitorContext<ObjectValue> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitKeyPairValue(VisitorContext<KeyPairValue> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitCastExpression(VisitorContext<CastExpression> visitCtx) {
        visitCtx.visitChildren(this);
    }

    @Override
    public void visitCastExpressionAsType(VisitorContext<CastExpressionAsType> visitCtx) {
        visitCtx.visitChildren(this);
    }
}
