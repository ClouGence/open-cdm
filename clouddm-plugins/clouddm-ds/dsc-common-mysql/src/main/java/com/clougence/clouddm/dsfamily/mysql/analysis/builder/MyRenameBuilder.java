package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.AbstractDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.NameType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyTableDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class MyRenameBuilder extends AbstractDomainBuilder {

    protected TargetType   targetType;

    protected List<String> oldNameList;
    protected List<String> newNameList;

    public MyRenameBuilder(TargetType targetType){
        this.targetType = (targetType);
    }

    @Override
    public List<Domain> build() {
        if (targetType == TargetType.Table) {
            MyTableDomain tableDomain = new MyTableDomain();
            tableDomain.setSqlType(SecQueryType.RENAME_TABLE);
            tableDomain.setAuditKind(SecQueryKind.ALTER);
            Map<UmiTypes, String> map = BuilderUtil.parseTableName(oldNameList);
            tableDomain.setSchema(map.get(UmiTypes.Schema));
            tableDomain.setTable(map.get(UmiTypes.Table));
            Map<UmiTypes, String> newName = BuilderUtil.parseTableName(newNameList);
            tableDomain.setNewName(newName.get(UmiTypes.Table));
            if (newName.get(UmiTypes.Schema) == null) {
                tableDomain.setNewSchemaName(map.get(UmiTypes.Schema));
            } else {
                tableDomain.setNewSchemaName(newName.get(UmiTypes.Schema));
            }
            return Collections.singletonList(tableDomain);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            Domain domain = list.get(0);
            ObjNameDomain objNameDomain = (ObjNameDomain) domain;
            if (objNameDomain.getType() == NameType.TABLE) {
                this.oldNameList = objNameDomain.getNameList();
            } else if (objNameDomain.getType() == NameType.NEW_TABLE) {
                this.newNameList = objNameDomain.getNameList();
            } else {
                super.handleSubDomain(list, source);
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
