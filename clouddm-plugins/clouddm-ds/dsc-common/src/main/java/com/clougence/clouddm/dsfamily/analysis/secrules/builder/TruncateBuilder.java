package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class TruncateBuilder<T extends RdbTableDomain> extends AbstractDomainBuilder {

    protected List<Domain> result = new ArrayList<>();

    protected T getTableDomain() { return (T) new RdbTableDomain(); }

    @Override
    public List<Domain> build() {
        return result;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain domain = (ObjNameDomain) list.get(0);
            Map<UmiTypes, String> map = BuilderUtil.parseTableName(domain.getNameList());
            T tableDomain = getTableDomain();
            tableDomain.setAuditKind(SecQueryKind.DML);
            tableDomain.setSqlType(SecQueryType.TRUNCATE);
            tableDomain.setCatalog(map.get(UmiTypes.Catalog));
            tableDomain.setSchema(map.get(UmiTypes.Schema));
            tableDomain.setTable(map.get(UmiTypes.Table));
            result.add(tableDomain);
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
