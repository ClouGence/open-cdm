package com.clougence.detectrule.parser.ast.statement;

import java.io.IOException;

import com.clougence.detectrule.parser.ast.Expression;
import com.clougence.detectrule.parser.ast.Fragment;
import com.clougence.detectrule.parser.format.DetectRuleFmtOptions;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.ast.visitor.VisitorTree;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * if or switch then statement
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class SwitchThenStatement extends StatementList implements Fragment, VisitorTree {

    private Expression testExpr;

    public SwitchThenStatement(Expression testExpr){
        this.testExpr = testExpr;
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitSwitchThenStatement(new VisitorContext<>(this, v -> {
                testExpr.accept(visitor);
                super.accept(visitor);
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        if (writer.getBooleanOption(DetectRuleFmtOptions.NEW_LINE_IF_AROUND_CONDITION) && !writer.lastIsNewLine()) {
            writer.newLineAndIndents();

            writer.incrDepth();
            writer.fillSpace();
            this.testExpr.doFormat(writer);
            writer.decrDepth();

            writer.newLineAndIndents();
            this.printKeywords("then", writer);
        } else {

            if (!writer.lastIsBlank()) {
                writer.write(" ");
            }
            this.testExpr.doFormat(writer);

            this.printKeywords("then", writer);
        }

        // after then ...
        writer.newLineAndIndents();
        writer.incrDepth();
        writer.fillSpace();
        super.doFormat(writer);
        writer.decrDepth();
    }

    private void printKeywords(String keywords, FmtWriter writer) throws IOException {
        if (writer.lastIsBlank() || writer.lastIsNewLine()) {
            writer.write(keywords);
        } else {
            writer.write(" ");
            writer.write(keywords);
        }
    }
}
