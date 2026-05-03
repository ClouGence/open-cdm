package com.clougence.clouddm.sdk.service.approval;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdpApprovalTicketInfo {

    private String approIdentity;
    private String providerType; // see: ApprovalProvider
    private String ownerUid;
}
