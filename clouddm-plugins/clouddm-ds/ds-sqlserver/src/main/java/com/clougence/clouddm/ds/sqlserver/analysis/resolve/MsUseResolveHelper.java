package com.clougence.clouddm.ds.sqlserver.analysis.resolve;

import java.util.Collections;
import java.util.List;

import com.alibaba.druid.sql.ast.statement.SQLUseStatement;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSchemaDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class MsUseResolveHelper extends MsAbstractResolveHelper {

    public List<RuleDomain> fromSwitchCatalog(SQLUseStatement statement) {
        RdbSchemaDomain domain = new RdbSchemaDomain();
        domain.setSqlType(SecQueryType.SWITCH_CATALOG);
        domain.setAuditKind(SecQueryKind.OTHER);
        domain.setOptions(Collections.emptyMap());

        domain.setSchema(getString(statement.getDatabase()));

        return Collections.singletonList(domain);
    }
}
