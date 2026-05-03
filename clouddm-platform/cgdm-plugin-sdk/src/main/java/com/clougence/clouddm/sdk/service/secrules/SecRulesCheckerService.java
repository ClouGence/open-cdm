package com.clougence.clouddm.sdk.service.secrules;

import java.util.List;

import com.clougence.clouddm.sdk.service.Service;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public interface SecRulesCheckerService extends Service {

    List<SecParam> getParameters(String scriptType, String script);

    SecResult doChecker(CheckerRule checker, CheckerData domain, CheckerOptions options);
}
