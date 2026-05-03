package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

/**
 * for update set value
 */
public class SetValueBuilder extends AbstractDomainBuilder {

    protected List<Domain> values = new ArrayList<>();

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            RdbColumnDomain rdbColumnDomain = new RdbColumnDomain();
            rdbColumnDomain.setColumn(objNameDomain.getNameList().get(objNameDomain.getNameList().size() - 1));
            values.add(rdbColumnDomain);
        } else if (source == DomainSource.SELECT) {
            values.add(list.get(0));
        } else if (source == DomainSource.FUNCTION) {
            values.add(list.get(0));
        } else if (source == DomainSource.COLUMN) {
            values.add(list.get(0));
        } else if (source == DomainSource.CONSTANT) {
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public List<Domain> build() {
        return values;
    }
}
