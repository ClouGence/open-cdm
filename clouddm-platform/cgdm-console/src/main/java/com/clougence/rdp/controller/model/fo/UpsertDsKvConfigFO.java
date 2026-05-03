package com.clougence.rdp.controller.model.fo;

import java.util.Map;

import lombok.Data;

/**
 * @author bucketli 2021/2/1 15:17
 */
@Data
public class UpsertDsKvConfigFO {

    long                dataSourceId;

    Map<String, String> updateConfigs;

    Map<String, String> needCreateConfigs;
}
