package com.clougence.clouddm.console.web.service.security.mode;

import java.util.List;

import com.clougence.clouddm.sdk.model.analysis.TargetType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2022/3/23 18:42:25
 */
@Getter
@Setter
public class DmSecRuleConfig {

    private List<TargetType> ruleTargets;
    private List<TargetType> queryRangeExactTargets;
    private List<TargetType> queryRangePrefixTargets;
    private List<TargetType> queryRangeSuffixTargets;
    private List<TargetType> queryRangeIncludeTargets;

    private List<TargetType> senRangeExactTargets;
    private List<TargetType> senRangePrefixTargets;
    private List<TargetType> senRangeSuffixTargets;
    private List<TargetType> senRangeIncludeTargets;
}
