package com.clougence.rdp.controller.model.lo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDsEnvLO {

    private Long   dsEnvId;
    private String oldEnvName;
    private String oldDescription;
    private String newEnvName;
    private String newDescription;
}
