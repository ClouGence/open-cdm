package com.clougence.clouddm.sdk.analysis.secrules;

import java.util.List;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.model.analysis.TargetType;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public interface SecRulesSupportSpi extends Spi {

    boolean isSupport();

    List<TargetType> supportModel();

    List<TargetType> exactRangeForQuery();

    List<TargetType> prefixRangeForQuery();

    List<TargetType> suffixRangeForQuery();

    List<TargetType> includeRangeForQuery();

    List<TargetType> exactRangeForSen();

    List<TargetType> prefixRangeForSen();

    List<TargetType> suffixRangeForSen();

    List<TargetType> includeRangeForSen();
}
