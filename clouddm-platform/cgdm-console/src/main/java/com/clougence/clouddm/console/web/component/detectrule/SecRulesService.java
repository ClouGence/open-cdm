package com.clougence.clouddm.console.web.component.detectrule;

import com.clougence.clouddm.sdk.service.secrules.SecRulesCheckerService;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public interface SecRulesService {

    void cleanCache(String checkerName);

    SecCheckerRules fetchCheckerRules(String ownerUid, long dsId);

    SecCheckerRules fetchCheckerRulesByDsId(long dsId);

    SecRulesCheckerService checkerSpi();

}
