package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbFunctionDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class DropFunctionBuilder extends AbstractDomainBuilder {

    private RdbFunctionDomain functionDomain = new RdbFunctionDomain();

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
//            if (objNameDomain.getType() != NameType.FUNCTION) {
//                throw new UnsupportedOperationException("Unsupported obj name type: " + objNameDomain.getType());
//            }
            Map<UmiTypes, String> map = BuilderUtil.parseFunctionName(objNameDomain.getNameList());
            functionDomain.setCatalog(map.get(UmiTypes.Catalog));
            functionDomain.setSchema(map.get(UmiTypes.Schema));
            functionDomain.setName(map.get(UmiTypes.Function));
        }
    }

    @Override
    public List<Domain> build() {
        functionDomain.setAuditKind(SecQueryKind.DROP);
        functionDomain.setSqlType(SecQueryType.DROP_FUNCTION);
        return Collections.singletonList(functionDomain);
    }
}
