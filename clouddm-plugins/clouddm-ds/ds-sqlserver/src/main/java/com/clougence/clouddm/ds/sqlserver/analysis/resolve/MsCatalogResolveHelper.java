package com.clougence.clouddm.ds.sqlserver.analysis.resolve;

import java.util.Collections;
import java.util.List;

import com.alibaba.druid.sql.ast.statement.SQLCreateDatabaseStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropDatabaseStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerExecStatement;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsCatalogDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class MsCatalogResolveHelper extends MsAbstractResolveHelper {

    public List<RuleDomain> fromCreateCatalog(SQLCreateDatabaseStatement statement) {
        MsCatalogDomain domain = new MsCatalogDomain();
        domain.setSqlType(SecQueryType.CREATE_CATALOG);
        domain.setAuditKind(SecQueryKind.CREATE);
        domain.setOptions(Collections.emptyMap());

        domain.setCatalog(extractName(statement.getName()));
        if (statement.getComment() != null) {
            domain.setComment(statement.getComment().toString());

        }
        return Collections.singletonList(domain);
    }

    public List<RuleDomain> fromDropCatalog(SQLDropDatabaseStatement statement) {
        MsCatalogDomain domain = new MsCatalogDomain();
        domain.setSqlType(SecQueryType.DROP_CATALOG);
        domain.setAuditKind(SecQueryKind.DROP);
        domain.setOptions(Collections.emptyMap());

        domain.setCatalog(extractName(statement.getName()));

        if (statement.isIfExists()) {
            domain.setIfExists(true);
        }

        return Collections.singletonList(domain);
    }

    public List<RuleDomain> fromRenameCatalog(SQLServerExecStatement statement) {
        MsCatalogDomain domain = new MsCatalogDomain();
        domain.setSqlType(SecQueryType.RENAME_CATALOG);
        domain.setAuditKind(SecQueryKind.ALTER);
        domain.setOptions(Collections.emptyMap());

        domain.setCatalog(extractName(statement.getParameters().get(0).getExpr().toString()));
        domain.setNewName(extractName(statement.getParameters().get(1).getExpr().toString()));

        return Collections.singletonList(domain);
    }
}
