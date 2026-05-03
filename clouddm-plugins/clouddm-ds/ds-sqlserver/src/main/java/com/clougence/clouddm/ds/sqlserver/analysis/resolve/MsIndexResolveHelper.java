package com.clougence.clouddm.ds.sqlserver.analysis.resolve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class MsIndexResolveHelper extends MsAbstractResolveHelper {

    public List<RuleDomain> fromCreateIndex(SQLCreateIndexStatement statement) {
        RdbIndexDomain domain = new RdbIndexDomain();
        domain.setSqlType(SecQueryType.CREATE_INDEX);
        domain.setAuditKind(SecQueryKind.CREATE);
        domain.setOptions(Collections.emptyMap());

        //tabDomain.setTableSchema(tabDomain.getSchema());
        domain.setTableName(extractName(((SQLExprTableSource) statement.getTable()).getName()));
        if (statement.getType() != null) {
            domain.setType(statement.getType());
        } else {
            domain.setType("index");
        }

        domain.setCatalog(null);
        //tabDomain.setSchema(tabDomain.getSchema());
        domain.setName(extractName(statement.getName()));
        domain.setComment(getString(statement.getComment()));

        domain.setColumns(new ArrayList<>());
        for (SQLSelectOrderByItem item : statement.getColumns()) {
            String columnName = getString(item.getExpr());
            domain.getColumns().add(columnName);
        }

        return Collections.singletonList(domain);
    }

    public List<RuleDomain> fromDropIndex(SQLDropIndexStatement statement) {
        RdbIndexDomain domain = new RdbIndexDomain();
        domain.setSqlType(SecQueryType.DROP_INDEX);
        domain.setAuditKind(SecQueryKind.DROP);
        domain.setOptions(Collections.emptyMap());

        //tabDomain.setTableSchema(tabDomain.getSchema());
        domain.setTableName(extractName(statement.getTableName().getName()));
        domain.setType("index");
        //tabDomain.setSchema(tabDomain.getSchema());
        domain.setName(extractName(statement.getIndexName()));

        return Collections.singletonList(domain);
    }

}
