package com.clougence.rdp.dal.mapper.authcode;

import java.util.Set;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.model.authcode.RdpAuthVersionFieldDO;

public interface RdpAuthVersionFieldMapper extends BaseMapper<RdpAuthVersionFieldDO> {

    Set<String> queryAllVersion();

    RdpAuthVersionFieldDO getFieldByVersion(String applyCodeVersion);
}
