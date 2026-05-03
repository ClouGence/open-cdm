package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

/**
 * @Author: mode
 * @Date: 2024-11-20 11:00
 */
public enum RdbJoinType {

    NONE,
    LEFT_JOIN,
    RIGHT_JOIN,
    INNER_JOIN,
    CROSS_JOIN,

    //FULL_OUTER_JOIN,
    OTHER_JOIN,
}
