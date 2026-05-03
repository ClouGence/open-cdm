package com.clougence.clouddm.console.web.service.project;

import java.util.List;

import com.clougence.clouddm.console.web.dal.enumeration.ImType;
import com.clougence.clouddm.console.web.dal.model.DmMessengerDO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsImAddFO;
import com.clougence.clouddm.console.web.model.fo.project.DevopsImUpdateFO;
import com.clougence.clouddm.console.web.service.project.domain.DmImDef;

public interface DmImService {

    List<DmImDef> getImDefList();

    List<DmMessengerDO> queryImList(String ownerUid);

    List<DmMessengerDO> queryMessengerByOwnerAndType(String ownerUid, ImType imType);

    DmMessengerDO queryImById(String ownerUid, long imId);

    void addIm(String ownerUid, DevopsImAddFO fo);

    void deleteImById(String ownerUid, long imId);

    void updateImById(String ownerUid, DevopsImUpdateFO fo);

    void testImByConfig(String ownerUid, DevopsImAddFO fo);
}
