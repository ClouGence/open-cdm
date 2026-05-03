package com.clougence.clouddm.ds.sqlserver.analysis.resolve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLAlterViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbViewDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class MsViewResolveHelper extends MsAbstractResolveHelper {

    public List<RuleDomain> fromAlterView(SQLAlterViewStatement statement) {
        RdbViewDomain domain = new RdbViewDomain();
        domain.setSqlType(SecQueryType.ALTER_VIEW);
        domain.setAuditKind(SecQueryKind.ALTER);
        domain.setOptions(Collections.emptyMap());

        SQLExprTableSource tableSource = statement.getTableSource();
        SQLName tableName = tableSource.getName();
        domain.setView(extractName(tableName));

        return Collections.singletonList(domain);
    }

    public List<RuleDomain> fromDropView(SQLDropViewStatement statement) {
        List<RuleDomain> dropList = new ArrayList<>();
        for (SQLExprTableSource item : statement.getTableSources()) {
            RdbViewDomain domain = new RdbViewDomain();
            domain.setSqlType(SecQueryType.DROP_VIEW);
            domain.setAuditKind(SecQueryKind.DROP);
            domain.setOptions(Collections.emptyMap());

            domain.setView(extractName(item.getName()));
            dropList.add(domain);
        }
        return dropList;
    }

    public List<RuleDomain> fromCreateView(SQLCreateViewStatement statement) {
        RdbViewDomain domain = new RdbViewDomain();
        domain.setSqlType(SecQueryType.CREATE_VIEW);
        domain.setAuditKind(SecQueryKind.CREATE);
        domain.setOptions(Collections.emptyMap());

        SQLExprTableSource tableSource = statement.getTableSource();
        SQLName tableName = tableSource.getName();
        domain.setView(extractName(tableName));

        return Collections.singletonList(domain);
    }
}
