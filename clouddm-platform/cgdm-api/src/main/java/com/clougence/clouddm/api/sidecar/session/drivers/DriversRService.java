package com.clougence.clouddm.api.sidecar.session.drivers;

import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.RSocketSendDTO;

/**
 * @author mode 2021/1/14 14:10
 */
@RSocketApiClass
public interface DriversRService {

    DsDriverVer refreshDriverVersion(RSocketSendDTO sendDTO, String familyName, String version);

    void deleteDriverResource(RSocketSendDTO sendDTO, String familyName, String version);

    void transferDriverResource(RSocketSendDTO sendDTO, String familyName, String version, String fileName, long offset, byte[] content);
}
