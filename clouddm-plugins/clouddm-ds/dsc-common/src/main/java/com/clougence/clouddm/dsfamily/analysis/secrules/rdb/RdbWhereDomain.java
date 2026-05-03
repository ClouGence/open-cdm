package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mode
 * @Date: 2024-11-20 11:00
 */
@Getter
@Setter
public abstract class RdbWhereDomain extends RdbQueryDomain {

    // for where
    private boolean      hasWhere;
    private List<String> whereColumns;
    private boolean      selectInWhere;

    // for join
    @Deprecated
    private RdbJoinType  joinType;

    // other mark
    private boolean      hasUnion;
    //    private boolean      hasExists;

    public void addWhereColumn(String whereCol) {
        if (whereCol == null || whereCol.isEmpty()) {
            return;
        }
        if (this.whereColumns == null) {
            this.whereColumns = new ArrayList<>();
        }
        if (!this.whereColumns.contains(whereCol)) {
            this.whereColumns.add(whereCol);
            this.hasWhere = true;
        }
    }
}
