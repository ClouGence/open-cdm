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

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.clougence.detectrule.parser.DetectRulesFeature;
import com.clougence.detectrule.parser.antlr.DetectRulesParser.*;
import com.clougence.detectrule.parser.ast.Expression;
import com.clougence.detectrule.parser.ast.RuleStatement;
import com.clougence.detectrule.parser.ast.expr.*;
import com.clougence.detectrule.parser.ast.primary.*;
import com.clougence.detectrule.parser.ast.statement.*;
import com.clougence.detectrule.parser.ast.token.*;
import com.clougence.detectrule.parser.format.CaseType;
import com.clougence.dslpaser.antlr.AbstractLocationParseTreeVisitor;
import com.clougence.dslpaser.ast.token.StringToken;
import com.clougence.dslpaser.ast.token.SymbolToken;
import com.clougence.utils.StringUtils;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link DetectRulesParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public class DefaultDetectRulesVisitor<T> extends AbstractLocationParseTreeVisitor<T> implements DetectRulesParserVisitor<T> {

    private final List<DetectRulesFeature> features;
    private final Stack<Object>            instStack = new Stack<>();

    public DefaultDetectRulesVisitor(List<DetectRulesFeature> features){
        super();
        this.features = features;
    }

    /* ----------------------------------------------------------------------------------- statements */

    @Override
    public T visitRoot(RootContext ctx) {
        StatementList statementList = code(new StatementList(), ctx);

        List<DefineStatementContext> defineCtxList = ctx.defineStatement();
        if (defineCtxList != null) {
            for (DefineStatementContext defineCtx : defineCtxList) {
                defineCtx.accept(this);
                DefineStatement statement = (DefineStatement) this.instStack.pop();
                statementList.add(statement);
            }
        }

        this.instStack.push(statementList);
        MultiStatementContext multiCtx = ctx.multiStatement();
        if (multiCtx != null) {
            multiCtx.accept(this);
        }

        return (T) this.instStack.pop();
    }

    @Override
    public T visitMultiStatement(MultiStatementContext ctx) {
        List<SingleStatementContext> statementCtxList = ctx.singleStatement();

        if (statementCtxList != null) {
            for (SingleStatementContext statementCtx : statementCtxList) {
                statementCtx.accept(this);
                RuleStatement statement = (RuleStatement) this.instStack.pop();
                StatementList statementSet = (StatementList) this.instStack.peek();
                statementSet.add(statement);
            }
        }

        return null;
    }

    @Override
    public T visitSingleStatement(SingleStatementContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitDefineStatement(DefineStatementContext ctx) {
        TerminalNode defineNameCtx = ctx.PRI_STR();
        StringToken defineName = fixString(defineNameCtx);
        DefineStatement defineStatement = new DefineStatement(defineName);

        DefineTypeContext defineTypeCtx = ctx.defineType();
        if (defineTypeCtx != null) {
            defineTypeCtx.accept(this);
            defineStatement.setType((StringToken) this.instStack.pop());
        }

        DefineDefaultContext defaultCtx = ctx.defineDefault();
        if (defaultCtx != null) {
            defineStatement.setDefaultValue(fixString(defaultCtx.PRI_STR()));
        }

        DefineEnumContext enumCtx = ctx.defineEnum();
        if (enumCtx != null) {
            List<TerminalNode> enumCtxList = enumCtx.PRI_STR();
            List<StringToken> enumList = new ArrayList<>();
            for (TerminalNode enumItem : enumCtxList) {
                enumList.add(fixString(enumItem));
            }
            defineStatement.setEnums(enumList);
        }

        DefineHINTContext hintCtx = ctx.defineHINT();
        if (hintCtx != null) {
            defineStatement.setHint(fixString(hintCtx.PRI_STR()));
        }

        this.instStack.push(code(defineStatement, ctx));
        return null;
    }

    @Override
    public T visitDefineType(DefineTypeContext ctx) {
        TerminalNode intSymbolCtx = ctx.INTEGER();
        TerminalNode floatSymbolCtx = ctx.FLOAT();
        TerminalNode decSymbolCtx = ctx.DECIMAL();
        TerminalNode boolSymbolCtx = ctx.BOOL();
        TerminalNode strSymbolCtx = ctx.STRING();
        TerminalNode dateSymbolCtx = ctx.DATE();
        TerminalNode timeSymbolCtx = ctx.TIME();
        TerminalNode datetimeSymbolCtx = ctx.DATETIME();

        if (intSymbolCtx != null) {
            this.instStack.push(code(this.asStringToken(intSymbolCtx), intSymbolCtx));
        } else if (floatSymbolCtx != null) {
            this.instStack.push(code(this.asStringToken(floatSymbolCtx), floatSymbolCtx));
        } else if (decSymbolCtx != null) {
            this.instStack.push(code(this.asStringToken(decSymbolCtx), decSymbolCtx));
        } else if (boolSymbolCtx != null) {
            this.instStack.push(code(this.asStringToken(boolSymbolCtx), boolSymbolCtx));
        } else if (strSymbolCtx != null) {
            this.instStack.push(code(this.asStringToken(strSymbolCtx), strSymbolCtx));
        } else if (dateSymbolCtx != null) {
            this.instStack.push(code(this.asStringToken(dateSymbolCtx), dateSymbolCtx));
        } else if (timeSymbolCtx != null) {
            this.instStack.push(code(this.asStringToken(timeSymbolCtx), timeSymbolCtx));
        } else if (datetimeSymbolCtx != null) {
            this.instStack.push(code(this.asStringToken(datetimeSymbolCtx), datetimeSymbolCtx));
        } else {
            throw new IllegalStateException("can't happen.");
        }

        return null;
    }

    @Override
    public T visitDefineDefault(DefineDefaultContext ctx) {
        return null; // do nothing
    }

    @Override
    public T visitDefineEnum(DefineEnumContext ctx) {
        return null; // do nothing
    }

    @Override
    public T visitDefineHINT(DefineHINTContext ctx) {
        return null; // do nothing
    }

    @Override
    public T visitIfStatement(IfStatementContext ctx) {
        SwitchStatement switchInst = code(new SwitchStatement(), ctx);

        // if ...
        ConditionThenContext conditionCtx = ctx.conditionThen();
        TerminalNode start = conditionCtx.IF();
        TerminalNode end = conditionCtx.THEN();
        ExpressionContext testExprCtx = conditionCtx.expression();

        testExprCtx.accept(this);
        Expression testExpr = (Expression) this.instStack.pop();

        MultiStatementContext statementCtx = conditionCtx.multiStatement();
        if (statementCtx != null) {
            this.instStack.push(code(new SwitchThenStatement(testExpr), statementCtx));
            statementCtx.accept(this);
        } else {
            this.instStack.push(code(new SwitchThenStatement(testExpr), start, end));
        }

        switchInst.addIF((SwitchThenStatement) this.instStack.pop());

        // elseif ...
        List<ElseifThenContext> elseConditionCtxList = ctx.elseifThen();
        for (ElseifThenContext elseConditionCtx : elseConditionCtxList) {
            ExpressionContext elseTestExprCtx = elseConditionCtx.expression();
            TerminalNode elseStart = elseConditionCtx.ELSEIF();
            TerminalNode elseEnd = elseConditionCtx.THEN();

            // if expr then
            elseTestExprCtx.accept(this);
            Expression elseIfExpr = (Expression) this.instStack.pop();

            // body
            MultiStatementContext elseStatementsCtx = elseConditionCtx.multiStatement();
            if (elseStatementsCtx != null) {
                this.instStack.push(code(new SwitchThenStatement(elseIfExpr), elseStatementsCtx));
                elseStatementsCtx.accept(this);
            } else {
                this.instStack.push(code(new SwitchThenStatement(elseIfExpr), elseStart, elseEnd));
            }

            switchInst.addIF((SwitchThenStatement) this.instStack.pop());
        }

        // else
        ElseThenContext elseCtx = ctx.elseThen();
        if (elseCtx != null) {
            TerminalNode elseStart = elseCtx.ELSE();
            TerminalNode elseEnd = ctx.END();
            MultiStatementContext elseStatementsCtx = elseCtx.multiStatement();
            if (elseStatementsCtx != null) {
                this.instStack.push(code(new SwitchElseStatement(), elseStatementsCtx));
                elseStatementsCtx.accept(this);
            } else {
                this.instStack.push(code(new SwitchElseStatement(), elseStart, elseEnd));
            }

            switchInst.setElse((SwitchElseStatement) this.instStack.pop());
        }

        this.instStack.push(switchInst);
        return null;
    }

    @Override
    public T visitConditionThen(ConditionThenContext ctx) {
        return null; // do nothing
    }

    @Override
    public T visitElseifThen(ElseifThenContext ctx) {
        return null; // do nothing
    }

    @Override
    public T visitElseThen(ElseThenContext ctx) {
        return null; // do nothing
    }

    @Override
    public T visitReturnStatement(ReturnStatementContext ctx) {
        ExpressionContext exprCtx = ctx.expression();
        if (exprCtx == null) {
            this.instStack.push(code(new ReturnStatement(null), ctx.RETURN()));
        } else {
            exprCtx.accept(this);
            Expression expr = (Expression) this.instStack.pop();
            this.instStack.push(code(new ReturnStatement(expr), exprCtx));
        }
        return null;
    }

    @Override
    public T visitThrowStatement(ThrowStatementContext ctx) {
        ExpressionContext exprCtx = ctx.expression();
        exprCtx.accept(this);
        Expression expr = (Expression) this.instStack.pop();
        this.instStack.push(code(new ThrowStatement(expr), exprCtx));
        return null;
    }

    @Override
    public T visitAssertStatement(AssertStatementContext ctx) {
        ExpressionContext testExprCtx = ctx.expression();
        AssertExprContext dataExprCtx = ctx.assertExpr();

        testExprCtx.accept(this);
        Expression testExpr = (Expression) this.instStack.pop();
        Expression dataExpr = null;

        if (dataExprCtx != null) {
            dataExprCtx.expression().accept(this);
            dataExpr = (Expression) this.instStack.pop();
        }

        this.instStack.push(code(new AssertStatement(testExpr, dataExpr), ctx));
        return null;
    }

    @Override
    public T visitAssertExpr(AssertExprContext ctx) {
        return null; // do nothing
    }

    @Override
    public T visitAssignStatement(AssignStatementContext ctx) {
        TerminalNode idNode = ctx.IDENTIFIER();
        StringToken idToken = code(new StringToken("", idNode.getText()), idNode);

        ctx.expression().accept(this);
        Expression varExpr = (Expression) this.instStack.pop();

        this.instStack.push(code(new AssignStatement(idToken, varExpr), ctx));

        return null;
    }

    /* ----------------------------------------------------------------------------------- expression */

    @Override
    public T visitAtomExpr(AtomExprContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitAccessParamExpr(AccessParamExprContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitPrivilegeExpr(PrivilegeExprContext ctx) {
        ExpressionContext exprCtx = ctx.expression();
        exprCtx.accept(this);

        Expression testExpr = (Expression) this.instStack.pop();
        this.instStack.push(code(new PrivilegeExpression(testExpr), ctx));

        return null;
    }

    @Override
    public T visitUnaryBeforeIncreExpr(UnaryBeforeIncreExprContext ctx) {
        ExpressionContext exprCtx = ctx.expression();
        exprCtx.accept(this);

        Expression testExpr = (Expression) this.instStack.pop();
        SymbolToken symbol = fixSymbolToken(ctx.AO_INCR());

        if (testExpr instanceof UnaryExpression) {
            throw new IllegalStateException("syntaxError continuous unary symbol at " + symbol.toLineRange());
        }

        this.instStack.push(code(new UnaryExpression(symbol, SymbolSide.Left, testExpr), ctx));
        return null;
    }

    @Override
    public T visitUnaryBeforeDecrExpr(UnaryBeforeDecrExprContext ctx) {
        ExpressionContext exprCtx = ctx.expression();
        exprCtx.accept(this);

        Expression testExpr = (Expression) this.instStack.pop();
        SymbolToken symbol = fixSymbolToken(ctx.AO_DECR());

        if (testExpr instanceof UnaryExpression) {
            throw new IllegalStateException("syntaxError continuous unary symbol at " + symbol.toLineRange());
        }

        this.instStack.push(code(new UnaryExpression(symbol, SymbolSide.Left, testExpr), ctx));
        return null;
    }

    @Override
    public T visitUnaryAfterIncreExpr(UnaryAfterIncreExprContext ctx) {
        ExpressionContext exprCtx = ctx.expression();
        exprCtx.accept(this);

        Expression testExpr = (Expression) this.instStack.pop();
        SymbolToken symbol = fixSymbolToken(ctx.AO_INCR());

        if (testExpr instanceof UnaryExpression) {
            throw new IllegalStateException("syntaxError continuous unary symbol at " + symbol.toLineRange());
        }

        this.instStack.push(code(new UnaryExpression(symbol, SymbolSide.Right, testExpr), ctx));
        return null;
    }

    @Override
    public T visitUnaryAfterDecrExpr(UnaryAfterDecrExprContext ctx) {
        ExpressionContext exprCtx = ctx.expression();
        exprCtx.accept(this);

        Expression testExpr = (Expression) this.instStack.pop();
        SymbolToken symbol = fixSymbolToken(ctx.AO_DECR());

        if (testExpr instanceof UnaryExpression) {
            throw new IllegalStateException("syntaxError continuous unary symbol at " + symbol.toLineRange());
        }

        this.instStack.push(code(new UnaryExpression(symbol, SymbolSide.Right, testExpr), ctx));
        return null;
    }

    @Override
    public T visitUnaryNotPlusExpr(UnaryNotPlusExprContext ctx) {
        ExpressionContext exprCtx = ctx.expression();
        exprCtx.accept(this);

        TerminalNode usingNotCtx;
        TerminalNode loNotCtx = ctx.LO_NOT();
        TerminalNode boNotCtx = ctx.BO_NOT();
        if (loNotCtx != null) {
            usingNotCtx = loNotCtx;
        } else if (boNotCtx != null) {
            usingNotCtx = boNotCtx;
        } else {
            throw new IllegalStateException("can't happen.");
        }

        Expression testExpr = (Expression) this.instStack.pop();
        SymbolToken symbolStr = fixSymbolToken(usingNotCtx);

        if (testExpr instanceof UnaryExpression) {
            throw new IllegalStateException("syntaxError continuous unary symbol at " + symbolStr.toLineRange());
        }

        this.instStack.push(code(new UnaryExpression(symbolStr, SymbolSide.Left, testExpr), ctx));
        return null;
    }

    @Override
    public T visitFuncOrDetectExpr(FuncOrDetectExprContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitUnaryExpr(UnaryExprContext ctx) {
        ExpressionContext exprCtx = ctx.expression();
        exprCtx.accept(this);

        Expression testExpr = (Expression) this.instStack.pop();

        this.visitUnarySymbol(ctx.unarySymbol());
        SymbolToken symbolStr = (SymbolToken) this.instStack.pop();

        if (testExpr instanceof UnaryExpression) {
            throw new IllegalStateException("syntaxError continuous unary symbol at " + symbolStr.toLineRange());
        }

        this.instStack.push(code(new UnaryExpression(symbolStr, SymbolSide.Left, testExpr), ctx));
        return null;
    }

    @Override
    public T visitMultiplicativeExpr(MultiplicativeExprContext ctx) {
        this.visitMultiplicativeSymbol(ctx.multiplicativeSymbol());
        ctx.expression(0).accept(this);
        ctx.expression(1).accept(this);

        Expression secondExpr = (Expression) this.instStack.pop();
        Expression firstExpr = (Expression) this.instStack.pop();
        SymbolToken symbol = (SymbolToken) this.instStack.pop();

        this.instStack.push(code(new DyadicExpression(firstExpr, symbol, secondExpr), ctx));

        return null;
    }

    @Override
    public T visitAdditiveExpr(AdditiveExprContext ctx) {
        this.visitAdditiveSymbol(ctx.additiveSymbol());
        ctx.expression(0).accept(this);
        ctx.expression(1).accept(this);

        Expression secondExpr = (Expression) this.instStack.pop();
        Expression firstExpr = (Expression) this.instStack.pop();
        SymbolToken symbol = (SymbolToken) this.instStack.pop();

        this.instStack.push(code(new DyadicExpression(firstExpr, symbol, secondExpr), ctx));

        return null;
    }

    @Override
    public T visitBitwiseExpr(BitwiseExprContext ctx) {
        this.visitBitwiseSymbol(ctx.bitwiseSymbol());
        ctx.expression(0).accept(this);
        ctx.expression(1).accept(this);

        Expression secondExpr = (Expression) this.instStack.pop();
        Expression firstExpr = (Expression) this.instStack.pop();
        SymbolToken symbol = (SymbolToken) this.instStack.pop();

        this.instStack.push(code(new DyadicExpression(firstExpr, symbol, secondExpr), ctx));

        return null;
    }

    @Override
    public T visitShiftExpr(ShiftExprContext ctx) {
        this.visitShiftSymbol(ctx.shiftSymbol());
        ctx.expression(0).accept(this);
        ctx.expression(1).accept(this);

        Expression secondExpr = (Expression) this.instStack.pop();
        Expression firstExpr = (Expression) this.instStack.pop();
        SymbolToken symbol = (SymbolToken) this.instStack.pop();

        this.instStack.push(code(new DyadicExpression(firstExpr, symbol, secondExpr), ctx));

        return null;
    }

    @Override
    public T visitCompareExpr(CompareExprContext ctx) {
        this.visitCompareSymbol(ctx.compareSymbol());
        ctx.expression(0).accept(this);
        ctx.expression(1).accept(this);

        Expression secondExpr = (Expression) this.instStack.pop();
        Expression firstExpr = (Expression) this.instStack.pop();
        SymbolToken symbol = (SymbolToken) this.instStack.pop();

        this.instStack.push(code(new DyadicExpression(firstExpr, symbol, secondExpr), ctx));

        return null;
    }

    @Override
    public T visitMatchExpr(MatchExprContext ctx) {
        TerminalNode notCtx = ctx.NOT();
        MatchSymbolContext symbolCtx = ctx.matchSymbol();

        this.visitMatchSymbol(symbolCtx);
        ctx.expression(0).accept(this);
        ctx.expression(1).accept(this);

        Expression secondExpr = (Expression) this.instStack.pop();
        Expression firstExpr = (Expression) this.instStack.pop();
        SymbolToken symbol = (SymbolToken) this.instStack.pop();

        if (notCtx != null) {
            symbol = code(new DoubleSymbolToken(symbol, notCtx.getText(), SymbolSide.Left), notCtx, symbolCtx);
        }

        this.instStack.push(code(new DyadicExpression(firstExpr, symbol, secondExpr), ctx));

        return null;
    }

    @Override
    public T visitLogicAndExpr(LogicAndExprContext ctx) {
        ctx.expression(0).accept(this);
        ctx.expression(1).accept(this);

        Expression secondExpr = (Expression) this.instStack.pop();
        Expression firstExpr = (Expression) this.instStack.pop();
        SymbolToken symbol = fixSymbolToken(ctx.LO_AND());

        this.instStack.push(code(new DyadicExpression(firstExpr, symbol, secondExpr), ctx));

        return null;
    }

    @Override
    public T visitLogicOrExpr(LogicOrExprContext ctx) {
        ctx.expression(0).accept(this);
        ctx.expression(1).accept(this);

        Expression secondExpr = (Expression) this.instStack.pop();
        Expression firstExpr = (Expression) this.instStack.pop();
        SymbolToken symbol = fixSymbolToken(ctx.LO_OR());

        this.instStack.push(code(new DyadicExpression(firstExpr, symbol, secondExpr), ctx));

        return null;
    }

    @Override
    public T visitTernaryExpr(TernaryExprContext ctx) {
        ctx.expression(0).accept(this);
        ctx.expression(1).accept(this);
        ctx.expression(2).accept(this);

        Expression elseExpr = (Expression) this.instStack.pop();
        Expression thenExpr = (Expression) this.instStack.pop();
        Expression testExpr = (Expression) this.instStack.pop();

        this.instStack.push(code(new TernaryExpression(testExpr, thenExpr, elseExpr), ctx));

        return null;
    }

    @Override
    public T visitUnarySymbol(UnarySymbolContext ctx) {
        TerminalNode plusCtx = ctx.AO_PLUS();
        TerminalNode minusCtx = ctx.AO_MINUS();
        TerminalNode notCtx = ctx.NOT();

        if (plusCtx != null) {
            this.instStack.push(fixSymbolToken(plusCtx));
        } else if (minusCtx != null) {
            this.instStack.push(fixSymbolToken(minusCtx));
        } else if (notCtx != null) {
            this.instStack.push(fixSymbolToken(notCtx));
        } else {
            throw new IllegalStateException("can't happen.");
        }

        return null;
    }

    @Override
    public T visitMultiplicativeSymbol(MultiplicativeSymbolContext ctx) {
        TerminalNode mulCtx = ctx.AO_MUL();
        TerminalNode divCtx = ctx.AO_DIV();
        TerminalNode modCtx = ctx.AO_MOD();

        if (mulCtx != null) {
            this.instStack.push(fixSymbolToken(mulCtx));
        } else if (divCtx != null) {
            this.instStack.push(fixSymbolToken(divCtx));
        } else if (modCtx != null) {
            this.instStack.push(fixSymbolToken(modCtx));
        } else {
            throw new IllegalStateException("can't happen.");
        }

        return null;
    }

    @Override
    public T visitAdditiveSymbol(AdditiveSymbolContext ctx) {
        TerminalNode plusCtx = ctx.AO_PLUS();
        TerminalNode minusCtx = ctx.AO_MINUS();

        if (plusCtx != null) {
            this.instStack.push(fixSymbolToken(plusCtx));
        } else if (minusCtx != null) {
            this.instStack.push(fixSymbolToken(minusCtx));
        } else {
            throw new IllegalStateException("can't happen.");
        }

        return null;
    }

    @Override
    public T visitBitwiseSymbol(BitwiseSymbolContext ctx) {
        TerminalNode andCtx = ctx.BO_AND();
        TerminalNode orCtx = ctx.BO_OR();
        TerminalNode xorCtx = ctx.BO_XOR();

        if (andCtx != null) {
            this.instStack.push(fixSymbolToken(andCtx));
        } else if (orCtx != null) {
            this.instStack.push(fixSymbolToken(orCtx));
        } else if (xorCtx != null) {
            this.instStack.push(fixSymbolToken(xorCtx));
        } else {
            throw new IllegalStateException("can't happen.");
        }

        return null;
    }

    @Override
    public T visitShiftSymbol(ShiftSymbolContext ctx) {
        TerminalNode rshift2Ctx = ctx.BO_RSHIFT2();
        TerminalNode rshiftCtx = ctx.BO_RSHIFT();
        TerminalNode lshiftCtx = ctx.BO_LSHIFT();

        if (rshift2Ctx != null) {
            this.instStack.push(fixSymbolToken(rshift2Ctx));
        } else if (rshiftCtx != null) {
            this.instStack.push(fixSymbolToken(rshiftCtx));
        } else if (lshiftCtx != null) {
            this.instStack.push(fixSymbolToken(lshiftCtx));
        } else {
            throw new IllegalStateException("can't happen.");
        }

        return null;
    }

    @Override
    public T visitCompareSymbol(CompareSymbolContext ctx) {
        TerminalNode geCtx = ctx.CO_GE();
        TerminalNode gtCtx = ctx.CO_GT();
        TerminalNode leCtx = ctx.CO_LE();
        TerminalNode ltCtx = ctx.CO_LT();
        TerminalNode eqCtx = ctx.CO_EQ();
        TerminalNode ieqCtx = ctx.CO_IEQ();
        TerminalNode neCtx = ctx.CO_NE();

        if (geCtx != null) {
            this.instStack.push(fixSymbolToken(geCtx));
        } else if (gtCtx != null) {
            this.instStack.push(fixSymbolToken(gtCtx));
        } else if (leCtx != null) {
            this.instStack.push(fixSymbolToken(leCtx));
        } else if (ltCtx != null) {
            this.instStack.push(fixSymbolToken(ltCtx));
        } else if (eqCtx != null) {
            this.instStack.push(fixSymbolToken(eqCtx));
        } else if (ieqCtx != null) {
            this.instStack.push(fixSymbolToken(ieqCtx));
        } else if (neCtx != null) {
            this.instStack.push(fixSymbolToken(neCtx));
        } else {
            throw new IllegalStateException("can't happen.");
        }

        return null;
    }

    @Override
    public T visitMatchSymbol(MatchSymbolContext ctx) {
        TerminalNode inCtx = ctx.IN();
        TerminalNode matchesCtx = ctx.MATCHES();

        if (inCtx != null) {
            this.instStack.push(fixSymbolToken(inCtx));
        } else if (matchesCtx != null) {
            this.instStack.push(fixSymbolToken(matchesCtx));
        } else {
            throw new IllegalStateException("can't happen.");
        }

        return null;
    }

    @Override
    public T visitParamExpr(ParamExprContext ctx) {
        TerminalNode start = ctx.OREP();
        TerminalNode end = ctx.CCBR();
        TerminalNode stringCtx = ctx.PRI_STR();
        IdentifyContext idExprCtx = ctx.identify();

        if (stringCtx != null) {
            StringToken string = fixString(stringCtx);
            this.instStack.push(code(new ParamExpression(string), start, end));
        } else if (idExprCtx != null) {
            idExprCtx.accept(this);
            IdentifyNameExpression idExpr = (IdentifyNameExpression) this.instStack.pop();
            String warpChar = idExpr.isUsingQualifier() ? "`" : "";
            String idStr = idExpr.getIdentify();

            StringToken paramStr = code(new StringToken(warpChar, idStr), idExpr);
            this.instStack.push(code(new ParamExpression(paramStr), start, end));
        } else {
            throw new IllegalStateException("can't happen.");
        }

        return null;
    }

    @Override
    public T visitFunOrDetectExpr(FunOrDetectExprContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitDetectPart(DetectPartContext ctx) {
        ctx.identifyExpr().accept(this);
        IdentifyGroupExpression idExpr = (IdentifyGroupExpression) this.instStack.pop();
        idExpr = code(idExpr, ctx);

        this.instStack.push(code(new DetectExpression(idExpr), ctx));
        return null;
    }

    @Override
    public T visitFuncPart(FuncPartContext ctx) {
        DetectExpression funcExpr = (DetectExpression) this.instStack.pop();
        TerminalNode end = ctx.RBT();
        IdentifyExprContext followExprCtx = ctx.identifyExpr();
        SubExprContext followSubCtx = ctx.subExpr();

        // args
        this.instStack.push(code(new FunctionParameters(), ctx.LBT(), ctx.RBT()));
        FuncArgPartContext funcArgCtx = ctx.funcArgPart();
        if (funcArgCtx != null) {
            this.visitFuncArgPart(funcArgCtx);
        }
        FunctionParameters funcParams = (FunctionParameters) this.instStack.pop();

        // func
        FunctionExpression func = code(new FunctionExpression(funcExpr, funcParams), funcExpr.getStartPosition(), end);
        if (followExprCtx == null && followSubCtx == null) {
            this.instStack.push(func);
            return null;
        }

        // follow-up
        IdentifyGroupExpression newFunc = code(new IdentifyGroupExpression(), funcExpr.getStartPosition(), followExprCtx);
        newFunc.appendExpr(func);

        if (followExprCtx != null) {
            followExprCtx.accept(this);
        } else if (followSubCtx != null) {
            this.instStack.push(code(new IdentifyGroupExpression(), followSubCtx));
            followSubCtx.accept(this);
        } else {
            throw new IllegalStateException("can't happen.");
        }

        IdentifyGroupExpression followExpr = (IdentifyGroupExpression) this.instStack.pop();
        for (Expression expr : followExpr.getContinuous()) {
            newFunc.appendExpr(expr);
        }
        this.instStack.push(newFunc);

        return null;
    }

    @Override
    public T visitFuncArgPart(FuncArgPartContext ctx) {
        FunctionParameters params = (FunctionParameters) this.instStack.peek();

        List<ExpressionContext> exprCtxList = ctx.expression();
        for (ExpressionContext exprCtx : exprCtxList) {
            exprCtx.accept(this);
            params.addArg((Expression) this.instStack.pop());
        }

        return null;
    }

    @Override
    public T visitIdentifyExpr(IdentifyExprContext ctx) {
        this.instStack.push(code(new IdentifyGroupExpression(), ctx));

        ctx.identifyPart().accept(this);

        SubExprContext subExprCtx = ctx.subExpr();
        if (subExprCtx != null) {
            subExprCtx.accept(this);
        }

        return null;
    }

    @Override
    public T visitIdentifyPart(IdentifyPartContext ctx) {
        IdentifyGroupExpression idGroupExpr = (IdentifyGroupExpression) this.instStack.peek();

        for (IdentifyContext idExprCtx : ctx.identify()) {
            idExprCtx.accept(this);
            idGroupExpr.appendExpr((IdentifyNameExpression) this.instStack.pop());
        }

        return null;
    }

    @Override
    public T visitSubExpr(SubExprContext ctx) {
        IdentifyGroupExpression idGroupExpr = (IdentifyGroupExpression) this.instStack.peek();

        ctx.expression().accept(this);
        Expression expression = (Expression) this.instStack.pop();
        idGroupExpr.appendExpr(code(new IdentifySubExpression(expression), ctx.LSBT(), ctx.RSBT()));

        SubExprContext subExprCtx = ctx.subExpr();
        if (subExprCtx != null) {
            subExprCtx.accept(this);
        }

        IdentifyExprContext idExprCtx = ctx.identifyExpr();
        if (idExprCtx != null) {
            idExprCtx.accept(this);
            IdentifyGroupExpression group = (IdentifyGroupExpression) this.instStack.pop();
            for (Expression identify : group.getContinuous()) {
                idGroupExpr.appendExpr(identify);
            }
        }

        return null;
    }

    @Override
    public T visitIdentify(IdentifyContext ctx) {
        TerminalNode idExprCtx1 = ctx.IDENTIFIER();
        TerminalNode idExprCtx2 = ctx.PRI_IDENTIFIER();
        AllIdentifierKeywordsContext idExprCtx3 = ctx.allIdentifierKeywords();

        if (idExprCtx1 != null) {
            String idStr = idExprCtx1.getText();
            this.instStack.push(code(new IdentifyNameExpression(false, idStr), ctx));
        } else if (idExprCtx2 != null) {
            String idStr = idExprCtx2.getText();
            idStr = idStr.substring(1, idStr.length() - 1);
            this.instStack.push(code(new IdentifyNameExpression(true, idStr), ctx));
        } else if (idExprCtx3 != null) {
            idExprCtx3.accept(this);
        } else {
            throw new IllegalStateException("can't happen.");
        }

        return null;
    }

    @Override
    public T visitAllIdentifierKeywords(AllIdentifierKeywordsContext ctx) {
        ParseTree child = ctx.getChild(0);
        String idStr = child.getText();
        this.instStack.push(code(new IdentifyNameExpression(false, idStr), ctx));
        return null;
    }

    /* ----------------------------------------------------------------------------------- basic type */

    @Override
    public T visitAnyPrimary(AnyPrimaryContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitBooleanValue(BooleanValueContext ctx) {
        TerminalNode trueCtx = ctx.TRUE();
        TerminalNode falseCtx = ctx.FALSE();

        if (trueCtx != null) {
            this.instStack.push(code(new BooleanValue("true"), trueCtx));
        } else {
            this.instStack.push(code(new BooleanValue("false"), falseCtx));
        }

        return null;
    }

    @Override
    public T visitNumberValue(NumberValueContext ctx) {
        ctx.numValue().accept(this);
        NumberValue numValue = (NumberValue) this.instStack.peek();
        code(numValue, ctx);

        SignSymbolContext signCtx = ctx.signSymbol();
        if (signCtx != null) {
            numValue.setSymbol(true);

            if (signCtx.AO_MINUS() != null) {
                numValue.setNegative(true);
            } else if (signCtx.AO_PLUS() != null) {
                numValue.setNegative(false);
            }
        }

        return null;
    }

    @Override
    public T visitSignSymbol(SignSymbolContext ctx) {
        return null; // do nothing
    }

    @Override
    public T visitFloatNumValuePart(FloatNumValuePartContext ctx) {
        TerminalNode floatCtx = ctx.PRI_FLOAT();
        TerminalNode decCtx = ctx.PRI_DECIMAL();
        TerminalNode intCtx = ctx.PRI_INTEGER();

        if (floatCtx != null) {
            String floatStr = floatCtx.getText();
            this.instStack.push(code(new NumberValue(NumberType.Float, floatStr), floatCtx));

        } else if (decCtx != null) {
            String decStr = decCtx.getText();
            this.instStack.push(code(new NumberValue(NumberType.Decimal, decStr), decCtx));

        } else if (intCtx != null) {
            String floatStr = intCtx.getText();
            this.instStack.push(code(new NumberValue(NumberType.Float, floatStr + "."), intCtx));

        } else {
            throw new IllegalStateException("can't happen.");
        }

        return null;
    }

    @Override
    public T visitIntNumValuePart(IntNumValuePartContext ctx) {
        TerminalNode intCtx = ctx.PRI_INTEGER();
        String intStr = intCtx.getText();
        this.instStack.push(code(new NumberValue(NumberType.Integer, intStr), intCtx));

        return null;
    }

    @Override
    public T visitXobValue(XobValueContext ctx) {
        TerminalNode xobCtx = ctx.PRI_XOB_NUM();
        String xobText = xobCtx.getText();

        if (StringUtils.startsWith(xobText, "0x")) {
            String xobValue = xobText.substring(2);
            this.instStack.push(code(new XxValue(XxType.Hex, CaseType.LowerCase, xobValue), xobCtx));
        } else if (StringUtils.startsWith(xobText, "0X")) {
            String xobValue = xobText.substring(2);
            this.instStack.push(code(new XxValue(XxType.Hex, CaseType.UpperCase, xobValue), xobCtx));
        } else if (StringUtils.startsWith(xobText, "0o")) {
            String xobValue = xobText.substring(2);
            this.instStack.push(code(new XxValue(XxType.Oct, CaseType.LowerCase, xobValue), xobCtx));
        } else if (StringUtils.startsWith(xobText, "0O")) {
            String xobValue = xobText.substring(2);
            this.instStack.push(code(new XxValue(XxType.Oct, CaseType.UpperCase, xobValue), xobCtx));
        } else if (StringUtils.startsWith(xobText, "0b")) {
            String xobValue = xobText.substring(2);
            this.instStack.push(code(new XxValue(XxType.Bit, CaseType.LowerCase, xobValue), xobCtx));
        } else if (StringUtils.startsWith(xobText, "0B")) {
            String xobValue = xobText.substring(2);
            this.instStack.push(code(new XxValue(XxType.Bit, CaseType.UpperCase, xobValue), xobCtx));
        } else {
            throw new IllegalStateException("can't happen.");
        }

        return null;
    }

    @Override
    public T visitStringValue(StringValueContext ctx) {
        TerminalNode stringCtx = ctx.PRI_STR();

        String stringText = stringCtx.getText();
        String wrapStr = String.valueOf(stringText.charAt(0));
        String bodyText = stringText.substring(1, stringText.length() - 1);

        this.instStack.push(code(new StringValue(wrapStr, bodyText), stringCtx));

        return null;
    }

    @Override
    public T visitDatetimeValue(DatetimeValueContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitFmtDateTimeValuePart(FmtDateTimeValuePartContext ctx) {
        TerminalNode fmtCtx = ctx.PRI_DATETIME_FMT();
        TerminalNode dtCtx = ctx.PRI_STR();

        StringToken fmtToken = fixDateTimeFmtIncludeBracket(fmtCtx);
        StringToken valueToken = fixString(dtCtx);

        CastTypeDtFmtToken dtFmtToken = code(new CastTypeDtFmtToken(fmtToken), fmtCtx);
        this.instStack.push(code(new DataTimeValue(valueToken, dtFmtToken), ctx));

        return null;
    }

    @Override
    public T visitDateTimeValueValuePart(DateTimeValueValuePartContext ctx) {
        TerminalNode dtCtx = ctx.PRI_DATETIME();
        StringToken valueToken = fixDateTimeFmt(dtCtx);

        this.instStack.push(code(new DataTimeValue(valueToken, null), ctx));
        return null;
    }

    @Override
    public T visitNullVaue(NullVaueContext ctx) {
        TerminalNode nullCtx = ctx.NULL();

        this.instStack.push(code(new NullValue(nullCtx.getText()), nullCtx));

        return null;
    }

    @Override
    public T visitCollectionValue(CollectionValueContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitListValue(ListValueContext ctx) {
        ListValue listValue = code(new ListValue(), ctx);

        List<AnyPrimaryContext> valueCtxList = ctx.anyPrimary();
        if (valueCtxList != null) {
            for (AnyPrimaryContext valueCtx : valueCtxList) {
                valueCtx.accept(this);
                Expression value = (Expression) this.instStack.pop();
                listValue.addExpression(value);
            }
        }

        this.instStack.push(listValue);
        return null;
    }

    @Override
    public T visitObjectValue(ObjectValueContext ctx) {
        this.instStack.push(code(new ObjectValue(), ctx));

        return visitChildren(ctx);
    }

    @Override
    public T visitObjectItem(ObjectItemContext ctx) {
        ObjectValue objectValue = (ObjectValue) this.instStack.peek();

        StringToken keyToken = fixString(ctx.PRI_STR());

        ctx.anyPrimary().accept(this);
        Expression valToken = (Expression) this.instStack.pop();

        objectValue.addField(code(new KeyPairValue(keyToken, valToken), ctx));

        return null;
    }

    @Override
    public T visitCastExpr(CastExprContext ctx) {
        ExpressionContext exprCtx = ctx.expression();
        DataTypeContext dataTypeCtx = ctx.dataType();

        exprCtx.accept(this);
        Expression expression = (Expression) this.instStack.pop();

        dataTypeCtx.accept(this);
        CastExpressionAsType typeToken = (CastExpressionAsType) this.instStack.pop();

        this.instStack.push(code(new CastExpression(expression, typeToken), ctx));

        return null;
    }

    @Override
    public T visitCastNum(CastNumContext ctx) {
        TerminalNode intSymbolCtx = ctx.INTEGER();
        TerminalNode floatSymbolCtx = ctx.FLOAT();
        TerminalNode decSymbolCtx = ctx.DECIMAL();

        StringToken symbol;
        CastType castType;
        if (intSymbolCtx != null) {
            symbol = this.asStringToken(intSymbolCtx);
            castType = CastType.Integer;
        } else if (floatSymbolCtx != null) {
            symbol = this.asStringToken(floatSymbolCtx);
            castType = CastType.Float;
        } else if (decSymbolCtx != null) {
            symbol = this.asStringToken(decSymbolCtx);
            castType = CastType.Decimal;
        } else {
            throw new IllegalStateException("can't happen.");
        }

        CastExpressionAsType castToken = code(new CastExpressionAsType(symbol, castType), ctx);

        NumFmtValueContext fmtCtx = ctx.numFmtValue();
        if (fmtCtx != null) {
            fmtCtx.accept(this);
            CastTypeNumFmtToken numFmtToken = (CastTypeNumFmtToken) this.instStack.pop();
            castToken.setNumFmtToken(numFmtToken);
        }

        this.instStack.push(castToken);

        return null;
    }

    @Override
    public T visitNumFmtValue(NumFmtValueContext ctx) {
        TerminalNode precisionCtx = ctx.PRI_INTEGER();
        StringToken precision = code(new StringToken("", precisionCtx.getText()), precisionCtx);

        RoundingToken roundingToken = null;
        NumFmtRoundingContext roundingCtx = ctx.numFmtRounding();
        if (roundingCtx != null) {
            roundingCtx.accept(this);
            roundingToken = (RoundingToken) this.instStack.pop();
        }

        this.instStack.push(code(new CastTypeNumFmtToken(precision, roundingToken), ctx));
        return null;
    }

    @Override
    public T visitNumFmtRounding(NumFmtRoundingContext ctx) {
        TerminalNode upSymbolCtx = ctx.UP();
        TerminalNode downSymbolCtx = ctx.DOWN();
        TerminalNode ceilingSymbolCtx = ctx.CEILING();
        TerminalNode floorSymbolCtx = ctx.FLOOR();
        TerminalNode halfUpSymbolCtx = ctx.HALF_UP();
        TerminalNode halfDownSymbolCtx = ctx.HALF_DOWN();
        TerminalNode halfEvenSymbolCtx = ctx.HALF_EVEN();
        TerminalNode unnecessarySymbolCtx = ctx.UNNECESSARY();

        String symbol;
        RoundingMode roundingType;
        if (upSymbolCtx != null) {
            symbol = upSymbolCtx.getText();
            roundingType = RoundingMode.UP;
        } else if (downSymbolCtx != null) {
            symbol = downSymbolCtx.getText();
            roundingType = RoundingMode.DOWN;
        } else if (ceilingSymbolCtx != null) {
            symbol = ceilingSymbolCtx.getText();
            roundingType = RoundingMode.CEILING;
        } else if (floorSymbolCtx != null) {
            symbol = floorSymbolCtx.getText();
            roundingType = RoundingMode.FLOOR;
        } else if (halfUpSymbolCtx != null) {
            symbol = halfUpSymbolCtx.getText();
            roundingType = RoundingMode.HALF_UP;
        } else if (halfDownSymbolCtx != null) {
            symbol = halfDownSymbolCtx.getText();
            roundingType = RoundingMode.HALF_DOWN;
        } else if (halfEvenSymbolCtx != null) {
            symbol = halfEvenSymbolCtx.getText();
            roundingType = RoundingMode.HALF_EVEN;
        } else if (unnecessarySymbolCtx != null) {
            symbol = unnecessarySymbolCtx.getText();
            roundingType = RoundingMode.UNNECESSARY;
        } else {
            throw new IllegalStateException("can't happen.");
        }

        this.instStack.push(code(new RoundingToken(symbol, roundingType), ctx));
        return null;
    }

    @Override
    public T visitCastOther(CastOtherContext ctx) {
        TerminalNode boolSymbolCtx = ctx.BOOL();
        TerminalNode strSymbolCtx = ctx.STRING();
        TerminalNode dateSymbolCtx = ctx.DATE();
        TerminalNode timeSymbolCtx = ctx.TIME();
        TerminalNode datetimeSymbolCtx = ctx.DATETIME();

        StringToken symbol;
        CastType castType;
        if (boolSymbolCtx != null) {
            symbol = this.asStringToken(boolSymbolCtx);
            castType = CastType.Bool;
        } else if (strSymbolCtx != null) {
            symbol = this.asStringToken(strSymbolCtx);
            castType = CastType.String;
        } else if (dateSymbolCtx != null) {
            symbol = this.asStringToken(dateSymbolCtx);
            castType = CastType.Date;
        } else if (timeSymbolCtx != null) {
            symbol = this.asStringToken(timeSymbolCtx);
            castType = CastType.Time;
        } else if (datetimeSymbolCtx != null) {
            symbol = this.asStringToken(datetimeSymbolCtx);
            castType = CastType.DateTime;
        } else {
            throw new IllegalStateException("can't happen.");
        }

        this.instStack.push(code(new CastExpressionAsType(symbol, castType), ctx));
        return null;
    }

    @Override
    public T visitCastDtFmt(CastDtFmtContext ctx) {
        TerminalNode fmtCtx = ctx.PRI_DATETIME_FMT();
        StringToken fmtToken = fixDateTimeFmtIncludeBracket(fmtCtx);

        CastTypeDtFmtToken dtFmt = code(new CastTypeDtFmtToken(fmtToken), fmtToken);

        CastExpressionAsType ctToken = code(new CastExpressionAsType(null, CastType.DateTimeFormat), ctx);
        ctToken.setDtFmtToken(dtFmt);

        this.instStack.push(ctToken);

        return null;
    }

    private StringToken fixString(TerminalNode stringNode) {
        String nodeText = stringNode.getText();

        String wrapStr = String.valueOf(nodeText.charAt(0));
        String bodyText = nodeText.substring(1, nodeText.length() - 1);
        return code(new StringToken(wrapStr, bodyText), stringNode);
    }

    private StringToken fixDateTimeFmt(TerminalNode stringNode) {
        String nodeText = stringNode.getText();
        nodeText = nodeText.substring(1);

        String wrapStr = String.valueOf(nodeText.charAt(0));
        String bodyText = nodeText.substring(1, nodeText.length() - 1);
        return code(new StringToken(wrapStr, bodyText), stringNode);
    }

    private StringToken fixDateTimeFmtIncludeBracket(TerminalNode stringNode) {
        String nodeText = stringNode.getText();
        nodeText = nodeText.substring(2, nodeText.length() - 1);

        String wrapStr = String.valueOf(nodeText.charAt(0));
        String bodyText = nodeText.substring(1, nodeText.length() - 1);
        return code(new StringToken(wrapStr, bodyText), stringNode);
    }

    private StringToken asStringToken(TerminalNode stringNode) {
        String nodeText = stringNode.getText();
        return code(new StringToken("", nodeText), stringNode);
    }

    private SymbolToken fixSymbolToken(TerminalNode stringNode) {
        String nodeText = stringNode.getText();
        return code(new SymbolToken(nodeText), stringNode);
    }

    /* ----------------------------------------------------------------------------------- statements */

    @Override
    public T visitComment(CommentContext ctx) {
        TerminalNode c1 = ctx.COMMENT1();
        TerminalNode c2 = ctx.COMMENT1();
        return null;
    }
}
