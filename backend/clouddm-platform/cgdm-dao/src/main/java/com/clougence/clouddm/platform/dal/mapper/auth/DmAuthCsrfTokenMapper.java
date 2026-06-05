package com.clougence.clouddm.platform.dal.mapper.auth;

import java.util.Date;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthCsrfTokenDO;

public interface DmAuthCsrfTokenMapper extends BaseMapper<DmAuthCsrfTokenDO> {

    int deleteByToken(String token);

    int deleteBeforeTime(Date date);

    DmAuthCsrfTokenDO queryByToken(String token);

    int updateToken(String token, DmAuthCsrfTokenDO data);
}
