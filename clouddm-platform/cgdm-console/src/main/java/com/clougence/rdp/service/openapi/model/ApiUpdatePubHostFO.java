package com.clougence.rdp.service.openapi.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiUpdatePubHostFO {

    @Min(value = 1, message = "{min.datasourceid}")
    private long   dataSourceId;

    @NotBlank(message = "{notblank.publichost}")
    private String publicHost;

    private String publicHttpHost;
}
