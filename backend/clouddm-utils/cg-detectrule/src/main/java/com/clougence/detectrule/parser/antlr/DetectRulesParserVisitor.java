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

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link DetectRulesParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface DetectRulesParserVisitor<T> extends ParseTreeVisitor<T> {

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#root}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitRoot(DetectRulesParser.RootContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#comment}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitComment(DetectRulesParser.CommentContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#multiStatement}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMultiStatement(DetectRulesParser.MultiStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#singleStatement}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSingleStatement(DetectRulesParser.SingleStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#defineStatement}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDefineStatement(DetectRulesParser.DefineStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#defineType}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDefineType(DetectRulesParser.DefineTypeContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#defineDefault}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDefineDefault(DetectRulesParser.DefineDefaultContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#defineEnum}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDefineEnum(DetectRulesParser.DefineEnumContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#defineHINT}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDefineHINT(DetectRulesParser.DefineHINTContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#ifStatement}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfStatement(DetectRulesParser.IfStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#conditionThen}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConditionThen(DetectRulesParser.ConditionThenContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#elseifThen}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitElseifThen(DetectRulesParser.ElseifThenContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#elseThen}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitElseThen(DetectRulesParser.ElseThenContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#returnStatement}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitReturnStatement(DetectRulesParser.ReturnStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#throwStatement}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitThrowStatement(DetectRulesParser.ThrowStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#assertStatement}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAssertStatement(DetectRulesParser.AssertStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#assertExpr}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAssertExpr(DetectRulesParser.AssertExprContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#assignStatement}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAssignStatement(DetectRulesParser.AssignStatementContext ctx);

    /**
     * Visit a parse tree produced by the {@code logicOrExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLogicOrExpr(DetectRulesParser.LogicOrExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code unaryNotPlusExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitUnaryNotPlusExpr(DetectRulesParser.UnaryNotPlusExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code funcOrDetectExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFuncOrDetectExpr(DetectRulesParser.FuncOrDetectExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code unaryBeforeDecrExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitUnaryBeforeDecrExpr(DetectRulesParser.UnaryBeforeDecrExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code unaryAfterDecrExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitUnaryAfterDecrExpr(DetectRulesParser.UnaryAfterDecrExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code matchExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMatchExpr(DetectRulesParser.MatchExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code atomExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAtomExpr(DetectRulesParser.AtomExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code privilegeExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPrivilegeExpr(DetectRulesParser.PrivilegeExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code additiveExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAdditiveExpr(DetectRulesParser.AdditiveExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code shiftExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitShiftExpr(DetectRulesParser.ShiftExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code bitwiseExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBitwiseExpr(DetectRulesParser.BitwiseExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code unaryExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitUnaryExpr(DetectRulesParser.UnaryExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code logicAndExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLogicAndExpr(DetectRulesParser.LogicAndExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code ternaryExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTernaryExpr(DetectRulesParser.TernaryExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code unaryBeforeIncreExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitUnaryBeforeIncreExpr(DetectRulesParser.UnaryBeforeIncreExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code unaryAfterIncreExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitUnaryAfterIncreExpr(DetectRulesParser.UnaryAfterIncreExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code accessParamExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAccessParamExpr(DetectRulesParser.AccessParamExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code multiplicativeExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMultiplicativeExpr(DetectRulesParser.MultiplicativeExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code compareExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCompareExpr(DetectRulesParser.CompareExprContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#unarySymbol}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitUnarySymbol(DetectRulesParser.UnarySymbolContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#multiplicativeSymbol}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMultiplicativeSymbol(DetectRulesParser.MultiplicativeSymbolContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#additiveSymbol}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAdditiveSymbol(DetectRulesParser.AdditiveSymbolContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#bitwiseSymbol}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBitwiseSymbol(DetectRulesParser.BitwiseSymbolContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#shiftSymbol}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitShiftSymbol(DetectRulesParser.ShiftSymbolContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#compareSymbol}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCompareSymbol(DetectRulesParser.CompareSymbolContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#matchSymbol}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMatchSymbol(DetectRulesParser.MatchSymbolContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#paramExpr}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitParamExpr(DetectRulesParser.ParamExprContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#funOrDetectExpr}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFunOrDetectExpr(DetectRulesParser.FunOrDetectExprContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#detectPart}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDetectPart(DetectRulesParser.DetectPartContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#funcPart}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFuncPart(DetectRulesParser.FuncPartContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#funcArgPart}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFuncArgPart(DetectRulesParser.FuncArgPartContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#identifyExpr}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIdentifyExpr(DetectRulesParser.IdentifyExprContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#subExpr}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSubExpr(DetectRulesParser.SubExprContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#identifyPart}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIdentifyPart(DetectRulesParser.IdentifyPartContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#identify}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIdentify(DetectRulesParser.IdentifyContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#anyPrimary}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAnyPrimary(DetectRulesParser.AnyPrimaryContext ctx);

    /**
     * Visit a parse tree produced by the {@code booleanValue}
     * labeled alternative in {@link DetectRulesParser#primitiveValue}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBooleanValue(DetectRulesParser.BooleanValueContext ctx);

    /**
     * Visit a parse tree produced by the {@code numberValue}
     * labeled alternative in {@link DetectRulesParser#primitiveValue}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNumberValue(DetectRulesParser.NumberValueContext ctx);

    /**
     * Visit a parse tree produced by the {@code xobValue}
     * labeled alternative in {@link DetectRulesParser#primitiveValue}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitXobValue(DetectRulesParser.XobValueContext ctx);

    /**
     * Visit a parse tree produced by the {@code stringValue}
     * labeled alternative in {@link DetectRulesParser#primitiveValue}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStringValue(DetectRulesParser.StringValueContext ctx);

    /**
     * Visit a parse tree produced by the {@code datetimeValue}
     * labeled alternative in {@link DetectRulesParser#primitiveValue}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDatetimeValue(DetectRulesParser.DatetimeValueContext ctx);

    /**
     * Visit a parse tree produced by the {@code nullVaue}
     * labeled alternative in {@link DetectRulesParser#primitiveValue}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNullVaue(DetectRulesParser.NullVaueContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#signSymbol}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSignSymbol(DetectRulesParser.SignSymbolContext ctx);

    /**
     * Visit a parse tree produced by the {@code floatNumValuePart}
     * labeled alternative in {@link DetectRulesParser#numValue}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFloatNumValuePart(DetectRulesParser.FloatNumValuePartContext ctx);

    /**
     * Visit a parse tree produced by the {@code intNumValuePart}
     * labeled alternative in {@link DetectRulesParser#numValue}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIntNumValuePart(DetectRulesParser.IntNumValuePartContext ctx);

    /**
     * Visit a parse tree produced by the {@code fmtDateTimeValuePart}
     * labeled alternative in {@link DetectRulesParser#dtValue}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFmtDateTimeValuePart(DetectRulesParser.FmtDateTimeValuePartContext ctx);

    /**
     * Visit a parse tree produced by the {@code dateTimeValueValuePart}
     * labeled alternative in {@link DetectRulesParser#dtValue}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDateTimeValueValuePart(DetectRulesParser.DateTimeValueValuePartContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#collectionValue}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCollectionValue(DetectRulesParser.CollectionValueContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#listValue}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitListValue(DetectRulesParser.ListValueContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#objectValue}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitObjectValue(DetectRulesParser.ObjectValueContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#objectItem}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitObjectItem(DetectRulesParser.ObjectItemContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#castExpr}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCastExpr(DetectRulesParser.CastExprContext ctx);

    /**
     * Visit a parse tree produced by the {@code castNum}
     * labeled alternative in {@link DetectRulesParser#dataType}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCastNum(DetectRulesParser.CastNumContext ctx);

    /**
     * Visit a parse tree produced by the {@code castOther}
     * labeled alternative in {@link DetectRulesParser#dataType}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCastOther(DetectRulesParser.CastOtherContext ctx);

    /**
     * Visit a parse tree produced by the {@code castDtFmt}
     * labeled alternative in {@link DetectRulesParser#dataType}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCastDtFmt(DetectRulesParser.CastDtFmtContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#numFmtValue}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNumFmtValue(DetectRulesParser.NumFmtValueContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#numFmtRounding}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNumFmtRounding(DetectRulesParser.NumFmtRoundingContext ctx);

    /**
     * Visit a parse tree produced by {@link DetectRulesParser#allIdentifierKeywords}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAllIdentifierKeywords(DetectRulesParser.AllIdentifierKeywordsContext ctx);
}
