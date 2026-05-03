package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.CallArgsDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCallDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class CallDomainBuilder extends AbstractDomainBuilder {

    private RdbCallDomain domain   = getDomain();

    private List<String>  nameList = new ArrayList<>();

    protected RdbCallDomain getDomain() {
        RdbCallDomain rdbCallDomain = new RdbCallDomain();
        rdbCallDomain.setChildren(new ArrayList<>());
        rdbCallDomain.setArgs(new ArrayList<>());
        return rdbCallDomain;
    }

    @Override
    public List<Domain> build() {
        if (domain.getArgs().isEmpty()) {
            domain.setEmptyArg(true);
        }
        domain.setFunc(true);
        domain.setSqlType(SecQueryType.CALL);
        domain.setAuditKind(SecQueryKind.CALL);

        Map<UmiTypes, String> map = BuilderUtil.parseFunctionName(nameList);
        domain.setCatalog(map.get(UmiTypes.Catalog));
        domain.setSchema(map.get(UmiTypes.Schema));
        domain.setCallName(map.get(UmiTypes.Function));

        return Collections.singletonList(domain);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource status) {
        // for sum(*)..
        if (status == DomainSource.COLUMN) {
            RdbColumnDomain rdbColumnDomain = (RdbColumnDomain) list.get(0);
            if (rdbColumnDomain.getColumn().equals("*")) {
                this.domain.addArgStr("*");
            }
        } else if (status == DomainSource.FUNCTION_ARGS) {
            CallArgsDomain callArgsDomain = (CallArgsDomain) list.get(0);
            for (Domain ruleDomain : callArgsDomain.getDomains()) {
                this.domain.addChild((RuleDomain) ruleDomain);
            }
            domain.setArgs(callArgsDomain.getArgs());
        } else if (status == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            this.nameList = objNameDomain.getNameList();
        } else {
            super.handleSubDomain(list, status);
        }
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.VALUE) {
            nameList.add((String) value);
        }
    }
}
