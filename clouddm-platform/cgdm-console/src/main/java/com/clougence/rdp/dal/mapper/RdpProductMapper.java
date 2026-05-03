package com.clougence.rdp.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.rdp.dal.model.RdpProductDO;

/**
 * @author wanshao create time is 2019/12/12 9:27 下午
 **/
@Deprecated
public interface RdpProductMapper extends BaseMapper<RdpProductDO> {

    RdpProductDO queryByProductType(String productType, String productVersionType);

    RdpProductDO queryByProductTypeAndVersion(String productType, String productVersionType, String productVersion);
}
