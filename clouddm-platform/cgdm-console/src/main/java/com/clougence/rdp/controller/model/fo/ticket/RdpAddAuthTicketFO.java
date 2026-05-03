package com.clougence.rdp.controller.model.fo.ticket;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.clougence.clouddm.sdk.security.auth.AuthKind;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdpAddAuthTicketFO {

    @NotNull
    private AuthKind        authKind;
    @NotNull
    private List<ApplyAuth> applyAuths;
}
