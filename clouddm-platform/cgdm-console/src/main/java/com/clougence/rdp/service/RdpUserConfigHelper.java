package com.clougence.rdp.service;

import java.util.List;
import java.util.Map;

import com.clougence.rdp.dal.model.RdpUserKvBaseConfigDO;

/**
 * @author bucketli 2020/11/7 18:01
 */
public interface RdpUserConfigHelper {

    void fillFieldValue(Object instance, Map<String, String> configMap);

    List<RdpUserKvBaseConfigDO> collectConfigs(Object instance, String uid);
}
