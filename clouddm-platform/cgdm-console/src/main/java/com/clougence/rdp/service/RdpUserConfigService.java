package com.clougence.rdp.service;

import java.util.List;
import java.util.Map;

import com.clougence.rdp.constant.UserConfigTagType;
import com.clougence.rdp.controller.model.fo.UpsertUserConfigFO;
import com.clougence.rdp.controller.model.lo.UpsertUserConfigLO;
import com.clougence.rdp.controller.model.vo.RdpUserConfigVO;
import com.clougence.rdp.dal.model.RdpUserKvBaseConfigDO;
import com.clougence.rdp.global.config.user.UserDefinedConfig;

/**
 * @author bucketli 2022/1/10 20:18:18
 */
public interface RdpUserConfigService {

    UserDefinedConfig fetchPriUserConfig(String uid);

    List<RdpUserConfigVO> getAllConfig(String uid);

    List<RdpUserConfigVO> queryUserConfigVosWithNewEntries(String uid);

    /**
     * @return key is configName, value is config detail.
     */
    Map<String, RdpUserConfigVO> queryWithNewEntriesAndSpecifiedConfs(String uid, List<String> configs);

    List<RdpUserKvBaseConfigDO> getSpecifiedConfigs(String uid, List<String> configNames);

    RdpUserKvBaseConfigDO getDefaultClusterName(String uid);

    RdpUserKvBaseConfigDO getSpecifiedConfig(String uid, String configName);

    List<RdpUserConfigVO> queryOneConfigTypeByUid(String uid, UserConfigTagType type);

    List<UpsertUserConfigLO> upsertConfigValue(String ownerUid, UpsertUserConfigFO config);

    void initUserConfigs(String uid);

    void initSubAccountConfigs(String uid);
}
