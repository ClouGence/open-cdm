package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

import lombok.Getter;
import lombok.Setter;

/**
 * include DML and DQL basic
 * @Author: mode
 * @Date: 2024-11-20 11:00
 */
@Getter
@Setter
public abstract class RdbQueryDomain extends RuleDomain {

    private boolean hasWith;
}
