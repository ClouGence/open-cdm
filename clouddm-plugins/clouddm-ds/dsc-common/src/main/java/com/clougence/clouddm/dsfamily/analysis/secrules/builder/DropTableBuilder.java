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

public abstract class DropTableBuilder<T extends RdbTableDomain> extends AbstractDomainBuilder {

    protected List<Domain> tableDomains = new ArrayList<>();

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            RdbTableDomain tableDomain = getTableDomain();

            tableDomain.setSqlType(SecQueryType.DROP_TABLE);
            tableDomain.setAuditKind(SecQueryKind.DROP);

            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);

            List<String> nameList = objNameDomain.getNameList();
            Map<UmiTypes, String> map = BuilderUtil.parseTableName(nameList);
            tableDomain.setCatalog(map.get(UmiTypes.Catalog));
            tableDomain.setSchema(map.get(UmiTypes.Schema));
            tableDomain.setTable(map.get(UmiTypes.Table));

            this.tableDomains.add(tableDomain);
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public List<Domain> build() {
        return tableDomains;
    }

    protected abstract T getTableDomain();
}
