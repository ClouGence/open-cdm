package com.clougence.clouddm.sdk.approval;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ApprovalForm {

    private String ticketUserPhone;
    private String ticketTitle;
    private String ticketDesc;

    private String templateIdentity;
}
