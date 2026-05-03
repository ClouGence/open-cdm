package com.clougence.rdp.dal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.constant.auth.CanBeReplaced;
import com.clougence.rdp.dal.model.RdpSysConfigDO;

/**
 * @author wanshao create time is 2020/9/8
 **/
public interface RdpSysConfigMapper extends BaseMapper<RdpSysConfigDO> {

    List<RdpSysConfigDO> queryByUid(String uid);

    List<RdpSysConfigDO> queryEmptyUidConf();

    void updateUserConfig(String uid, String configName, String configValue);

    RdpSysConfigDO queryByConfigName(String configName, @CanBeReplaced String uid);
}
