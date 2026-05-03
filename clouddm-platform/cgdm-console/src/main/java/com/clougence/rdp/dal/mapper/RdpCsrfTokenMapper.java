package com.clougence.rdp.dal.mapper;

import java.util.Date;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.model.RdpCsrfTokenDO;

public interface RdpCsrfTokenMapper extends BaseMapper<RdpCsrfTokenDO> {

    int deleteByToken(String token);

    int deleteBeforeTime(Date date);

    RdpCsrfTokenDO queryByToken(String token);

    int updateToken(String token, RdpCsrfTokenDO data);
}
