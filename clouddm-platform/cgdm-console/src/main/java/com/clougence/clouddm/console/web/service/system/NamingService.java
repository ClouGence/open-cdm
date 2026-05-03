package com.clougence.clouddm.console.web.service.system;

import com.clougence.clouddm.console.web.dal.enumeration.RuleKind;

/**
 * @author bucketli 2020/7/10 21:14
 */
public interface NamingService {

    String genLocalClusterName();

    String genClusterName();

    String genTicketBizId();

    String genDefaultClusterName();

    String genWorkerSequenceNumber();

    String genWorkerName();

    String genAccessKey();

    String genSecretKey();

    String genUid();

    /**
     * @return random password for inner user
     */
    String genInnerUserPwd();

    String genSecRuleName(RuleKind ruleKind);
}
