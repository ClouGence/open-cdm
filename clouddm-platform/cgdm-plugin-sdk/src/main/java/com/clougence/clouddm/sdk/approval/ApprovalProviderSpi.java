package com.clougence.clouddm.sdk.approval;

import java.util.List;

import com.clougence.clouddm.sdk.LifeSpi;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;

public interface ApprovalProviderSpi extends LifeSpi {

    // create a third-party approval process and return jump url,approval identity and approval activity list
    ApprovalCreateInstanceResult createApprovalInstance(String ownerUid, ApprovalForm form) throws ThirdPartyApiException;

    // get the latest information
    ApprovalInstanceInfo getLastInfo(String ownerUid, String identity) throws ThirdPartyApiException;

    // get approval template list
    List<ApprovalTemplate> getTemplates(String ownerUid) throws ThirdPartyApiException;

    void useTemplate(String ownerUid, String before, String current) throws ThirdPartyApiException;

    // get user info
    UserDetail getUserDetailByUid(String ownerUid, String targetUserId) throws ThirdPartyApiException;

    // cancel third party approval process
    void cancelApprovalInst(String ownerUid, CancelInstanceInfo info) throws ThirdPartyApiException;
}
