package com.clougence.clouddm.console.web.component.detectrule.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public interface QueryTypeHandler {

    void handleAfterSqlOperation(RuleDomain ruleDomain, Long dsId, Map<String, String> map, Date execTime);

    List<SecQueryType> canHandleType();
}
