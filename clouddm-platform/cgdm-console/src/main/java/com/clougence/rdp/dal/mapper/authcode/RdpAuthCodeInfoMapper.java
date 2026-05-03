package com.clougence.rdp.dal.mapper.authcode;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.model.authcode.RdpAuthCodeInfoDO;

public interface RdpAuthCodeInfoMapper extends BaseMapper<RdpAuthCodeInfoDO> {

    RdpAuthCodeInfoDO queryAuthCode();

}
