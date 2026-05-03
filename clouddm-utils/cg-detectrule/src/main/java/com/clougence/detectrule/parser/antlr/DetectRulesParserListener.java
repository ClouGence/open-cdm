// Generated from DetectRulesParser.g4 by ANTLR 4.9.2
package com.clougence.detectrule.parser.antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DetectRulesParser}.
 */
public interface DetectRulesParserListener extends ParseTreeListener {

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#root}.
     * @param ctx the parse tree
     */
    void enterRoot(DetectRulesParser.RootContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#root}.
     * @param ctx the parse tree
     */
    void exitRoot(DetectRulesParser.RootContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#comment}.
     * @param ctx the parse tree
     */
    void enterComment(DetectRulesParser.CommentContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#comment}.
     * @param ctx the parse tree
     */
    void exitComment(DetectRulesParser.CommentContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#multiStatement}.
     * @param ctx the parse tree
     */
    void enterMultiStatement(DetectRulesParser.MultiStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#multiStatement}.
     * @param ctx the parse tree
     */
    void exitMultiStatement(DetectRulesParser.MultiStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#singleStatement}.
     * @param ctx the parse tree
     */
    void enterSingleStatement(DetectRulesParser.SingleStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#singleStatement}.
     * @param ctx the parse tree
     */
    void exitSingleStatement(DetectRulesParser.SingleStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#defineStatement}.
     * @param ctx the parse tree
     */
    void enterDefineStatement(DetectRulesParser.DefineStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#defineStatement}.
     * @param ctx the parse tree
     */
    void exitDefineStatement(DetectRulesParser.DefineStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#defineType}.
     * @param ctx the parse tree
     */
    void enterDefineType(DetectRulesParser.DefineTypeContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#defineType}.
     * @param ctx the parse tree
     */
    void exitDefineType(DetectRulesParser.DefineTypeContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#defineDefault}.
     * @param ctx the parse tree
     */
    void enterDefineDefault(DetectRulesParser.DefineDefaultContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#defineDefault}.
     * @param ctx the parse tree
     */
    void exitDefineDefault(DetectRulesParser.DefineDefaultContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#defineEnum}.
     * @param ctx the parse tree
     */
    void enterDefineEnum(DetectRulesParser.DefineEnumContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#defineEnum}.
     * @param ctx the parse tree
     */
    void exitDefineEnum(DetectRulesParser.DefineEnumContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#defineHINT}.
     * @param ctx the parse tree
     */
    void enterDefineHINT(DetectRulesParser.DefineHINTContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#defineHINT}.
     * @param ctx the parse tree
     */
    void exitDefineHINT(DetectRulesParser.DefineHINTContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#ifStatement}.
     * @param ctx the parse tree
     */
    void enterIfStatement(DetectRulesParser.IfStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#ifStatement}.
     * @param ctx the parse tree
     */
    void exitIfStatement(DetectRulesParser.IfStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#conditionThen}.
     * @param ctx the parse tree
     */
    void enterConditionThen(DetectRulesParser.ConditionThenContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#conditionThen}.
     * @param ctx the parse tree
     */
    void exitConditionThen(DetectRulesParser.ConditionThenContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#elseifThen}.
     * @param ctx the parse tree
     */
    void enterElseifThen(DetectRulesParser.ElseifThenContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#elseifThen}.
     * @param ctx the parse tree
     */
    void exitElseifThen(DetectRulesParser.ElseifThenContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#elseThen}.
     * @param ctx the parse tree
     */
    void enterElseThen(DetectRulesParser.ElseThenContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#elseThen}.
     * @param ctx the parse tree
     */
    void exitElseThen(DetectRulesParser.ElseThenContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#returnStatement}.
     * @param ctx the parse tree
     */
    void enterReturnStatement(DetectRulesParser.ReturnStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#returnStatement}.
     * @param ctx the parse tree
     */
    void exitReturnStatement(DetectRulesParser.ReturnStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#throwStatement}.
     * @param ctx the parse tree
     */
    void enterThrowStatement(DetectRulesParser.ThrowStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#throwStatement}.
     * @param ctx the parse tree
     */
    void exitThrowStatement(DetectRulesParser.ThrowStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#assertStatement}.
     * @param ctx the parse tree
     */
    void enterAssertStatement(DetectRulesParser.AssertStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#assertStatement}.
     * @param ctx the parse tree
     */
    void exitAssertStatement(DetectRulesParser.AssertStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#assertExpr}.
     * @param ctx the parse tree
     */
    void enterAssertExpr(DetectRulesParser.AssertExprContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#assertExpr}.
     * @param ctx the parse tree
     */
    void exitAssertExpr(DetectRulesParser.AssertExprContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#assignStatement}.
     * @param ctx the parse tree
     */
    void enterAssignStatement(DetectRulesParser.AssignStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#assignStatement}.
     * @param ctx the parse tree
     */
    void exitAssignStatement(DetectRulesParser.AssignStatementContext ctx);

    /**
     * Enter a parse tree produced by the {@code logicOrExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterLogicOrExpr(DetectRulesParser.LogicOrExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code logicOrExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitLogicOrExpr(DetectRulesParser.LogicOrExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code unaryNotPlusExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterUnaryNotPlusExpr(DetectRulesParser.UnaryNotPlusExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code unaryNotPlusExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitUnaryNotPlusExpr(DetectRulesParser.UnaryNotPlusExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code funcOrDetectExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterFuncOrDetectExpr(DetectRulesParser.FuncOrDetectExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code funcOrDetectExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitFuncOrDetectExpr(DetectRulesParser.FuncOrDetectExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code unaryBeforeDecrExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterUnaryBeforeDecrExpr(DetectRulesParser.UnaryBeforeDecrExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code unaryBeforeDecrExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitUnaryBeforeDecrExpr(DetectRulesParser.UnaryBeforeDecrExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code unaryAfterDecrExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterUnaryAfterDecrExpr(DetectRulesParser.UnaryAfterDecrExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code unaryAfterDecrExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitUnaryAfterDecrExpr(DetectRulesParser.UnaryAfterDecrExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code matchExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterMatchExpr(DetectRulesParser.MatchExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code matchExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitMatchExpr(DetectRulesParser.MatchExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code atomExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterAtomExpr(DetectRulesParser.AtomExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code atomExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitAtomExpr(DetectRulesParser.AtomExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code privilegeExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterPrivilegeExpr(DetectRulesParser.PrivilegeExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code privilegeExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitPrivilegeExpr(DetectRulesParser.PrivilegeExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code additiveExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterAdditiveExpr(DetectRulesParser.AdditiveExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code additiveExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitAdditiveExpr(DetectRulesParser.AdditiveExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code shiftExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterShiftExpr(DetectRulesParser.ShiftExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code shiftExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitShiftExpr(DetectRulesParser.ShiftExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code bitwiseExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterBitwiseExpr(DetectRulesParser.BitwiseExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code bitwiseExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitBitwiseExpr(DetectRulesParser.BitwiseExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code unaryExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterUnaryExpr(DetectRulesParser.UnaryExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code unaryExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitUnaryExpr(DetectRulesParser.UnaryExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code logicAndExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterLogicAndExpr(DetectRulesParser.LogicAndExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code logicAndExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitLogicAndExpr(DetectRulesParser.LogicAndExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code ternaryExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterTernaryExpr(DetectRulesParser.TernaryExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code ternaryExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitTernaryExpr(DetectRulesParser.TernaryExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code unaryBeforeIncreExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterUnaryBeforeIncreExpr(DetectRulesParser.UnaryBeforeIncreExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code unaryBeforeIncreExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitUnaryBeforeIncreExpr(DetectRulesParser.UnaryBeforeIncreExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code unaryAfterIncreExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterUnaryAfterIncreExpr(DetectRulesParser.UnaryAfterIncreExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code unaryAfterIncreExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitUnaryAfterIncreExpr(DetectRulesParser.UnaryAfterIncreExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code accessParamExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterAccessParamExpr(DetectRulesParser.AccessParamExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code accessParamExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitAccessParamExpr(DetectRulesParser.AccessParamExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code multiplicativeExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterMultiplicativeExpr(DetectRulesParser.MultiplicativeExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code multiplicativeExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitMultiplicativeExpr(DetectRulesParser.MultiplicativeExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code compareExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void enterCompareExpr(DetectRulesParser.CompareExprContext ctx);

    /**
     * Exit a parse tree produced by the {@code compareExpr}
     * labeled alternative in {@link DetectRulesParser#expression}.
     * @param ctx the parse tree
     */
    void exitCompareExpr(DetectRulesParser.CompareExprContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#unarySymbol}.
     * @param ctx the parse tree
     */
    void enterUnarySymbol(DetectRulesParser.UnarySymbolContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#unarySymbol}.
     * @param ctx the parse tree
     */
    void exitUnarySymbol(DetectRulesParser.UnarySymbolContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#multiplicativeSymbol}.
     * @param ctx the parse tree
     */
    void enterMultiplicativeSymbol(DetectRulesParser.MultiplicativeSymbolContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#multiplicativeSymbol}.
     * @param ctx the parse tree
     */
    void exitMultiplicativeSymbol(DetectRulesParser.MultiplicativeSymbolContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#additiveSymbol}.
     * @param ctx the parse tree
     */
    void enterAdditiveSymbol(DetectRulesParser.AdditiveSymbolContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#additiveSymbol}.
     * @param ctx the parse tree
     */
    void exitAdditiveSymbol(DetectRulesParser.AdditiveSymbolContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#bitwiseSymbol}.
     * @param ctx the parse tree
     */
    void enterBitwiseSymbol(DetectRulesParser.BitwiseSymbolContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#bitwiseSymbol}.
     * @param ctx the parse tree
     */
    void exitBitwiseSymbol(DetectRulesParser.BitwiseSymbolContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#shiftSymbol}.
     * @param ctx the parse tree
     */
    void enterShiftSymbol(DetectRulesParser.ShiftSymbolContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#shiftSymbol}.
     * @param ctx the parse tree
     */
    void exitShiftSymbol(DetectRulesParser.ShiftSymbolContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#compareSymbol}.
     * @param ctx the parse tree
     */
    void enterCompareSymbol(DetectRulesParser.CompareSymbolContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#compareSymbol}.
     * @param ctx the parse tree
     */
    void exitCompareSymbol(DetectRulesParser.CompareSymbolContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#matchSymbol}.
     * @param ctx the parse tree
     */
    void enterMatchSymbol(DetectRulesParser.MatchSymbolContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#matchSymbol}.
     * @param ctx the parse tree
     */
    void exitMatchSymbol(DetectRulesParser.MatchSymbolContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#paramExpr}.
     * @param ctx the parse tree
     */
    void enterParamExpr(DetectRulesParser.ParamExprContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#paramExpr}.
     * @param ctx the parse tree
     */
    void exitParamExpr(DetectRulesParser.ParamExprContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#funOrDetectExpr}.
     * @param ctx the parse tree
     */
    void enterFunOrDetectExpr(DetectRulesParser.FunOrDetectExprContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#funOrDetectExpr}.
     * @param ctx the parse tree
     */
    void exitFunOrDetectExpr(DetectRulesParser.FunOrDetectExprContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#detectPart}.
     * @param ctx the parse tree
     */
    void enterDetectPart(DetectRulesParser.DetectPartContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#detectPart}.
     * @param ctx the parse tree
     */
    void exitDetectPart(DetectRulesParser.DetectPartContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#funcPart}.
     * @param ctx the parse tree
     */
    void enterFuncPart(DetectRulesParser.FuncPartContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#funcPart}.
     * @param ctx the parse tree
     */
    void exitFuncPart(DetectRulesParser.FuncPartContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#funcArgPart}.
     * @param ctx the parse tree
     */
    void enterFuncArgPart(DetectRulesParser.FuncArgPartContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#funcArgPart}.
     * @param ctx the parse tree
     */
    void exitFuncArgPart(DetectRulesParser.FuncArgPartContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#identifyExpr}.
     * @param ctx the parse tree
     */
    void enterIdentifyExpr(DetectRulesParser.IdentifyExprContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#identifyExpr}.
     * @param ctx the parse tree
     */
    void exitIdentifyExpr(DetectRulesParser.IdentifyExprContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#subExpr}.
     * @param ctx the parse tree
     */
    void enterSubExpr(DetectRulesParser.SubExprContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#subExpr}.
     * @param ctx the parse tree
     */
    void exitSubExpr(DetectRulesParser.SubExprContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#identifyPart}.
     * @param ctx the parse tree
     */
    void enterIdentifyPart(DetectRulesParser.IdentifyPartContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#identifyPart}.
     * @param ctx the parse tree
     */
    void exitIdentifyPart(DetectRulesParser.IdentifyPartContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#identify}.
     * @param ctx the parse tree
     */
    void enterIdentify(DetectRulesParser.IdentifyContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#identify}.
     * @param ctx the parse tree
     */
    void exitIdentify(DetectRulesParser.IdentifyContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#anyPrimary}.
     * @param ctx the parse tree
     */
    void enterAnyPrimary(DetectRulesParser.AnyPrimaryContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#anyPrimary}.
     * @param ctx the parse tree
     */
    void exitAnyPrimary(DetectRulesParser.AnyPrimaryContext ctx);

    /**
     * Enter a parse tree produced by the {@code booleanValue}
     * labeled alternative in {@link DetectRulesParser#primitiveValue}.
     * @param ctx the parse tree
     */
    void enterBooleanValue(DetectRulesParser.BooleanValueContext ctx);

    /**
     * Exit a parse tree produced by the {@code booleanValue}
     * labeled alternative in {@link DetectRulesParser#primitiveValue}.
     * @param ctx the parse tree
     */
    void exitBooleanValue(DetectRulesParser.BooleanValueContext ctx);

    /**
     * Enter a parse tree produced by the {@code numberValue}
     * labeled alternative in {@link DetectRulesParser#primitiveValue}.
     * @param ctx the parse tree
     */
    void enterNumberValue(DetectRulesParser.NumberValueContext ctx);

    /**
     * Exit a parse tree produced by the {@code numberValue}
     * labeled alternative in {@link DetectRulesParser#primitiveValue}.
     * @param ctx the parse tree
     */
    void exitNumberValue(DetectRulesParser.NumberValueContext ctx);

    /**
     * Enter a parse tree produced by the {@code xobValue}
     * labeled alternative in {@link DetectRulesParser#primitiveValue}.
     * @param ctx the parse tree
     */
    void enterXobValue(DetectRulesParser.XobValueContext ctx);

    /**
     * Exit a parse tree produced by the {@code xobValue}
     * labeled alternative in {@link DetectRulesParser#primitiveValue}.
     * @param ctx the parse tree
     */
    void exitXobValue(DetectRulesParser.XobValueContext ctx);

    /**
     * Enter a parse tree produced by the {@code stringValue}
     * labeled alternative in {@link DetectRulesParser#primitiveValue}.
     * @param ctx the parse tree
     */
    void enterStringValue(DetectRulesParser.StringValueContext ctx);

    /**
     * Exit a parse tree produced by the {@code stringValue}
     * labeled alternative in {@link DetectRulesParser#primitiveValue}.
     * @param ctx the parse tree
     */
    void exitStringValue(DetectRulesParser.StringValueContext ctx);

    /**
     * Enter a parse tree produced by the {@code datetimeValue}
     * labeled alternative in {@link DetectRulesParser#primitiveValue}.
     * @param ctx the parse tree
     */
    void enterDatetimeValue(DetectRulesParser.DatetimeValueContext ctx);

    /**
     * Exit a parse tree produced by the {@code datetimeValue}
     * labeled alternative in {@link DetectRulesParser#primitiveValue}.
     * @param ctx the parse tree
     */
    void exitDatetimeValue(DetectRulesParser.DatetimeValueContext ctx);

    /**
     * Enter a parse tree produced by the {@code nullVaue}
     * labeled alternative in {@link DetectRulesParser#primitiveValue}.
     * @param ctx the parse tree
     */
    void enterNullVaue(DetectRulesParser.NullVaueContext ctx);

    /**
     * Exit a parse tree produced by the {@code nullVaue}
     * labeled alternative in {@link DetectRulesParser#primitiveValue}.
     * @param ctx the parse tree
     */
    void exitNullVaue(DetectRulesParser.NullVaueContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#signSymbol}.
     * @param ctx the parse tree
     */
    void enterSignSymbol(DetectRulesParser.SignSymbolContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#signSymbol}.
     * @param ctx the parse tree
     */
    void exitSignSymbol(DetectRulesParser.SignSymbolContext ctx);

    /**
     * Enter a parse tree produced by the {@code floatNumValuePart}
     * labeled alternative in {@link DetectRulesParser#numValue}.
     * @param ctx the parse tree
     */
    void enterFloatNumValuePart(DetectRulesParser.FloatNumValuePartContext ctx);

    /**
     * Exit a parse tree produced by the {@code floatNumValuePart}
     * labeled alternative in {@link DetectRulesParser#numValue}.
     * @param ctx the parse tree
     */
    void exitFloatNumValuePart(DetectRulesParser.FloatNumValuePartContext ctx);

    /**
     * Enter a parse tree produced by the {@code intNumValuePart}
     * labeled alternative in {@link DetectRulesParser#numValue}.
     * @param ctx the parse tree
     */
    void enterIntNumValuePart(DetectRulesParser.IntNumValuePartContext ctx);

    /**
     * Exit a parse tree produced by the {@code intNumValuePart}
     * labeled alternative in {@link DetectRulesParser#numValue}.
     * @param ctx the parse tree
     */
    void exitIntNumValuePart(DetectRulesParser.IntNumValuePartContext ctx);

    /**
     * Enter a parse tree produced by the {@code fmtDateTimeValuePart}
     * labeled alternative in {@link DetectRulesParser#dtValue}.
     * @param ctx the parse tree
     */
    void enterFmtDateTimeValuePart(DetectRulesParser.FmtDateTimeValuePartContext ctx);

    /**
     * Exit a parse tree produced by the {@code fmtDateTimeValuePart}
     * labeled alternative in {@link DetectRulesParser#dtValue}.
     * @param ctx the parse tree
     */
    void exitFmtDateTimeValuePart(DetectRulesParser.FmtDateTimeValuePartContext ctx);

    /**
     * Enter a parse tree produced by the {@code dateTimeValueValuePart}
     * labeled alternative in {@link DetectRulesParser#dtValue}.
     * @param ctx the parse tree
     */
    void enterDateTimeValueValuePart(DetectRulesParser.DateTimeValueValuePartContext ctx);

    /**
     * Exit a parse tree produced by the {@code dateTimeValueValuePart}
     * labeled alternative in {@link DetectRulesParser#dtValue}.
     * @param ctx the parse tree
     */
    void exitDateTimeValueValuePart(DetectRulesParser.DateTimeValueValuePartContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#collectionValue}.
     * @param ctx the parse tree
     */
    void enterCollectionValue(DetectRulesParser.CollectionValueContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#collectionValue}.
     * @param ctx the parse tree
     */
    void exitCollectionValue(DetectRulesParser.CollectionValueContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#listValue}.
     * @param ctx the parse tree
     */
    void enterListValue(DetectRulesParser.ListValueContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#listValue}.
     * @param ctx the parse tree
     */
    void exitListValue(DetectRulesParser.ListValueContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#objectValue}.
     * @param ctx the parse tree
     */
    void enterObjectValue(DetectRulesParser.ObjectValueContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#objectValue}.
     * @param ctx the parse tree
     */
    void exitObjectValue(DetectRulesParser.ObjectValueContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#objectItem}.
     * @param ctx the parse tree
     */
    void enterObjectItem(DetectRulesParser.ObjectItemContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#objectItem}.
     * @param ctx the parse tree
     */
    void exitObjectItem(DetectRulesParser.ObjectItemContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#castExpr}.
     * @param ctx the parse tree
     */
    void enterCastExpr(DetectRulesParser.CastExprContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#castExpr}.
     * @param ctx the parse tree
     */
    void exitCastExpr(DetectRulesParser.CastExprContext ctx);

    /**
     * Enter a parse tree produced by the {@code castNum}
     * labeled alternative in {@link DetectRulesParser#dataType}.
     * @param ctx the parse tree
     */
    void enterCastNum(DetectRulesParser.CastNumContext ctx);

    /**
     * Exit a parse tree produced by the {@code castNum}
     * labeled alternative in {@link DetectRulesParser#dataType}.
     * @param ctx the parse tree
     */
    void exitCastNum(DetectRulesParser.CastNumContext ctx);

    /**
     * Enter a parse tree produced by the {@code castOther}
     * labeled alternative in {@link DetectRulesParser#dataType}.
     * @param ctx the parse tree
     */
    void enterCastOther(DetectRulesParser.CastOtherContext ctx);

    /**
     * Exit a parse tree produced by the {@code castOther}
     * labeled alternative in {@link DetectRulesParser#dataType}.
     * @param ctx the parse tree
     */
    void exitCastOther(DetectRulesParser.CastOtherContext ctx);

    /**
     * Enter a parse tree produced by the {@code castDtFmt}
     * labeled alternative in {@link DetectRulesParser#dataType}.
     * @param ctx the parse tree
     */
    void enterCastDtFmt(DetectRulesParser.CastDtFmtContext ctx);

    /**
     * Exit a parse tree produced by the {@code castDtFmt}
     * labeled alternative in {@link DetectRulesParser#dataType}.
     * @param ctx the parse tree
     */
    void exitCastDtFmt(DetectRulesParser.CastDtFmtContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#numFmtValue}.
     * @param ctx the parse tree
     */
    void enterNumFmtValue(DetectRulesParser.NumFmtValueContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#numFmtValue}.
     * @param ctx the parse tree
     */
    void exitNumFmtValue(DetectRulesParser.NumFmtValueContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#numFmtRounding}.
     * @param ctx the parse tree
     */
    void enterNumFmtRounding(DetectRulesParser.NumFmtRoundingContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#numFmtRounding}.
     * @param ctx the parse tree
     */
    void exitNumFmtRounding(DetectRulesParser.NumFmtRoundingContext ctx);

    /**
     * Enter a parse tree produced by {@link DetectRulesParser#allIdentifierKeywords}.
     * @param ctx the parse tree
     */
    void enterAllIdentifierKeywords(DetectRulesParser.AllIdentifierKeywordsContext ctx);

    /**
     * Exit a parse tree produced by {@link DetectRulesParser#allIdentifierKeywords}.
     * @param ctx the parse tree
     */
    void exitAllIdentifierKeywords(DetectRulesParser.AllIdentifierKeywordsContext ctx);
}
