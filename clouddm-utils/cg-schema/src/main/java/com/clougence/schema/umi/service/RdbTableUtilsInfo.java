package com.clougence.schema.umi.service;

import java.util.List;
import java.util.Map;

import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbForeignKey;
import com.clougence.schema.umi.special.rdb.RdbIndex;
import com.clougence.schema.umi.special.rdb.RdbPartition;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.constraint.ConstraintObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdbTableUtilsInfo {

    private Map<String, List<RdbColumn>>            allColumns;
    private Map<String, Map<String, UmiConstraint>> pkUkMap;
    private Map<String, List<RdbForeignKey>>        fkMap;
    private Map<String, List<RdbIndex>>             indexMap;
    private Map<String, RdbPartition>               partitionMap;
    private Map<String, List<ConstraintObject>>     constraintMap;
}
