package com.clougence.clouddm.sdk.analysis.secrules;

import java.util.List;

import com.clougence.clouddm.sdk.service.Service;
import com.clougence.clouddm.sdk.service.secrules.*;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public interface SecRulesCheckerSpi extends Service {

    List<SecParam> getParameters(String scriptType, String script);

    SecResult doChecker(CheckerRule checker, CheckerData domain, CheckerOptions options);
}
