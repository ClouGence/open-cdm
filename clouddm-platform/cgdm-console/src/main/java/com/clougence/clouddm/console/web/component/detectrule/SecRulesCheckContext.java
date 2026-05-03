package com.clougence.clouddm.console.web.component.detectrule;

import com.clougence.clouddm.console.web.dal.enumeration.WarnLevel;
import com.clougence.clouddm.sdk.service.secrules.Requester;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Getter
@Setter
@Builder
public class SecRulesCheckContext {

    // for CodeLocation
    private int       basicCodeLine;
    private int       basicCodeColumn;

    // env info
    private long      dsId;
    private String    currentUID;
    private String    currentCatalog;
    private String    currentSchema;
    private Requester requester;

    // parameter
    private WarnLevel unsupportedLevel;
}
