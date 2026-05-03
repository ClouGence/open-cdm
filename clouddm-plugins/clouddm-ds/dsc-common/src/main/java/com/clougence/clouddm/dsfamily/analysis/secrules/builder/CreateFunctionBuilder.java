package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbFunctionDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CreateFunctionBuilder extends AbstractDomainBuilder {

    private RdbFunctionDomain rdbFunctionDomain = new RdbFunctionDomain();

    @Override
    public List<Domain> build() {
        rdbFunctionDomain.setSqlType(SecQueryType.CREATE_FUNCTION);
        rdbFunctionDomain.setAuditKind(SecQueryKind.CREATE);

        return Collections.singletonList(rdbFunctionDomain);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            Map<UmiTypes, String> map = BuilderUtil.parseFunctionName(objNameDomain.getNameList());
            rdbFunctionDomain.setCatalog(map.get(UmiTypes.Catalog));
            rdbFunctionDomain.setSchema(map.get(UmiTypes.Schema));
            rdbFunctionDomain.setName(map.get(UmiTypes.Function));
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
