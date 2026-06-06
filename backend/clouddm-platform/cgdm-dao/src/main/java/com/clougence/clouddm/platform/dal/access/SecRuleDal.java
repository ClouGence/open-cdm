package com.clougence.clouddm.platform.dal.access;

import com.clougence.clouddm.platform.dal.mapper.secrule.*;

public interface SecRuleDal {

    DmSecRangeMapper rangeMapper();

    DmSecRefererMapper refererMapper();

    DmSecRulesMapper rulesMapper();

    DmSecSensitiveMapper sensitiveMapper();

    DmSecSpecMapper specMapper();

    // ---------- dal service methods ----------
}
