package com.clougence.schema.umi.special.rdb;

import java.util.*;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.schema.umi.struts.ConstraintUmiData;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.schema.umi.struts.constraint.GeneralConstraintType;
import com.clougence.utils.CollectionUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

/**
 * 结构类型
 * 
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2020-05-21
 */
@Getter
@Setter
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public class RdbTable extends ConstraintUmiData implements Value {

    private String                   catalog;
    private String                   schema;
    private String                   name;
    private String                   alias;
    private String                   comment;
    private UmiTypes                 umiType;

    private String                   tableType;
    private Map<String, RdbColumn>   columns;

    private RdbPrimaryKey            primaryKey;

    private List<RdbUniqueKey>       uniqueKeys;
    private List<RdbIndex>           indices;
    private List<RdbForeignKey>      foreignKeys;
    private RdbPartition             partition;

    // Although the structure is the same，but unique constraint and unique index is diff
    private List<RdbUniqueKey>       uniqueConstraints;
    private List<RdbCheckConstraint> checkConstraints;

    public RdbTable(){
        this.umiType = UmiTypes.Table;
    }

    @Override
    public String asValue() {
        return getName();
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public void setPrimaryKey(RdbPrimaryKey primaryKey) {
        this.primaryKey = primaryKey;

        if (primaryKey == null) {
            super.overwriteConstraint(GeneralConstraintType.Primary, Collections.emptyList());
        } else {
            super.overwriteConstraint(GeneralConstraintType.Primary, Collections.singletonList(primaryKey));
        }
    }

    public void setUniqueKeys(List<RdbUniqueKey> uniqueKeys) {
        this.uniqueKeys = uniqueKeys;

        if (CollectionUtils.isEmpty(uniqueKeys)) {
            super.overwriteConstraint(GeneralConstraintType.Unique, Collections.emptyList());
        } else {
            super.overwriteConstraint(GeneralConstraintType.Unique, uniqueKeys);
        }
    }

    public void addIndex(RdbIndex index) {
        if (index == null) {
            return;
        }

        if (this.indices == null) {
            this.indices = new ArrayList<>();
        }

        this.indices.add(index);
    }

    public void addColumn(RdbColumn rdbColumn) {
        if (rdbColumn == null) {
            return;
        }

        if (this.columns == null) {
            this.columns = new LinkedHashMap<>();
        }

        this.columns.put(rdbColumn.getName(), rdbColumn);
    }

    public void addUniqueKey(RdbUniqueKey uniqueKey) {
        if (uniqueKey == null) {
            return;
        }

        if (this.uniqueKeys == null) {
            this.uniqueKeys = new ArrayList<>();
        }

        this.uniqueKeys.add(uniqueKey);
        super.addConstraint(uniqueKey);
    }

    public void addUniqueConstraint(RdbUniqueKey uniqueKey) {
        if (uniqueKey == null) {
            return;
        }

        if (this.uniqueConstraints == null) {
            this.uniqueConstraints = new ArrayList<>();
        }

        this.uniqueConstraints.add(uniqueKey);
        super.addConstraint(uniqueKey);
    }

    public void addCheckConstraint(RdbCheckConstraint checkConstraint) {
        if (checkConstraint == null) {
            return;
        }

        if (this.checkConstraints == null) {
            this.checkConstraints = new ArrayList<>();
        }

        this.checkConstraints.add(checkConstraint);
        super.addConstraint(checkConstraint);
    }

    public void addForeignKey(RdbForeignKey foreignKey) {
        if (foreignKey == null) {
            return;
        }

        if (this.foreignKeys == null) {
            this.foreignKeys = new ArrayList<>();
        }

        this.foreignKeys.add(foreignKey);
    }
}
