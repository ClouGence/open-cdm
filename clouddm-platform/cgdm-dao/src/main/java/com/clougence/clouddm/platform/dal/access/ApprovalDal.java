package com.clougence.clouddm.platform.dal.access;

import com.clougence.clouddm.platform.dal.mapper.approval.*;

public interface ApprovalDal {

    DmApprovalMapper approvalMapper();

    DmApprovalPersonMapper personMapper();

    DmApprovalProcessActivityMapper activityMapper();

    DmApprovalProcessMapper processMapper();

    DmApprovalTemplateMapper templateMapper();
}
