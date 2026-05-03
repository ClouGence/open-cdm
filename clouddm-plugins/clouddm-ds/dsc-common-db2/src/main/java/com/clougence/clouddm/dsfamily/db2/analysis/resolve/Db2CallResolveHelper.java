package com.clougence.clouddm.dsfamily.db2.analysis.resolve;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.SQLCallStatement;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCallDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

import java.util.ArrayList;
import java.util.List;

public class Db2CallResolveHelper extends Db2AbstractResolveHelper {

    public List<RuleDomain> fromCall(SQLCallStatement statement) {

        List<RuleDomain> result = new ArrayList<>();
        RdbCallDomain domain = new RdbCallDomain();
        domain.setArgs(new ArrayList<>());
        domain.setSqlType(SecQueryType.CALL);
        domain.setAuditKind(SecQueryKind.CALL);

        SQLName sqlName = statement.getProcedureName();
        if (sqlName instanceof SQLPropertyExpr) {
            SQLPropertyExpr procedureName = (SQLPropertyExpr) sqlName;
            domain.setCallName(procedureName.getName());
            domain.setSchema(procedureName.getOwnerName());
        } else if (sqlName instanceof SQLIdentifierExpr) {
            SQLIdentifierExpr identifier = (SQLIdentifierExpr) sqlName;
            domain.setCallName(identifier.getName());
        }

        if (statement.getParameters() != null && !statement.getParameters().isEmpty()) {
            domain.setEmptyArg(false);
            for (SQLExpr parameter : statement.getParameters()) {
                fromParam(parameter, domain, result);
            }
        } else {
            domain.setEmptyArg(true);
        }

        result.add(0, domain);
        return result;
    }

    private void fromParam(SQLExpr parameter, RdbCallDomain domain, List<RuleDomain> result) {
        if (parameter instanceof SQLIntegerExpr) {
            domain.getArgs().add(((SQLIntegerExpr) parameter).getValue().toString());
        } else if (parameter instanceof SQLCharExpr) {
            domain.getArgs().add(((SQLCharExpr) parameter).getText());
        } else if (parameter instanceof SQLMethodInvokeExpr) {
            domain.getArgs().add(parameter.toString());
            handleSQLMethodInvokeExpr((SQLMethodInvokeExpr) parameter, result);
        }
    }

    private void handleSQLMethodInvokeExpr(SQLMethodInvokeExpr expr, List<RuleDomain> result) {
        RdbCallDomain rdbCallDomain = toCallDomain(expr);
        rdbCallDomain.setFunc(true);

        if (expr.getArguments() != null && !expr.getArguments().isEmpty()) {
            for (SQLExpr argument : expr.getArguments()) {
                fromParam(argument, rdbCallDomain, result);
            }
            rdbCallDomain.setEmptyArg(false);
        } else {
            rdbCallDomain.setEmptyArg(true);
        }

        result.add(0, rdbCallDomain);
    }
}
