package com.clougence.clouddm.ds.clickhouse.analysis.builder;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChDeleteDomain;
import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChUpdateDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.AlterTableBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.schema.umi.struts.UmiTypes;

public class ChAlterTableBuilder extends AlterTableBuilder {

    @Override
    public List<Domain> build() {

        Map<UmiTypes, String> map = BuilderUtil.parseTableName(nameList);
        String catalog = map.get(UmiTypes.Catalog);
        String schema = map.get(UmiTypes.Schema);
        String table = map.get(UmiTypes.Table);

        for (Domain ruleDomain : ruleDomains) {
            if (ruleDomain instanceof RdbColumnDomain) {
                RdbColumnDomain columnDomain = (RdbColumnDomain) ruleDomain;
                columnDomain.setTable(table);
                columnDomain.setSchema(schema);
                columnDomain.setCatalog(catalog);
            } else if (ruleDomain instanceof RdbConstraintDomain) {
                RdbConstraintDomain constraintDomain = (RdbConstraintDomain) ruleDomain;
                constraintDomain.setTableCatalog(catalog);
                constraintDomain.setTableSchema(schema);
                constraintDomain.setTableName(table);
            } else if (ruleDomain instanceof RdbTableDomain) {
                RdbTableDomain tableDomain = (RdbTableDomain) ruleDomain;
                tableDomain.setTable(table);
                tableDomain.setSchema(schema);
                tableDomain.setCatalog(catalog);
            } else if (ruleDomain instanceof ChUpdateDomain) {
                ChUpdateDomain updateDomain = (ChUpdateDomain) ruleDomain;
                updateDomain.setTable(table);
                updateDomain.setSchema(schema);
                updateDomain.setCatalog(catalog);
            } else if (ruleDomain instanceof RdbIndexDomain) {
                RdbIndexDomain indexDomain = (RdbIndexDomain) ruleDomain;
                indexDomain.setTableCatalog(catalog);
                indexDomain.setTableSchema(schema);
                indexDomain.setTableName(table);
            } else if (ruleDomain instanceof ChDeleteDomain) {
                ChDeleteDomain deleteDomain = (ChDeleteDomain) ruleDomain;
                deleteDomain.setTable(table);
                deleteDomain.setSchema(schema);
                deleteDomain.setCatalog(catalog);
            }
        }
        return ruleDomains;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.UPDATE) {
            this.ruleDomains.addAll(list);
        } else if (source == DomainSource.DELETE) {
            this.ruleDomains.addAll(list);
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
