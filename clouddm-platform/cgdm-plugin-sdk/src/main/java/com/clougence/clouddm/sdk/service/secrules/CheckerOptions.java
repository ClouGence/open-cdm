package com.clougence.clouddm.sdk.service.secrules;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
@Getter
@Setter
public class CheckerOptions {

    private DataSourceType      dsType;
    private Requester           requester;
    private Map<String, String> parameters;
}
