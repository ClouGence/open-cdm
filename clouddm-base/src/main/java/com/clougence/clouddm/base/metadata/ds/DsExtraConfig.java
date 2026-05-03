package com.clougence.clouddm.base.metadata.ds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2022/8/10 09:55:51
 *
 * replace com.clougence.cloudcanal.console.model.entity.DataSourceExtraDO
 */
@Getter
@Setter
@Deprecated
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DsExtraConfig {
}
