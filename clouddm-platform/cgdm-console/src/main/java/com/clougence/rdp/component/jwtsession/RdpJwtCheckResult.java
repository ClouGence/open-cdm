package com.clougence.rdp.component.jwtsession;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RdpJwtCheckResult {

    private boolean success;
    private String  message;
    private int     errorCode;

}
