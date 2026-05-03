package com.clougence.detectrule.parser.ast.statement;

import java.io.IOException;

import com.clougence.detectrule.parser.ast.Fragment;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.ast.visitor.VisitorTree;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * if or switch else statement
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class SwitchElseStatement extends StatementList implements Fragment, VisitorTree {

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitSwitchElseStatement(new VisitorContext<>(this, v -> {
                super.accept(visitor);
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        this.printKeywords("else", writer);
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
