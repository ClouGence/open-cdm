package com.clougence.detectrule.parser.ast.expr;

import java.io.IOException;
import java.util.Objects;

import com.clougence.detectrule.parser.ast.Expression;
import com.clougence.detectrule.parser.ast.Value;
import com.clougence.detectrule.parser.ast.primary.NumberValue;
import com.clougence.detectrule.parser.ast.token.SymbolSide;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.ast.token.SymbolToken;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * unary operation expression
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class UnaryExpression extends AstFragment implements Expression {

    private SymbolSide  side;
    private SymbolToken symbol;
    private Expression  expr;

    public UnaryExpression(SymbolToken symbol, SymbolSide side, Expression expr){
        this.side = Objects.requireNonNull(side, "symbol side is null.");
        this.symbol = Objects.requireNonNull(symbol, "symbol is null.");
        this.expr = Objects.requireNonNull(expr, "expr is null.");
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitUnaryExpression(new VisitorContext<>(this, v -> {
                this.expr.accept(v);
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        String symbol = this.symbol.getValue();
        if (this.side == SymbolSide.Left) {
            if (symbol.equals("not")) {
                writer.write("not ");
            } else if (this.expr instanceof NumberValue && ((NumberValue) this.expr).isSymbol()) {
                writer.write(symbol + " ");
            } else if (this.expr instanceof Value || this.expr instanceof PrivilegeExpression || this.expr instanceof DetectExpression || this.expr instanceof FunctionExpression
                       || this.expr instanceof ParamExpression) {
                writer.write(symbol);
            } else {
                writer.writeSymbol(symbol);
            }
            this.expr.doFormat(writer);
        } else {
            this.expr.doFormat(writer);
            if (this.expr instanceof PrivilegeExpression) {
                writer.write(symbol);
            } else if (!writer.lastIsBlank()) {
                writer.write(" ");
                writer.write(symbol);
            } else {
                writer.write(symbol);
            }
        }
    }
}
