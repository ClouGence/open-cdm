package com.clougence.clouddm.console.web.component.dsconfig;

import com.clougence.rdp.controller.model.vo.DriverVersionStatusVO;

public interface DmDriverService {

    DriverVersionStatusVO checkDriverStatus(Long clusterId, String driverFamily, String driverVersion);

    void downloadDriver(String uid, Long clusterId, String driverFamily, String driverVersion);
}
