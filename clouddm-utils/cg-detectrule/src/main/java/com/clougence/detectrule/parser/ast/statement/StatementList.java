package com.clougence.detectrule.parser.ast.statement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.detectrule.parser.ast.Fragment;
import com.clougence.detectrule.parser.ast.RuleStatement;
import com.clougence.detectrule.parser.antlr.DetectRuleAstVisitor;
import com.clougence.dslpaser.ast.Statement;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.dslpaser.ast.fragment.AstFragment;
import com.clougence.dslpaser.ast.visitor.Visitor;
import com.clougence.dslpaser.ast.visitor.VisitorContext;
import com.clougence.dslpaser.ast.visitor.VisitorTree;
import com.clougence.dslpaser.foramt.FmtWriter;

import lombok.Getter;
import lombok.Setter;

/**
 * multiple statement
 * @author zyc@hasor.net
 * @version : 2023-05-17
 */
@Getter
@Setter
public class StatementList extends AstFragment implements RuleStatement, Fragment, StatementSet, VisitorTree {

    private final List<Statement> statementList = new ArrayList<>();

    @Override
    public List<Statement> getStatements() { return this.statementList; }

    public void clear() {
        this.statementList.clear();
    }

    public void add(RuleStatement cmdInst) {
        if (cmdInst != null) {
            this.statementList.add(cmdInst);
        }

    }

    @Override
    public void accept(Visitor visitor) {
        if (visitor instanceof DetectRuleAstVisitor) {
            ((DetectRuleAstVisitor) visitor).visitStatementList(new VisitorContext<>(this, v -> {
                for (Statement statement : this.statementList) {
                    statement.accept(v);
                }
            }));
        }
    }

    @Override
    public void doFormat(FmtWriter writer) throws IOException {
        for (int i = 0; i < this.statementList.size(); i++) {
            Statement statement = this.statementList.get(i);
            if (i > 0) {
                writer.newLineAndIndents();
            }
            statement.doFormat(writer);
        }
    }

}
