package com.clougence.rdp.controller.model.fo;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/2/1 15:17
 */
@Getter
@Setter
public class UpsertUserConfigFO {

    /** key is configName */
    Map<String, String> updateConfigs;

    Map<String, String> needCreateConfigs;
}
