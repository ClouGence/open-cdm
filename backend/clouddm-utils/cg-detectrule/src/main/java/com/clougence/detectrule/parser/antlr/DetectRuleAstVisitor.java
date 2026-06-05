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
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;

public interface DetectRuleAstVisitor extends Visitor {

    // -------------------------------- for Statement

    void visitStatementList(VisitorContext<StatementList> visitCtx);

    void visitDefineStatement(VisitorContext<DefineStatement> visitCtx);

    void visitAssertStatement(VisitorContext<AssertStatement> visitCtx);

    void visitReturnStatement(VisitorContext<ReturnStatement> visitCtx);

    void visitThrowStatement(VisitorContext<ThrowStatement> visitCtx);

    void visitSwitchStatement(VisitorContext<SwitchStatement> visitCtx);

    void visitSwitchThenStatement(VisitorContext<SwitchThenStatement> visitCtx);

    void visitSwitchElseStatement(VisitorContext<SwitchElseStatement> visitCtx);

    void visitAssignStatement(VisitorContext<AssignStatement> visitCtx);

    // -------------------------------- for param/detect

    void visitParamExpression(VisitorContext<ParamExpression> visitCtx);

    void visitDetectExpression(VisitorContext<DetectExpression> visitCtx);

    // -------------------------------- for expr

    void visitUnaryExpression(VisitorContext<UnaryExpression> visitCtx);

    void visitDyadicExpression(VisitorContext<DyadicExpression> visitCtx);

    void visitTernaryExpression(VisitorContext<TernaryExpression> visitCtx);

    void visitPrivilegeExpression(VisitorContext<PrivilegeExpression> visitCtx);

    // -------------------------------- for evaluate

    void visitIdentifyNameExpression(VisitorContext<IdentifyNameExpression> visitCtx);

    void visitIdentifyGroupExpression(VisitorContext<IdentifyGroupExpression> visitCtx);

    void visitIdentifySubExpression(VisitorContext<IdentifySubExpression> visitCtx);

    void visitFunctionExpression(VisitorContext<FunctionExpression> visitCtx);

    void visitFunctionParameters(VisitorContext<FunctionParameters> visitCtx);

    // -------------------------------- for value

    void visitBooleanValue(VisitorContext<BooleanValue> visitCtx);

    void visitDataTimeValue(VisitorContext<DataTimeValue> visitCtx);

    void visitNullValue(VisitorContext<NullValue> visitCtx);

    void visitNumberValue(VisitorContext<NumberValue> visitCtx);

    void visitStringValue(VisitorContext<StringValue> visitCtx);

    void visitXxValue(VisitorContext<XxValue> visitCtx);

    void visitListValue(VisitorContext<ListValue> visitCtx);

    void visitObjectValue(VisitorContext<ObjectValue> visitCtx);

    void visitKeyPairValue(VisitorContext<KeyPairValue> visitCtx);

    void visitCastExpression(VisitorContext<CastExpression> visitCtx);

    void visitCastExpressionAsType(VisitorContext<CastExpressionAsType> visitCtx);
}
