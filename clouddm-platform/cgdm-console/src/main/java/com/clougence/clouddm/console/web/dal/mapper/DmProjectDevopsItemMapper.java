package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.model.DmProjectDevopsItemDO;

/**
 * @author mode create time is 2020/3/2
 **/
public interface DmProjectDevopsItemMapper extends BaseMapper<DmProjectDevopsItemDO> {

    List<DmProjectDevopsItemDO> queryItemByDevopsId(String ownerUid, long devopsId);

    int deleteItemByDevopsId(String ownerUid, long devopsId);
}
