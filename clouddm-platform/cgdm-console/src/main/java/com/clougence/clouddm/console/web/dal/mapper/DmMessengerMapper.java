package com.clougence.clouddm.console.web.dal.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.console.web.dal.enumeration.ImType;
import com.clougence.clouddm.console.web.dal.model.DmMessengerDO;

/**
 * @author mode
 * @since 1.1.3
 */
public interface DmMessengerMapper extends BaseMapper<DmMessengerDO> {

    List<DmMessengerDO> queryMessengerByOwnerAndType(String ownerUid, ImType imType);

    List<DmMessengerDO> queryMessengerByOwner(String ownerUid);

    DmMessengerDO queryImById(String ownerUid, long imId);

    void deleteByOwnerAndId(String ownerUid, long imId);

    void updateDisplayByOwnerAndId(String ownerUid, long imId, String newData);

    void updateWebhookUrlByOwnerAndId(String ownerUid, long imId, String newData, String newSecret);
}
