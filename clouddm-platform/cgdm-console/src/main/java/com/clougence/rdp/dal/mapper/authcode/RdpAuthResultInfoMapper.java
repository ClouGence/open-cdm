package com.clougence.rdp.dal.mapper.authcode;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.model.authcode.RdpAuthResultInfoDO;

@Deprecated
public interface RdpAuthResultInfoMapper extends BaseMapper<RdpAuthResultInfoDO> {

    RdpAuthResultInfoDO queryCurrentResult();

    List<RdpAuthResultInfoDO> queryAllAuthResult();

    void deleteByIds(List<Long> ids);
}
