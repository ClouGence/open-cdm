package com.clougence.rdp.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.model.cloud.RdpMarketSubDO;

/**
 * @author wanshao create time is 2021/1/5
 **/
@Deprecated
public interface RdpMarketSubMapper extends BaseMapper<RdpMarketSubDO> {

    RdpMarketSubDO queryAwsMarketSub(String awsCustomId, String awsProductCode, String awsAccountId);

    RdpMarketSubDO queryByAwsMarketInfo(String awsCustomId, String awsProductCode);

    void updateSubStatusById(String subStatus, Long id);
}
