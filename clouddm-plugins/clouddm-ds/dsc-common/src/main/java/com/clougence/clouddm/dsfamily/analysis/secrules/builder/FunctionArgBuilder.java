package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.CallArgsDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class FunctionArgBuilder extends AbstractDomainBuilder {

    private CallArgsDomain callArgsDomain = new CallArgsDomain();

    @Override
    public List<Domain> build() {
        return Collections.singletonList(callArgsDomain);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource status) {
        // for cast( id as TYPE)
        if (status == DomainSource.COLUMN_TYPE) {
            return;
        }

        if (status == DomainSource.SELECT) {
            RdbSelectDomain domain = (RdbSelectDomain) list.get(0);
            domain.setMode(RdbQueryMode.SUB_CALL);
            this.callArgsDomain.getDomains().addAll(list);
            return;
        }
        if (status != DomainSource.CONSTANT && status != DomainSource.FUNCTION && status != DomainSource.COLUMN && status != DomainSource.VARIABLE) {
            super.handleSubDomain(list, status);
        }
        this.callArgsDomain.getDomains().addAll(list);
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.VALUE || attr == CommonAttribute.FUNC_ARG_NAME) {
            callArgsDomain.getArgs().add((String) value);
        }
    }

}
