package com.clougence.rdp.controller.model.lo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePriHostLO {

    private Long   dataSourceId;
    private String oldPrivateHost;
    private String newPrivateHost;
}
