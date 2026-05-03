package com.clougence.clouddm.sdk.approval;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelInstanceInfo {

    private String ticketUserPhone;
    private String approvalInstanceIdentity;
    private String approvalTemplateCode;
}
