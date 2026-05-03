package com.clougence.clouddm.sdk.service.secrules;

import java.util.List;

import com.clougence.clouddm.sdk.model.analysis.TargetType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Getter
@Setter
public class CheckerRange {

    private TargetType   scope;
    private MatchMode    matchMode;
    private String       levelPrefix;
    private List<String> levelNodes;
    private boolean      chooseAll;
}
