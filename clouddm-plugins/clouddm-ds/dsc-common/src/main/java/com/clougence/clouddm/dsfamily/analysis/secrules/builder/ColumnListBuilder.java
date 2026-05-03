package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ColumnListDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCallDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstantDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

/**
 * create index  sss（column1,column2） on table
 *                    ---------------
 */
public class ColumnListBuilder extends AbstractDomainBuilder {

    private List<String> nameList = new ArrayList<>();

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.VALUE) {
            this.nameList.add((String) value);
        }
    }

    @Override
    public List<Domain> build() {
        return Collections.singletonList(new ColumnListDomain(nameList));
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            nameList.add(objNameDomain.getLastName());
        } else if (source == DomainSource.COLUMN) {
            for (Domain domain : list) {
                RdbColumnDomain rdbColumnDomain = (RdbColumnDomain) domain;
                if (rdbColumnDomain.getSchema() != null) {
                    super.handleSubDomain(list, source);
                }
                nameList.add(rdbColumnDomain.getColumn());
            }
        } else if (source == DomainSource.FUNCTION) {
            for (Domain domain : list) {
                parseFunction((RdbCallDomain) domain);
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }

    private void parseFunction(RdbCallDomain domain) {
        for (RuleDomain child : domain.getChildren()) {
            if (child instanceof RdbColumnDomain) {
                nameList.add(((RdbColumnDomain) child).getColumn());
            } else if (child instanceof RdbCallDomain) {
                parseFunction((RdbCallDomain) child);
            } else if (child instanceof RdbConstantDomain) {

            } else {
                throw new UnsupportedOperationException("Unknown child type: " + child.getSqlType());
            }
        }
    }
}
