package com.clougence.clouddm.console.web.component.detectrule;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public interface SecRulesEngine {

    SecRulesCheckResult doQueryCheck(String ownerUid, String currentUid, String querySql, SecRulesCheckContext context);

}
