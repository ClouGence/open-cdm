package com.clougence.rdp.service.openapi.model;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiListDsKvConfigsByDsIdFO {

    @Min(value = 1, message = "{min.datasourceid}")
    long   dataSourceId;

    String configName;
}
