package com.clougence.rdp.controller.model.lo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePubHostLO {

    private Long   dataSourceId;
    private String oldPublicHost;
    private String newPublicHost;
}
