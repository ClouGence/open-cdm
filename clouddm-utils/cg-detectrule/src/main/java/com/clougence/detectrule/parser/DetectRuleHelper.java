package com.clougence.detectrule.parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.clougence.detectrule.domain.ParamInfo;
import com.clougence.detectrule.parser.ast.expr.ParamExpression;
import com.clougence.detectrule.parser.ast.statement.DefineStatement;
import com.clougence.detectrule.parser.ast.statement.StatementList;
import com.clougence.detectrule.parser.antlr.DefaultDetectRuleAstVisitor;
import com.clougence.dslpaser.ast.Statement;
import com.clougence.dslpaser.ast.token.StringToken;
import com.clougence.dslpaser.ast.visitor.VisitorContext;

public class DetectRuleHelper {

    public static List<ParamInfo> extractParameters(StatementList statementList) {
        Map<String, ParamInfo> infos = new LinkedHashMap<>();

        for (Statement s : statementList.getStatements()) {
            if (s instanceof DefineStatement) {
                DefineStatement def = (DefineStatement) s;
                ParamInfo paramInfo = new ParamInfo();
                paramInfo.setName(def.getName().getValue());

                if (def.getType() != null) {
                    paramInfo.setType(def.getType().getValue().toLowerCase());
                }

                if (def.getDefaultValue() != null) {
                    paramInfo.setDefaultValue(def.getDefaultValue().getValue());
                }
                if (def.getEnums() != null) {
                    List<String> enums = new ArrayList<>();
                    def.getEnums().stream().map(StringToken::getValue).forEach(enumItem -> {
                        if (!enums.contains(enumItem)) {
                            enums.add(enumItem);
                        }
                    });
                    paramInfo.setEnums(enums);
                }

                if (def.getHint() != null) {
                    paramInfo.setHint(def.getHint().getValue());
                }

                infos.put(paramInfo.getName(), paramInfo);
            } else {
                s.accept(new DefaultDetectRuleAstVisitor() {

                    @Override
                    public void visitParamExpression(VisitorContext<ParamExpression> visitCtx) {
                        ParamExpression inst = visitCtx.getInst();
                        String paramName = inst.getParameter().getValue();
                        if (!infos.containsKey(paramName)) {
                            infos.put(paramName, toParamInfo(inst));
                        }
                    }
                });
            }
        }

        return new ArrayList<>(infos.values());
    }

    private static ParamInfo toParamInfo(ParamExpression inst) {
        StringToken paramNameToken = inst.getParameter();
        String paramName = paramNameToken.getValue();

        ParamInfo paramInfo = new ParamInfo();
        paramInfo.setName(paramName);

        return paramInfo;
    }
}
