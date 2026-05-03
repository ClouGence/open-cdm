package com.clougence.detectrule.parser.ast.primary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.detectrule.parser.ast.Expression;
import com.clougence.detectrule.parser.ast.Value;
import com.clougence.detectrule.parser.ast.expr.ParamExpression;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.foramt.FmtOptions;
import com.clougence.dslpaser.foramt.FmtWriter;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * list value
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class ListValue extends AstFragment implements Value {

    private final List<Expression> collection = new ArrayList<>();

    public void addExpression(Expression valueExp) {
        if (valueExp != null) {
            this.collection.add(valueExp);
        }
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitListValue(new VisitorContext<>(this, v -> {
                for (Expression expr : this.collection) {
                    expr.accept(v);
                }
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        String newLineMode = writer.getStringOption(FmtOptions.WRAPPING_ARRAY_ELEMENT_AS_NEW_LINE);

        writer.write("[");
        if (this.collection.isEmpty()) {
            if (writer.getBooleanOption(FmtOptions.NEW_LINE_EMPTY_ARRAY)) {
                writer.newLineAndIndents();
            } else if (writer.getBooleanOption(FmtOptions.SPACES_EMPTY_ARRAY)) {
                writer.write(" ");
            }
        } else {
            writer.incrDepth();
            boolean lastIsExpr = false;
            for (int i = 0; i < this.collection.size(); i++) {
                Expression expr = this.collection.get(i);

                // before element
                if (i > 0) {
                    if (StringUtils.equalsIgnoreCase(newLineMode, "Any")) {
                        writer.write(",");
                        writer.newLineAndIndents();
                    } else if (StringUtils.equalsIgnoreCase(newLineMode, "Expression")) {
                        if (isExpression(expr)) {
                            writer.write(",");
                            writer.newLineAndIndents();
                        } else {
                            writer.write(", ");
                        }
                    } else {
                        writer.write(", ");
                    }
                } else {
                    expr = this.collection.get(0);
                    if (StringUtils.equalsIgnoreCase(newLineMode, "Any")) {
                        writer.newLineAndIndents();
                    } else if (StringUtils.equalsIgnoreCase(newLineMode, "Expression")) {
                        if (isExpression(expr)) {
                            writer.newLineAndIndents();
                        }
                    }
                }

                // do element
                expr.doFormat(writer);
                lastIsExpr = isExpression(expr);
            }
            writer.decrDepth();

            // after element
            if (StringUtils.equalsIgnoreCase(newLineMode, "Any")) {
                writer.newLineAndIndents();
            } else if (StringUtils.equalsIgnoreCase(newLineMode, "Expression")) {
                if (lastIsExpr) {
                    writer.newLineAndIndents();
                }
            }
        }
        writer.write("]");
    }

    private boolean isExpression(Expression expr) {
        if (expr instanceof Value || expr instanceof ParamExpression) {
            return false;
        } else {
            return true;
        }
    }
}
