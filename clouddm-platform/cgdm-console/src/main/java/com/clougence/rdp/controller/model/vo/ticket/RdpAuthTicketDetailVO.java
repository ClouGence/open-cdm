package com.clougence.rdp.controller.model.vo.ticket;

import java.util.List;

import com.clougence.rdp.controller.model.fo.ticket.ApplyAuth;
import com.clougence.clouddm.sdk.security.auth.AuthKind;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdpAuthTicketDetailVO {

    private AuthKind        authKind;

    private List<ApplyAuth> applyAuths;

}
