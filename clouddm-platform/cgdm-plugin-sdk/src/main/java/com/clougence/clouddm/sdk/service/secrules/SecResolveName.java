package com.clougence.clouddm.sdk.service.secrules;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.model.analysis.TargetType;

public interface SecResolveName {

    //    String resolveName(TargetType targetType);

    List<Map<TargetType, String>> resolveResource();
}
