package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.NameType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ColumnListDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ConstraintTypeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.SqlConstraintType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class CreateIndexBuilder extends AbstractDomainBuilder {

    protected RdbIndexDomain indexDomain = getIndexDomain();

    protected RdbIndexDomain getIndexDomain() { return new RdbIndexDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.COMMENT) {
            indexDomain.setComment((String) value);
        } else if (attr == CommonAttribute.INDEX_TYPE) {
            indexDomain.setType((String) value);
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            if (objNameDomain.getType() == NameType.INDEX) {
                indexDomain.setName(objNameDomain.getName());
            } else {
                Map<UmiTypes, String> map = BuilderUtil.parseTableName(objNameDomain.getNameList());
                indexDomain.setTableCatalog(map.get(UmiTypes.Catalog));
                indexDomain.setTableSchema(map.get(UmiTypes.Schema));
                indexDomain.setTableName(map.get(UmiTypes.Table));
            }
        } else if (source == DomainSource.COLUMN_LIST) {
            ColumnListDomain columnListDomain = (ColumnListDomain) list.get(0);
            indexDomain.setColumns(columnListDomain.getColumns());
        } else if (source == DomainSource.CONSTRAINT_TYPE) {
            ConstraintTypeDomain constraintTypeDomain = (ConstraintTypeDomain) list.get(0);
            if (constraintTypeDomain.getConstraintType() == SqlConstraintType.Unique) {
                indexDomain.setType("unique");
            } else {
                super.handleSubDomain(list, source);
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public List<Domain> build() {
        indexDomain.setSqlType(SecQueryType.CREATE_INDEX);
        indexDomain.setAuditKind(SecQueryKind.CREATE);
        if (StringUtils.isEmpty(indexDomain.getType())) {
            indexDomain.setType("index");
        }
        return Collections.singletonList(indexDomain);
    }
}
