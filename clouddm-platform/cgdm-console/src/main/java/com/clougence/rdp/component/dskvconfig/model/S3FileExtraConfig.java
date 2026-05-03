package com.clougence.rdp.component.dskvconfig.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2022/8/10 09:33:48
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class S3FileExtraConfig extends FileExtraConfig {
}
