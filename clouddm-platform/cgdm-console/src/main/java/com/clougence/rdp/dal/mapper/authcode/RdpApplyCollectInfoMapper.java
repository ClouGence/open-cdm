package com.clougence.rdp.dal.mapper.authcode;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.constant.ApplyCollectType;
import com.clougence.rdp.dal.model.authcode.RdpApplyCollectInfoDO;

public interface RdpApplyCollectInfoMapper extends BaseMapper<RdpApplyCollectInfoDO> {

    RdpApplyCollectInfoDO queryActiveInfoByName(Long applyId, Long jobId, Long taskId, String linkType, ApplyCollectType collectName);

    List<RdpApplyCollectInfoDO> queryListInfoByNameAndApplyId(Long applyId, ApplyCollectType collectName);

    void refreshInfos(Date expiredTime);

}
