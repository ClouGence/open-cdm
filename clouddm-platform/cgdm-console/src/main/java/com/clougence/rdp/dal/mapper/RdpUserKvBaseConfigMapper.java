package com.clougence.rdp.dal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.constant.UserConfigTagType;
import com.clougence.rdp.dal.model.RdpUserKvBaseConfigDO;

/**
 * @author bucketli 2022/1/10 19:51:40
 */
public interface RdpUserKvBaseConfigMapper extends BaseMapper<RdpUserKvBaseConfigDO> {

    RdpUserKvBaseConfigDO queryByUidAndConfigName(String uid, String configName);

    List<RdpUserKvBaseConfigDO> listOneConfigTypeByUid(String uid, UserConfigTagType type);

    List<RdpUserKvBaseConfigDO> listByUid(String uid);

    List<RdpUserKvBaseConfigDO> listByUidAndConfigNames(String uid, List<String> configNames);

    void updateUserConfig(String uid, String configName, String configValue);
}
