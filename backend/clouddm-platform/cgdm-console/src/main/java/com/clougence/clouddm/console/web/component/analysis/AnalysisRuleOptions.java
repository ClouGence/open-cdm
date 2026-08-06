/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.console.web.component.analysis;

import java.util.Map;

import com.clougence.clouddm.platform.dal.model.secrule.WarnLevel;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.schema.umi.struts.UmiTypes;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalysisRuleOptions {

    private final String                currentUid;
    private final long                  dsId;
    private final Map<UmiTypes, Object> levels;
    private final Requester             requester;
    private final WarnLevel             unsupportedLevel;
}
