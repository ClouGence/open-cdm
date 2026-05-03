package com.clougence.rdp.service.openapi.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiUpdatePriHostFO {

    @Min(value = 1, message = "{min.datasourceid}")
    private long   dataSourceId;

    @NotBlank(message = "{notblank.privatehost}")
    private String privateHost;

    private String privateHttpHost;
}
