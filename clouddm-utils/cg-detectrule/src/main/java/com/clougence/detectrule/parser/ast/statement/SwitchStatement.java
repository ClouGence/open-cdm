package com.clougence.detectrule.parser.ast.statement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.detectrule.parser.ast.RuleStatement;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.ast.visitor.VisitorTree;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * if or switch statement
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class SwitchStatement extends AstFragment implements RuleStatement, VisitorTree {

    private final List<SwitchThenStatement> elseIfStatements = new ArrayList<>();
    private SwitchElseStatement             elseStatements   = null;

    public void addIF(SwitchThenStatement thenStatement) {
        this.elseIfStatements.add(thenStatement);
    }

    public void setElse(SwitchElseStatement elseStatements) { this.elseStatements = elseStatements; }

    public void clearElse() {
        this.elseIfStatements.clear();
    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitSwitchStatement(new VisitorContext<>(this, v -> {
                for (SwitchThenStatement thenStatement : this.elseIfStatements) {
                    thenStatement.accept(v);
                }
                if (this.elseStatements != null) {
                    this.elseStatements.accept(v);
                }
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        if (this.elseIfStatements.isEmpty()) {
            throw new IllegalStateException("missing condition.");
        }

        // if ... then
        for (int i = 0; i < this.elseIfStatements.size(); i++) {
            SwitchThenStatement thenStatement = this.elseIfStatements.get(i);

            // if or elseif ... then
            if (i == 0) {
                this.printKeywords("if", writer);
            } else {
                this.printKeywords("elseif", writer);
            }

            thenStatement.doFormat(writer);

            writer.newLineAndIndents();
        }

        if (this.elseStatements != null) {
            this.elseStatements.doFormat(writer);
            writer.newLineAndIndents();
        }

        writer.write("end");
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
