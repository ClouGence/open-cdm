package com.clougence.rdp.service.openapi.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiUpdateDsDescFO {

    @Min(value = 1, message = "{min.datasourceid}")
    private Long   dataSourceId;

    @NotBlank(message = "{notblank.instancedesc}")
    private String instanceDesc;
}
