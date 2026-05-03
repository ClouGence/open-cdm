package com.clougence.rdp.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.enumeration.RegisterStatus;
import com.clougence.rdp.dal.model.RdpSsoRegisterDO;

@Deprecated // delete in the future, SSO replace with LoginProviderSpi in the future
public interface RdpSsoRegisterMapper extends BaseMapper<RdpSsoRegisterDO> {

    RdpSsoRegisterDO queryByRequestId(String requestId);

    void updateStatusByRequestId(String requestId, RegisterStatus status);
}
