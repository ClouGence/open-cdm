package com.clougence.rdp.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.enumeration.mfa.MfaStatus;
import com.clougence.rdp.dal.model.RdpUserMfaDO;

/**
 * @author wanshao create time is 2021/1/5
 **/
public interface RdpUserMfaMapper extends BaseMapper<RdpUserMfaDO> {

    RdpUserMfaDO queryByUid(String uid);

    void updateById(Long id, String mfaKey, MfaStatus mfaStatus);

    void updateStatusById(Long id, MfaStatus mfaStatus);

    void emptyResetMfaKeyById(Long id);

    void updateResetMfaKeyById(Long id, String resetMfaKey);

    /**
     * For sub-account.
     */
    void deleteByUid(String uid);
}
