package com.clougence.clouddm.ds.maxcompute.analysis.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.maxcompute.analysis.builder.utils.McBuilderUtil;
import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.AbstractDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ColumnListDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class McAlterTableBuilder extends AbstractDomainBuilder {

    private Map<UmiTypes, String> map;
    private List<Domain>          ruleDomains = new ArrayList<>();
    private final boolean         schemaEnables;

    public McAlterTableBuilder(boolean schemaEnables){
        this.schemaEnables = schemaEnables;
    }

    @Override
    public List<Domain> build() {
        String table = map.get(UmiTypes.Table);
        String schema = map.get(UmiTypes.Schema);
        String catalog = map.get(UmiTypes.Catalog);

        for (Domain ruleDomain : ruleDomains) {
            if (ruleDomain instanceof RdbColumnDomain) {
                RdbColumnDomain columnDomain = (RdbColumnDomain) ruleDomain;
                columnDomain.setTable(table);
                columnDomain.setSchema(schema);
                columnDomain.setCatalog(catalog);
            } else if (ruleDomain instanceof RdbConstraintDomain) {
                RdbConstraintDomain constraintDomain = (RdbConstraintDomain) ruleDomain;
                constraintDomain.setTableSchema(schema);
                constraintDomain.setTableName(table);
                constraintDomain.setTableCatalog(catalog);
            } else if (ruleDomain instanceof RdbTableDomain) {
                RdbTableDomain indexDomain = (RdbTableDomain) ruleDomain;
                indexDomain.setTable(table);
                indexDomain.setSchema(schema);
                indexDomain.setCatalog(catalog);
            }
        }
        return new ArrayList<>(ruleDomains);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            Domain domain = list.get(0);
            ObjNameDomain objNameDomain = (ObjNameDomain) domain;
            this.map = McBuilderUtil.parseTableName(objNameDomain.getNameList(), schemaEnables);
        } else if (source == DomainSource.ALTER_TABLE_ITEM) {
            this.ruleDomains.addAll(list);
        } else if (source == DomainSource.COLUMN_DEF) {
            for (Domain domain : list) {
                RdbColumnDomain domainColumn = (RdbColumnDomain) domain;
                domainColumn.setAuditKind(SecQueryKind.CREATE);
                domainColumn.setSqlType(SecQueryType.ALTER_TABLE_ADD_COLUMN);
            }
            this.ruleDomains.addAll(list);
        } else if (source == DomainSource.COLUMN_LIST) {
            ColumnListDomain domain = (ColumnListDomain) list.get(0);
            for (String column : domain.getColumns()) {
                McColumnDomain rdbColumnDomain = new McColumnDomain();
                rdbColumnDomain.setAuditKind(SecQueryKind.DROP);
                rdbColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_DROP_COLUMN);
                rdbColumnDomain.setColumn(column);
                this.ruleDomains.add(rdbColumnDomain);
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }

}
