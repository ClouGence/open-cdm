package com.clougence.clouddm.platform.dal.access;

import com.clougence.clouddm.platform.dal.mapper.monitor.*;

public interface MonitorDal {

    DmMonAlertConfigDetailMapper alertConfigDetailMapper();

    DmMonAlertEventLogMapper alertEventLogMapper();

    DmMonBizLogMapper bizLogMapper();

    DmMonOpAuditMapper opAuditMapper();

    DmMonWebViewLogMapper webViewLogMapper();

    // ---------- dal service methods ----------
}
