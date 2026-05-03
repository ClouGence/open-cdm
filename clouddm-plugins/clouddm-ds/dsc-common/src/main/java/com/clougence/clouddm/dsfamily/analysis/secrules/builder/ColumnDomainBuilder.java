package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.schema.umi.struts.UmiTypes;

/**
 * for select ,update ,delete
 */
public class ColumnDomainBuilder extends AbstractDomainBuilder {

    private RdbColumnDomain columnDomain;

    private List<String>    nameList = new ArrayList<>();

    public ColumnDomainBuilder(){
        columnDomain = new RdbColumnDomain();
    }

    @Override
    public List<Domain> build() {
        Map<UmiTypes, String> map = BuilderUtil.parseColumnName(nameList);
        columnDomain.setCatalog(map.get(UmiTypes.Catalog));
        columnDomain.setSchema(map.get(UmiTypes.Schema));
        columnDomain.setTable(map.get(UmiTypes.Table));
        columnDomain.setColumn(map.get(UmiTypes.Column));

        return Collections.singletonList(columnDomain);
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.ALIAS) {
            this.columnDomain.setNewName(value.toString());
        } else if (attr == CommonAttribute.VALUE) {
            this.nameList.add((String) value);
        }
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        // for *
        if (source == DomainSource.COLUMN) {
            RdbColumnDomain ruleDomain = (RdbColumnDomain) list.get(0);
            this.nameList.add(ruleDomain.getColumn());
        } else if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            this.nameList.addAll(objNameDomain.getNameList());
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
